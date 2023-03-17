package com.d10ng.mapbox.activity.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d10ng.mapbox.activity.destinations.MapOfflineAddScreenDestination
import com.d10ng.mapbox.activity.search.LocationSearchManager
import com.d10ng.mapbox.constant.MapLayerType
import com.d10ng.mapbox.model.MapModel
import com.d10ng.mapbox.view.MapLayerDialogBuilder
import com.mapbox.geojson.Point
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

class MapOfflineAreaScreenViewModel : ViewModel() {

    /** 地图样式 */
    val layerFlow = MapModel.instant.layerTypeFlow

    /** 缩放比例 */
    val zoomFlow = MapModel.instant.zoomFlow

    /** 地图中心 */
    val targetFlow = MapModel.instant.targetFlow

    init {
        viewModelScope.launch {
            layerFlow.collect { type ->
                val act = MapActivity.instant.get()?: return@collect
                if (!type.isCanDown) {
                    MapModel.instant.updateLayer(
                        act.applicationContext,
                        MapLayerType.MAPBOX_STREETS
                    )
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
        MapModel.instant.zoomIn()
    }

    /** 点击缩小 */
    fun onClickZoomOut() {
        MapModel.instant.zoomOut()
    }

    /** 更新比例 */
    fun updateZoom(value: Double) {
        MapModel.instant.updateZoom(value)
    }

    /** 更新地图中心 */
    fun updateTarget(value: Point) {
        MapModel.instant.updateTarget(value)
    }

    /** 点击图层切换 */
    fun onClickLayer() {
        MapActivity.instant.get()?.apply {
            app.showDialog(MapLayerDialogBuilder(
                value = layerFlow.value,
                isOnlyShowCanDown = true,
                onChange = {
                    app.hideDialog()
                    MapModel.instant.updateLayer(this, it)
                }
            ))
        }
    }

    /** 点击移动到当前位置 */
    fun onClickLocation() {
        MapModel.instant.move2CurrentLocation()
    }

    /** 点击下载 */
    fun onClickDownload(nav: DestinationsNavigator) {
        nav.navigate(MapOfflineAddScreenDestination())
    }
}