package com.d10ng.mapbox.activity.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d10ng.app.managers.ActivityManager
import com.d10ng.app.utils.goTo
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.mapbox.activity.offline.MapOfflineActivity
import com.d10ng.mapbox.activity.search.LocationSearchManager
import com.d10ng.mapbox.stores.MapViewStore
import com.d10ng.mapbox.stores.NavigationStore
import com.d10ng.mapbox.view.MapLayerDialogBuilder
import com.mapbox.geojson.Point
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 地图导航
 * @Author d10ng
 * @Date 2023/9/16 17:14
 */
class MapNavigationHomeScreenViewModel : ViewModel() {

    /** 地图样式 */
    val layerFlow = MapViewStore.layerTypeFlow

    /** 缩放比例 */
    val zoomFlow = MapViewStore.zoomFlow

    /** 地图中心 */
    val targetFlow = MapViewStore.targetFlow

    /**
     * 点击返回
     */
    fun onClickBack() {
        ActivityManager.finishTop()
    }

    /**
     * 点击离线地图
     */
    fun onClickOffline() {
        ActivityManager.top()?.goTo(MapOfflineActivity::class.java)
    }

    /**
     * 点击搜索
     */
    fun onClickSearch(onNavigateOn: () -> Unit) {
        LocationSearchManager.start {
            it ?: return@start
            MapViewStore.updateTarget(it)
            setTarget(it, onNavigateOn)
        }
    }

    /** 点击放大 */
    fun onClickZoomIn() {
        MapViewStore.zoomIn()
    }

    /** 点击缩小 */
    fun onClickZoomOut() {
        MapViewStore.zoomOut()
    }

    /** 更新比例 */
    fun updateZoom(value: Double) {
        MapViewStore.updateZoom(value)
    }

    /** 更新地图中心 */
    fun updateTarget(value: Point) {
        MapViewStore.updateTarget(value)
    }

    /** 点击图层切换 */
    fun onClickLayer() {
        UiViewModelManager.showDialog(MapLayerDialogBuilder(
            value = MapViewStore.getCurrentLayer(),
            onChange = {
                MapViewStore.updateLayer(it)
            }
        ))
    }

    /** 点击移动到当前位置 */
    fun onClickLocation() {
        MapViewStore.moveToCurrentLocation()
    }

    /**
     * 点击设置目的地
     */
    fun onClickSet(onNavigateOn: () -> Unit) {
        setTarget(targetFlow.value, onNavigateOn)
    }

    /**
     * 设置目的地
     * @param point Point
     */
    private fun setTarget(point: Point, onNavigateOn: () -> Unit) {
        viewModelScope.launch {
            NavigationStore.setTarget(point)
            withContext(Dispatchers.Main) {
                onNavigateOn()
            }
        }
    }
}
