package com.d10ng.mapbox.activity.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d10ng.mapbox.activity.destinations.MapOfflineAddScreenDestination
import com.d10ng.mapbox.activity.search.LocationSearchManager
import com.d10ng.mapbox.constant.MapLayerType
import com.d10ng.mapbox.stores.MapStore
import com.mapbox.geojson.Point
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

class MapOfflineAreaScreenViewModel : ViewModel() {

    /** 地图样式 */
    val layerFlow = MapStore.layerTypeFlow

    /** 缩放比例 */
    val zoomFlow = MapStore.zoomFlow

    /** 地图中心 */
    val targetFlow = MapStore.targetFlow

    init {
        viewModelScope.launch {
            layerFlow.collect { type ->
                if (!type.isCanDown) {
                    MapStore.updateLayer(MapLayerType.MAPBOX_STREETS)
                }
            }
        }
    }

    /** 点击搜索 */
    fun onClickSearch() {
        MapActivity.instant.get()?.apply {
            LocationSearchManager.instant.startActivity(this) {
                if (it != null) {
                    updateTarget(it)
                }
            }
        }
    }

    /** 点击放大 */
    fun onClickZoomIn() {
        MapStore.zoomIn()
    }

    /** 点击缩小 */
    fun onClickZoomOut() {
        MapStore.zoomOut()
    }

    /** 更新比例 */
    fun updateZoom(value: Double) {
        MapStore.updateZoom(value)
    }

    /** 更新地图中心 */
    fun updateTarget(value: Point) {
        MapStore.updateTarget(value)
    }

    /** 点击图层切换 */
    fun onClickLayer() {
        // TODO
//        MapActivity.instant.get()?.apply {
//            app.showDialog(MapLayerDialogBuilder(
//                value = layerFlow.value,
//                isOnlyShowCanDown = true,
//                onChange = {
//                    app.hideDialog()
//                    MapModel.instant.updateLayer(this, it)
//                }
//            ))
//        }
    }

    /** 点击移动到当前位置 */
    fun onClickLocation() {
        MapStore.moveToCurrentLocation()
    }

    /** 点击下载 */
    fun onClickDownload(nav: DestinationsNavigator) {
        nav.navigate(MapOfflineAddScreenDestination())
    }
}