package com.d10ng.mapbox.activity.show

import androidx.lifecycle.ViewModel
import com.d10ng.app.resource.makeBitmapFromDrawable
import com.d10ng.mapbox.R
import com.d10ng.mapbox.stores.MapViewStore
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import kotlinx.coroutines.flow.MutableStateFlow

class LocationShowScreenViewModel: ViewModel() {

    companion object {
        private const val POINT = "POINT"
    }

    private val _initPoint = LocationShowActivity.initPoint

    /** 地图样式 */
    val layerFlow = MapViewStore.layerTypeFlow

    /** 缩放比例 */
    val zoomFlow = MapViewStore.zoomFlow

    /** 地图中心 */
    val targetFlow = MutableStateFlow(_initPoint)

    /** 图标 */
    val pointOptionFlow = MutableStateFlow(
        PointAnnotationOptions()
            .withGeometry(_initPoint)
            .withIconImage(POINT)
            .withIconSize(1.0)
    )

    /** 点击返回 */
    fun onClickBack() {
        LocationShowActivity.instant.get()?.finish()
    }

    /** 地图加载完成 */
    fun onMapStyleLoad(style: Style) {
        LocationShowActivity.instant.get()?.apply {
            makeBitmapFromDrawable(R.drawable.ic_map_location_target_25)?.apply {
                style.addImage(POINT, this)
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
        targetFlow.value = value
    }

    /** 点击图层切换 */
    fun onClickLayer() {
        // TODO
//        LocationShowActivity.instant.get()?.apply {
//            app.showDialog(MapLayerDialogBuilder(
//                value = layerFlow.value,
//                onChange = {
//                    app.hideDialog()
//                    MapModel.instant.updateLayer(this, it)
//                }
//            ))
//        }
    }

    /** 点击移动到位置 */
    fun onClickLocation() {
        targetFlow.value = _initPoint
    }

    /** 点击到这去 */
    fun onClickGo() {
        LocationShowActivity.instant.get()?.apply {
            // TODO
//            app.showDialog(RadioDialogBuilder(
//                title = "提示",
//                message = "使用第三方地图进行导航规划",
//                map = OtherMapType.toDialogMap(),
//                select = "",
//                customItemView = { _, info, onClick ->
//                    OtherMapRadioDialogItem(info = info, onClick = onClick)
//                },
//                isRow = false,
//                onSelect = { select ->
//                    app.hideDialog()
//                    val type = select.second as OtherMapType
//                    val dLatLng = DLatLng(_initPoint.latitude(), _initPoint.longitude())
//                    val latlng =
//                        dLatLng.convert(CoordinateSystemType.WGS84, CoordinateSystemType.GCJ02)
//                    when (type) {
//                        OtherMapType.GAODE -> startGaoDeMapMaker(
//                            latlng.latitude,
//                            latlng.longitude
//                        )
//                        OtherMapType.BAIDU -> startBaiDuMapMaker(
//                            latlng.latitude,
//                            latlng.longitude
//                        )
//                        else -> {}
//                    }
//                }
//            ))
        }
    }
}