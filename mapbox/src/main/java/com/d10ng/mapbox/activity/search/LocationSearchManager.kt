package com.d10ng.mapbox.activity.search

import android.app.Activity
import com.d10ng.app.base.goTo
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.tianditu.api.TianDiTuApi
import com.d10ng.tianditu.bean.LocationSearch
import com.mapbox.geojson.Point
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LocationSearchManager {

    companion object {
        val instant by lazy { LocationSearchManager() }
    }

    /** 经纬度 */
    private val resultFlow = MutableSharedFlow<Point?>()

    /** 等待结果协程 */
    private var scope: CoroutineScope? = null

    /**
     * 打开页面
     * @param act Activity
     * @param result Function1<Point?, Unit>
     */
    @Synchronized
    fun startActivity(act: Activity, result: (Point?) -> Unit) {
        act.goTo(LocationSearchActivity::class.java)
        scope?.cancel()
        scope = CoroutineScope(Dispatchers.IO).apply {
            launch {
                resultFlow.collect {
                    result.invoke(it)
                    LocationSearchActivity.instant.get()?.finish()
                    this.cancel()
                }
            }
        }
    }

    /**
     * 获得定位
     * @param result Point?
     */
    @Synchronized
    fun finish(result: Point?) {
        CoroutineScope(Dispatchers.IO).launch { resultFlow.emit(result) }
    }

    /** 获取结果 */
    fun getResultFlow() = resultFlow.asSharedFlow()

    /** 搜索 */
    suspend fun search(
        // TODO
        act: Activity,
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
}