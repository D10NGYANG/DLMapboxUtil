package com.d10ng.mapbox.activity.show

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.d10ng.applib.resource.makeBitmapFromDrawable
import com.d10ng.compose.BaseViewModel
import com.d10ng.compose.dialog.builder.RadioDialogBuilder
import com.d10ng.gps.startBaiDuMapMaker
import com.d10ng.gps.startGaoDeMapMaker
import com.d10ng.latlnglib.bean.DLatLng
import com.d10ng.latlnglib.constant.CoordinateSystemType
import com.d10ng.latlnglib.convert
import com.d10ng.mapbox.R
import com.d10ng.mapbox.model.MapModel
import com.d10ng.mapbox.view.MapLayerDialogBuilder
import com.d10ng.mapbox.view.OtherMapRadioDialogItem
import com.d10ng.mapbox.view.OtherMapType
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import kotlinx.coroutines.flow.MutableStateFlow

class LocationShowScreenViewModel(
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    companion object {
        private const val POINT = "POINT"
        private const val LATITUDE = "LATITUDE"
        private const val LONGITUDE = "LONGITUDE"
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        owner: SavedStateRegistryOwner,
        private val point: Point
    ) : AbstractSavedStateViewModelFactory(owner, null) {
        override fun <T : ViewModel> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T {
            handle[LATITUDE] = point.latitude()
            handle[LONGITUDE] = point.longitude()
            return LocationShowScreenViewModel(handle) as T
        }
    }

    private val _latitude = savedStateHandle.get<Double>(LATITUDE) ?: 0.0
    private val _longitude = savedStateHandle.get<Double>(LONGITUDE) ?: 0.0
    private val _initPoint = Point.fromLngLat(_longitude, _latitude)

    /** 地图样式 */
    val layerFlow = MapModel.instant.layerTypeFlow

    /** 缩放比例 */
    val zoomFlow = MapModel.instant.zoomFlow

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
        weakAct.get()?.finish()
    }

    /** 地图加载完成 */
    fun onMapStyleLoad(style: Style) {
        weakAct.get()?.apply {
            makeBitmapFromDrawable(R.drawable.ic_map_location_target_25)?.apply {
                style.addImage(POINT, this)
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
        targetFlow.value = value
    }

    /** 点击图层切换 */
    fun onClickLayer() {
        weakAct.get()?.apply {
            app.showDialog(MapLayerDialogBuilder(
                value = layerFlow.value,
                onChange = {
                    app.hideDialog()
                    MapModel.instant.updateLayer(this, it)
                }
            ))
        }
    }

    /** 点击移动到位置 */
    fun onClickLocation() {
        targetFlow.value = _initPoint
    }

    /** 点击到这去 */
    fun onClickGo() {
        weakAct.get()?.apply {
            app.showDialog(RadioDialogBuilder(
                title = "提示",
                message = "使用第三方地图进行导航规划",
                map = OtherMapType.toDialogMap(),
                select = "",
                customItemView = { _, info, onClick ->
                    OtherMapRadioDialogItem(info = info, onClick = onClick)
                },
                isRow = false,
                onSelect = { select ->
                    app.hideDialog()
                    val type = select.second as OtherMapType
                    val dLatLng = DLatLng(_initPoint.latitude(), _initPoint.longitude())
                    val latlng =
                        dLatLng.convert(CoordinateSystemType.WGS84, CoordinateSystemType.GCJ02)
                    when (type) {
                        OtherMapType.GAODE -> startGaoDeMapMaker(
                            latlng.latitude,
                            latlng.longitude
                        )
                        OtherMapType.BAIDU -> startBaiDuMapMaker(
                            latlng.latitude,
                            latlng.longitude
                        )
                        else -> {}
                    }
                }
            ))
        }
    }
}