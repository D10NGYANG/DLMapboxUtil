package com.d10ng.mapbox.activity.offline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.mapbox.activity.search.LocationSearchManager
import com.d10ng.mapbox.constant.MapLayerType
import com.d10ng.mapbox.destinations.MapOfflineAddScreenDestination
import com.d10ng.mapbox.stores.MapViewStore
import com.d10ng.mapbox.view.MapLayerDialogBuilder
import com.mapbox.geojson.Point
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

class MapOfflineAreaScreenViewModel : ViewModel() {

    /** 地图样式 */
    val layerFlow = MapViewStore.layerTypeFlow

    /** 缩放比例 */
    val zoomFlow = MapViewStore.zoomFlow

    /** 地图中心 */
    val targetFlow = MapViewStore.targetFlow

    init {
        viewModelScope.launch {
            layerFlow.collect { type ->
                if (!type.isCanDown) {
                    MapViewStore.updateLayer(MapLayerType.MAPBOX_STREETS_ZH)
                }
            }
        }
    }

    /** 点击搜索 */
    fun onClickSearch() {
        LocationSearchManager.start {
            if (it != null) {
                updateTarget(it)
            }
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
            isOnlyShowCanDown = true,
            onChange = {
                MapViewStore.updateLayer(it)
            }
        ))
    }

    /** 点击移动到当前位置 */
    fun onClickLocation() {
        MapViewStore.moveToCurrentLocation()
    }

    /** 点击下载 */
    fun onClickDownload(nav: DestinationsNavigator) {
        nav.navigate(MapOfflineAddScreenDestination())
    }
}