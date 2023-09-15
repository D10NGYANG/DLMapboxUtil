package com.d10ng.mapbox.activity.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d10ng.latlnglib.toLatitudeString
import com.d10ng.latlnglib.toLongitudeString
import com.d10ng.mapbox.activity.destinations.MapOfflineListScreenDestination
import com.d10ng.mapbox.stores.LocationStore
import com.d10ng.mapbox.stores.MapViewStore
import com.mapbox.geojson.Point
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MapMainScreenViewModel : ViewModel() {

    /** 位置文本 */
    val locationTextFlow = MutableStateFlow("正在获取当前位置...")

    /** 地图样式 */
    val layerFlow = MapViewStore.layerTypeFlow

    /** 缩放比例 */
    val zoomFlow = MapViewStore.zoomFlow

    /** 地图中心 */
    val targetFlow = MapViewStore.targetFlow

    init {
        viewModelScope.launch {
            val pattern = "Fd°m′S.ss″"
            LocationStore.getValueFlow().collect {
                it ?: return@collect
                val builder = StringBuilder("位置:")
                builder.append(it.latitude.toLatitudeString(pattern))
                    .append(",")
                    .append(it.longitude.toLongitudeString(pattern))
                locationTextFlow.emit(builder.toString())
            }
        }
    }


    /** 点击返回 */
    fun onClickBack() {
        MapActivity.instant.get()?.finish()
    }

    /** 点击离线地图 */
    fun onClickOffline(nav: DestinationsNavigator) {
        nav.navigate(MapOfflineListScreenDestination)
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
        // TODO
//        MapActivity.instant.get()?.apply {
//            app.showDialog(MapLayerDialogBuilder(
//                value = layerFlow.value,
//                onChange = {
//                    app.hideDialog()
//                    MapModel.instant.updateLayer(this, it)
//                }
//            ))
//        }
    }

    /** 点击移动到当前位置 */
    fun onClickLocation() {
        MapViewStore.moveToCurrentLocation()
    }
}