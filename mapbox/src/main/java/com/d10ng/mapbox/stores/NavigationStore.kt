package com.d10ng.mapbox.stores

import com.mapbox.geojson.Point
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * 导航数据存储
 * @Author d10ng
 * @Date 2023/9/16 17:37
 */
object NavigationStore {

    // 导航目标点
    val targetFlow = MutableStateFlow<Point?>(null)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            MapboxConfigDataStore.instant.getNavigationTargetFlow().collect {
                if (it.isNullOrEmpty() || it.contains(',').not()) {
                    targetFlow.value = null
                } else {
                    val arr = it.split(',')
                    targetFlow.value = Point.fromLngLat(arr[0].toDouble(), arr[1].toDouble())
                }
            }
        }
    }

    /**
     * 设置导航目标点
     * @param target Point?
     */
    suspend fun setTarget(target: Point?) {
        val str = if (target == null) ""
        else target.longitude().toString() + "," + target.latitude().toString()
        MapboxConfigDataStore.instant.setNavigationTarget(str)
    }
}