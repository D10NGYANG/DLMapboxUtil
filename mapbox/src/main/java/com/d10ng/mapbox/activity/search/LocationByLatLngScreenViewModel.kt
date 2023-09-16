package com.d10ng.mapbox.activity.search

import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.dialog.builder.InputDialogBuilder
import com.d10ng.mapbox.stores.MapViewStore
import com.d10ng.mapbox.view.MapLayerDialogBuilder
import com.mapbox.geojson.Point
import kotlinx.coroutines.launch

class LocationByLatLngScreenViewModel : ViewModel() {

    /** 地图样式 */
    val layerFlow = MapViewStore.layerTypeFlow

    /** 缩放比例 */
    val zoomFlow = MapViewStore.zoomFlow

    /** 地图中心 */
    val targetFlow = MapViewStore.targetFlow

    /** 点击纬度进行编辑 */
    fun onClickLat() {
        UiViewModelManager.showDialog(InputDialogBuilder(
            title = "纬度",
            inputs = listOf(InputDialogBuilder.Input(
                initValue = targetFlow.value.latitude().toString(),
                placeholder = "请输入",
                label = "请输入目标纬度，-90至90，eg:22.3",
                keyboardType = KeyboardType.Number,
                singleLine = true,
                verify = {
                    val lat = it.toDoubleOrNull()
                    if (lat == null || lat !in -90.0..90.0)
                        InputDialogBuilder.Verify(false, "纬度数值不正确！")
                    else InputDialogBuilder.Verify(true)
                }
            )),
            onConfirmClick = {
                updateTarget(
                    Point.fromLngLat(
                        targetFlow.value.longitude(),
                        it[0].toDoubleOrNull() ?: 0.0
                    )
                )
                true
            }
        ))
    }

    /** 点击经度进行编辑 */
    fun onClickLng() {
        UiViewModelManager.showDialog(InputDialogBuilder(
            title = "经度",
            inputs = listOf(InputDialogBuilder.Input(
                initValue = targetFlow.value.longitude().toString(),
                placeholder = "请输入",
                label = "请输入目标经度，-180至180，eg:113.2",
                keyboardType = KeyboardType.Number,
                singleLine = true,
                verify = {
                    val lng = it.toDoubleOrNull()
                    if (lng == null || lng !in -180.0..180.0)
                        InputDialogBuilder.Verify(false, "经度数值不正确！")
                    else InputDialogBuilder.Verify(true)
                }
            )),
            onConfirmClick = {
                updateTarget(
                    Point.fromLngLat(
                        it[0].toDoubleOrNull() ?: 0.0,
                        targetFlow.value.latitude()
                    )
                )
                true
            }
        ))
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

    /** 点击确定 */
    fun onClickSure() {
        viewModelScope.launch { LocationSearchManager.finish(targetFlow.value) }
    }
}