package com.d10ng.mapbox.activity.search

import com.d10ng.app.managers.ActivityManager
import com.d10ng.app.utils.goTo
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.mapbox.bean.HistoryInfo
import com.d10ng.mapbox.bean.createHistoryInfo
import com.d10ng.mapbox.bean.toHistoryInfo
import com.d10ng.mapbox.stores.HistoryStore
import com.d10ng.mapbox.stores.LocationStore
import com.d10ng.tianditu.api.TianDiTuApi
import com.d10ng.tianditu.bean.LocationSearch
import com.d10ng.tianditu.bean.PerimeterSearch
import com.d10ng.tianditu.bean.ReGeocode
import com.mapbox.geojson.Point
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

/**
 * 位置搜索管理器
 */
object LocationSearchManager {

    // 经纬度
    private val resultFlow = MutableSharedFlow<Point?>()
    private val scope = CoroutineScope(Dispatchers.IO)
    // 等待结果协程
    private var job: Job? = null

    /**
     * 打开页面
     * @param result Function1<Point?, Unit>
     */
    @Synchronized
    fun start(result: (Point?) -> Unit) {
        ActivityManager.top()?.goTo(LocationSearchActivity::class.java)
        job?.cancel()
        job = scope.launch {
            resultFlow.take(1).collect {
                result(it)
                ActivityManager.get<LocationSearchActivity>()?.finish()
            }
        }
    }

    internal suspend fun finish(geocode: ReGeocode) {
        finish(geocode.toHistoryInfo())
    }

    internal suspend fun finish(poi: LocationSearch.Poi) {
        finish(poi.toHistoryInfo())
    }

    internal suspend fun finish(name: String, address: String, point: Point) {
        finish(createHistoryInfo(name, address, point))
    }

    internal suspend fun finish(history: HistoryInfo) {
        HistoryStore.add(history)
        finish(Point.fromLngLat(history.longitude, history.latitude))
    }

    /**
     * 获得定位
     * @param result Point?
     */
    internal suspend fun finish(result: Point?) {
        resultFlow.emit(result)
    }

    /**
     * 搜索
     * @param value String
     * @param specify String?
     * @return LocationSearch?
     */
    suspend fun search(
        value: String,
        specify: String? = null,
    ): LocationSearch? {
        if (value.isEmpty()) return null
        val result = TianDiTuApi.getLocationSearchV2(value, specify)
        return if (result != null && result.status.infocode != 1000) {
            UiViewModelManager.showErrorNotify(result.status.cndesc)
            null
        } else result
    }

    suspend fun searchWithPerimeter(
        value: String
    ): Pair<PerimeterSearch?, LocationSearch?> {
        if (value.isEmpty()) return null to null
        val loc = LocationStore.getValueFlow().value ?: return null to search(value, null)
        val perimeter = TianDiTuApi.getPerimeterSearch(value, loc.longitude, loc.latitude)
        return if (perimeter == null || perimeter.status.infocode != 1000) {
            perimeter?.status?.cndesc?.let { UiViewModelManager.showErrorNotify(it) }
            null to search(value, null)
        } else if (perimeter.count.toInt() < 15) {
            perimeter to search(value, null)
        } else {
            perimeter to null
        }
    }
}