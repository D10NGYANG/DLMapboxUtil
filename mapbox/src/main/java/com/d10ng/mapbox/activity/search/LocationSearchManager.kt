package com.d10ng.mapbox.activity.search

import android.app.Activity
import com.d10ng.applib.app.goTo
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.coroutines.launchIO
import com.d10ng.tianditu.api.TianDiTuApi
import com.d10ng.tianditu.bean.LocationSearch
import com.mapbox.geojson.Point
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class LocationSearchManager {

    companion object {
        val instant by lazy { LocationSearchManager() }
    }

    /** 经纬度 */
    private val resultFlow = MutableSharedFlow<Point?>()
    /** 等待结果协程 */
    private var scope: CoroutineScope? = null
    /** 当前活动 */
    private var curAct: WeakReference<Activity>? = null

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
                    curAct?.get()?.finish()
                    this.cancel()
                }
            }
        }
    }

    /**
     * 获得定位
     * @param act Activity
     * @param result Point?
     */
    @Synchronized
    fun finish(act: Activity, result: Point?) {
        curAct = WeakReference(act)
        launchIO { resultFlow.emit(result) }
    }

    /** 获取结果 */
    fun getResultFlow() = resultFlow.asSharedFlow()

    /** 搜索 */
    suspend fun search(act: BaseActivity, value: String, specify: String? = null,): LocationSearch? {
        if (value.isEmpty()) return null
        val result = TianDiTuApi.getLocationSearchV2(value, specify)
        return if (result != null && result.status.infocode != 1000) {
            act.app.showError(result.status.cndesc)
            null
        } else result
    }
}