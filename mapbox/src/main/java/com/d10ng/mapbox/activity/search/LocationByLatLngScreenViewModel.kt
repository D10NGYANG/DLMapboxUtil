package com.d10ng.mapbox.activity.search

import androidx.lifecycle.ViewModel
import com.d10ng.mapbox.model.MapModel
import com.mapbox.geojson.Point

class LocationByLatLngScreenViewModel : ViewModel() {

    /** 地图样式 */
    val layerFlow = MapModel.instant.layerTypeFlow

    /** 缩放比例 */
    val zoomFlow = MapModel.instant.zoomFlow

    /** 地图中心 */
    val targetFlow = MapModel.instant.targetFlow

    /** 点击纬度进行编辑 */
    fun onClickLat() {
        LocationSearchActivity.instant.get()?.apply {
            // TODO
//            app.showDialog(InputDialogBuilder(
//                title = "纬度",
//                message = "请输入目标纬度，-90至90，eg:22.3",
//                inputs = listOf(InputDialogBuilder.Input(
//                    initValue = targetFlow.value.latitude().toString(),
//                    placeholder = "请输入",
//                    keyboardType = KeyboardType.Number,
//                    singleLine = true,
//                    verify = {
//                        val lat = it.toDoubleOrNull()
//                        if (lat == null || lat !in -90.0..90.0)
//                            InputDialogBuilder.Verify(false, "纬度数值不正确！")
//                        else InputDialogBuilder.Verify(true)
//                    }
//                )),
//                sureButton = "确定",
//                cancelButton = "取消",
//                onClickSure = {
//                    app.hideDialog()
//                    updateTarget(
//                        Point.fromLngLat(
//                            targetFlow.value.longitude(),
//                            it[0].toDoubleOrNull() ?: 0.0
//                        )
//                    )
//                },
//                onClickCancel = {
//                    app.hideDialog()
//                }
//            ))
        }
    }

    /** 点击经度进行编辑 */
    fun onClickLng() {
        LocationSearchActivity.instant.get()?.apply {
            // TODO
//            app.showDialog(InputDialogBuilder(
//                title = "经度",
//                message = "请输入目标经度，-180至180，eg:113.2",
//                inputs = listOf(InputDialogBuilder.Input(
//                    initValue = targetFlow.value.longitude().toString(),
//                    placeholder = "请输入",
//                    keyboardType = KeyboardType.Number,
//                    singleLine = true,
//                    verify = {
//                        val lng = it.toDoubleOrNull()
//                        if (lng == null || lng !in -180.0..180.0)
//                            InputDialogBuilder.Verify(false, "经度数值不正确！")
//                        else InputDialogBuilder.Verify(true)
//                    }
//                )),
//                sureButton = "确定",
//                cancelButton = "取消",
//                onClickSure = {
//                    app.hideDialog()
//                    updateTarget(
//                        Point.fromLngLat(
//                            it[0].toDoubleOrNull() ?: 0.0,
//                            targetFlow.value.latitude()
//                        )
//                    )
//                },
//                onClickCancel = {
//                    app.hideDialog()
//                }
//            ))
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
        // TODO
//        LocationSearchActivity.instant.get()?.apply {
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
        MapModel.instant.move2CurrentLocation()
    }

    /** 点击确定 */
    fun onClickSure() {
        LocationSearchManager.instant.finish(targetFlow.value)
    }
}