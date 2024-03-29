package com.d10ng.mapbox.stores

import android.app.Application
import android.graphics.Bitmap
import com.d10ng.mapbox.bean.OfflineMapInfo
import com.d10ng.mapbox.utils.getAllOfflineInfo
import com.d10ng.mapbox.utils.getSnapshotBitmap
import com.mapbox.bindgen.Expected
import com.mapbox.bindgen.Value
import com.mapbox.common.TileDataDomain
import com.mapbox.common.TileRegion
import com.mapbox.common.TileRegionError
import com.mapbox.common.TileRegionLoadOptions
import com.mapbox.common.TileRegionLoadProgress
import com.mapbox.common.TileStore
import com.mapbox.common.TileStoreObserver
import com.mapbox.common.TileStoreOptions
import com.mapbox.geojson.Geometry
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.OfflineManager
import com.mapbox.maps.ResourceOptionsManager
import com.mapbox.maps.TilesetDescriptorOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

/**
 * 地图管理器
 * @Author d10ng
 * @Date 2023/9/15 18:29
 */
object MapboxStore {

    private lateinit var application: Application

    internal fun init(app: Application) {
        application = app
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    var token = ""

    /** 离线地图瓦片管理器 */
    private var tileStore: TileStore? = null

    /** 离线地图信息 */
    val offlineMapInfoListFlow = MutableStateFlow(listOf<OfflineMapInfo>())

    /** 离线地图快照 */
    val offlineMapSnapshotFlow = MutableStateFlow(mapOf<String, Bitmap>())

    fun init(token: String) {
        this.token = token
        // 初始化TOKEN
        MapInitOptions(
            application,
            ResourceOptionsManager.getDefault(application, MapboxStore.token).resourceOptions
        )
        tileStore = TileStore.create().apply {
            setOption(
                TileStoreOptions.MAPBOX_ACCESS_TOKEN,
                TileDataDomain.MAPS,
                Value(token)
            )
            addObserver(tileStoreObserver)
        }
    }

    /** 离线地图瓦片监听器 */
    private val tileStoreObserver = object : TileStoreObserver {
        override fun onRegionLoadProgress(id: String, progress: TileRegionLoadProgress) {
            refreshMapOfflineList()
        }

        override fun onRegionLoadFinished(
            id: String,
            region: Expected<TileRegionError, TileRegion>
        ) {
            refreshMapOfflineList()
        }

        override fun onRegionRemoved(id: String) {
            refreshMapOfflineList()
        }

        override fun onRegionGeometryChanged(id: String, geometry: Geometry) {
            refreshMapOfflineList()
        }

        override fun onRegionMetadataChanged(id: String, value: Value) {
            refreshMapOfflineList()
        }
    }

    /** 刷新离线地图列表 */
    fun refreshMapOfflineList() {
        scope.launch {
            tileStore?.getAllOfflineInfo()?.apply {
                offlineMapInfoListFlow.emit(this)
            }
        }
    }

    /**
     * 添加离线下载任务
     * @param context Context
     * @param minZoom Double
     * @param maxZoom Double
     * @param title String
     */
    fun addOfflineDownload(
        minZoom: Int,
        maxZoom: Int,
        title: String,
    ) {
        val style = MapViewStore.getCurrentLayer().source
        val json = JSONObject().apply {
            put("style", style)
            put("minZoom", minZoom)
            put("maxZoom", maxZoom)
            put("title", title)
        }
        val offlineManager =
            OfflineManager(MapInitOptions.getDefaultResourceOptions(application))
        tileStore?.loadTileRegion(
            System.currentTimeMillis().toString(),
            TileRegionLoadOptions.Builder()
                .geometry(MapViewStore.targetFlow.value)
                .descriptors(
                    listOf(
                        offlineManager.createTilesetDescriptor(
                            TilesetDescriptorOptions.Builder()
                                .styleURI(style)
                                .minZoom(minZoom.toByte())
                                .maxZoom(maxZoom.toByte())
                                .build()
                        )
                    )
                )
                .metadata(Value(json.toString()))
                .build()
        )
    }

    /**
     * 重命名离线地图
     * @param id String
     * @param name String
     */
    fun renameOffline(id: String, name: String) {
        val info = offlineMapInfoListFlow.value.find { it.region.id == id } ?: return
        val json = JSONObject().apply {
            put("style", info.style.source)
            put("minZoom", info.minZoom)
            put("maxZoom", info.maxZoom)
            put("title", name)
        }
        val offlineManager =
            OfflineManager(MapInitOptions.getDefaultResourceOptions(application))
        tileStore?.loadTileRegion(
            info.region.id,
            TileRegionLoadOptions.Builder()
                .geometry(info.geometry)
                .descriptors(
                    listOf(
                        offlineManager.createTilesetDescriptor(
                            TilesetDescriptorOptions.Builder()
                                .styleURI(info.style.source)
                                .minZoom(info.minZoom.toByte())
                                .maxZoom(info.maxZoom.toByte())
                                .build()
                        )
                    )
                )
                .metadata(Value(json.toString()))
                .build()
        )
    }

    /**
     * 删除离线地图
     * @param id String
     */
    fun deleteOffline(id: String) {
        tileStore?.removeTileRegion(id)
        tileStore?.setOption(TileStoreOptions.DISK_QUOTA, Value(0))
    }

    /**
     * 更新快照
     */
    suspend fun updateSnapshot() {
        val offlineList = offlineMapInfoListFlow.value
        val map = offlineMapSnapshotFlow.value.toMutableMap()
        for (item in offlineList) {
            if (map.containsKey(item.region.id)) continue
            val bitmap = item.getSnapshotBitmap(application) ?: continue
            map[item.region.id] = bitmap
        }
        offlineMapSnapshotFlow.emit(map)
    }
}