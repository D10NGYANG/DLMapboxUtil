package com.d10ng.mapbox.activity.show

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.d10ng.app.managers.ActivityManager
import com.d10ng.app.resource.toBitmap
import com.d10ng.common.coordinate.Coordinate
import com.d10ng.common.coordinate.CoordinateSystemType
import com.d10ng.common.coordinate.convert
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.sheet.builder.ActionSheetBuilder
import com.d10ng.mapbox.R
import com.d10ng.mapbox.startup.StartupInitializer
import com.d10ng.mapbox.stores.MapViewStore
import com.d10ng.mapbox.utils.toShowString
import com.d10ng.mapbox.view.MapLayerDialogBuilder
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
            .withIconSize(0.8)
            .withIconOffset(listOf(0.0, -20.0))
    )

    /** 位置文本 */
    val locationTextFlow = MutableStateFlow(_initPoint.toShowString())

    /** 点击返回 */
    fun onClickBack() {
        ActivityManager.finishTop()
    }

    /** 地图加载完成 */
    fun onMapStyleLoad(style: Style) {
        StartupInitializer.application
            .getDrawable(R.drawable.ic_map_point_32)?.apply {
                style.addImage(POINT, this.toBitmap())
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
        UiViewModelManager.showDialog(MapLayerDialogBuilder(
            value = MapViewStore.getCurrentLayer(),
            onChange = {
                MapViewStore.updateLayer(it)
            }
        ))
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
                when (value) {
                    "高德地图" -> goToGaodeMap(_initPoint.longitude(), _initPoint.latitude(), "位置信息")
                    "百度地图" -> goToBaiduMap(_initPoint.longitude(), _initPoint.latitude(), "位置信息")
                    else -> {}
                }
            }
        ))
    }

    /**
     * 跳转到高德地图
     * @param longitude 经度
     * @param latitude 纬度
     * @param name 名称
     */
    private fun goToGaodeMap(longitude: Double, latitude: Double, name: String) {
        ActivityManager.top()?.apply {
            val install = runCatching { packageManager.getPackageInfo("com.autonavi.minimap", 0) }.isSuccess
            if (install.not()) {
                UiViewModelManager.showToast("请先安装高德地图", 3000)
                return
            }
            val loc = Coordinate(latitude, longitude).convert(CoordinateSystemType.WGS84, CoordinateSystemType.GCJ02)
            val uri = Uri.parse("androidamap://viewMap?sourceApplication=cim&poiname=${name}&lat=${loc.lat}&lon=${loc.lng}&dev=0")
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }

    /**
     * 跳转到百度地图
     * @param longitude 经度
     * @param latitude 纬度
     * @param name 名称
     */
    private fun goToBaiduMap(longitude: Double, latitude: Double, name: String) {
        ActivityManager.top()?.apply {
            val install = runCatching { packageManager.getPackageInfo("com.baidu.BaiduMap", 0) }.isSuccess
            if (install.not()) {
                UiViewModelManager.showToast("请先安装百度地图", 3000)
                return
            }
            val loc = Coordinate(latitude, longitude).convert(CoordinateSystemType.WGS84, CoordinateSystemType.BD09)
            val uri = Uri.parse("baidumap://map/marker?location=${loc.lat},${loc.lng}&title=${name}&traffic=on&src=andr.hailiao.cim")
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }
}