package com.d10ng.mapbox.activity.show

import androidx.lifecycle.ViewModel
import com.d10ng.app.base.ActivityManager
import com.d10ng.app.base.startBaiDuMapMaker
import com.d10ng.app.base.startGaoDeMapMaker
import com.d10ng.app.resource.makeBitmapFromDrawable
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.sheet.builder.ActionSheetBuilder
import com.d10ng.latlnglib.bean.DLatLng
import com.d10ng.latlnglib.constant.CoordinateSystemType
import com.d10ng.latlnglib.convert
import com.d10ng.mapbox.R
import com.d10ng.mapbox.startup.StartupInitializer
import com.d10ng.mapbox.stores.MapViewStore
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import kotlinx.coroutines.flow.MutableStateFlow

class LocationShowScreenViewModel : ViewModel() {

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
        ActivityManager.top().value?.finish()
    }

    /** 地图加载完成 */
    fun onMapStyleLoad(style: Style) {
        StartupInitializer.application
            .makeBitmapFromDrawable(R.drawable.ic_map_location_target_25)?.apply {
                style.addImage(POINT, this)
            }
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
        UiViewModelManager.showSheet(ActionSheetBuilder(
            items = setOf("高德地图", "百度地图"),
            onItemClick = { value ->
                val act = ActivityManager.top().value ?: return@ActionSheetBuilder
                val d = DLatLng(_initPoint.latitude(), _initPoint.longitude())
                val point = d.convert(CoordinateSystemType.WGS84, CoordinateSystemType.GCJ02)
                when (value) {
                    "高德地图" -> act.startGaoDeMapMaker(
                        point.latitude,
                        point.longitude
                    )

                    "百度地图" -> act.startBaiDuMapMaker(
                        point.latitude,
                        point.longitude
                    )

                    else -> {}
                }
            }
        ))
    }
}