package com.d10ng.mapbox.stores

import com.d10ng.mapbox.constant.MapLayerType
import com.mapbox.geojson.Point
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * 地图数据存储
 * @Author d10ng
 * @Date 2023/9/15 18:12
 */
object MapViewStore {

    /** 地图最大缩放倍数 mapbox默认为25.5 */
    private const val MAP_BOX_ZOOM_MAX = 20.0

    /** 地图最小缩放倍数 mapbox默认为0.0 */
    private const val MAP_BOX_ZOOM_MIN = 1.0

    /** 地图默认缩放倍数 */
    private const val MAP_BOX_ZOOM_DEFAULT = 16.0

    /** 默认经纬度位置 */
    private val defaultPoint: Point = Point.fromLngLat(116.40769, 39.89945)

    private val locationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var moveToLocationJob: kotlinx.coroutines.Job? = null

    /** 图层样式 */
    val layerTypeFlow = MutableStateFlow(MapLayerType.TD_VECTOR)

    /** 缩放比例 */
    val zoomFlow = MutableStateFlow(MAP_BOX_ZOOM_DEFAULT)

    /** 地图中心 */
    val targetFlow = MutableStateFlow(defaultPoint)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            MapboxConfigDataStore.instant.getLayerFlow().collect {
                layerTypeFlow.value = it ?: MapLayerType.TD_VECTOR
            }
        }
    }

    /**
     * 获取当前地图样式
     * @return MapLayerType
     */
    fun getCurrentLayer() = MapboxConfigDataStore.instant.getLayerSync() ?: MapLayerType.TD_VECTOR

    /**
     * 更新地图样式
     * @param layer MapLayerType
     */
    fun updateLayer(layer: MapLayerType) {
        CoroutineScope(Dispatchers.IO).launch {
            MapboxConfigDataStore.instant.setLayer(layer)
        }
    }

    /** 放大 */
    fun zoomIn() {
        if (zoomFlow.value < MAP_BOX_ZOOM_MAX) {
            zoomFlow.value = (zoomFlow.value + 1).coerceAtMost(MAP_BOX_ZOOM_MAX)
        }
    }

    /** 缩小 */
    fun zoomOut() {
        if (zoomFlow.value > MAP_BOX_ZOOM_MIN) {
            zoomFlow.value = (zoomFlow.value - 1).coerceAtLeast(MAP_BOX_ZOOM_MIN)
        }
    }

    /**
     * 更新比例
     * @param value Double
     */
    fun updateZoom(value: Double) {
        zoomFlow.value = value.coerceAtLeast(MAP_BOX_ZOOM_MIN).coerceAtMost(MAP_BOX_ZOOM_MAX)
    }

    /**
     * 更新地图中心
     * @param point Point
     */
    fun updateTarget(point: Point) {
        targetFlow.value = point
    }

    /** 移动地图中心到当前位置 */
    fun moveToCurrentLocation() {
        LocationStore.getValueFlow().value?.let { location ->
            moveToLocationJob?.cancel()
            targetFlow.value = Point.fromLngLat(location.longitude, location.latitude)
            return
        }
        // The first location can arrive asynchronously after the button is tapped.
        if (moveToLocationJob?.isActive == true) return
        moveToLocationJob = locationScope.launch {
            val location = LocationStore.getValueFlow().filterNotNull().first()
            targetFlow.value = Point.fromLngLat(location.longitude, location.latitude)
        }
    }

    /** Center the first map on the first valid location without overriding user movement later. */
    suspend fun initializeTargetFromLocation() {
        if (targetFlow.value != defaultPoint) return
        val location = LocationStore.getValueFlow().filterNotNull().first()
        if (targetFlow.value == defaultPoint) {
            targetFlow.value = Point.fromLngLat(location.longitude, location.latitude)
        }
    }
}
