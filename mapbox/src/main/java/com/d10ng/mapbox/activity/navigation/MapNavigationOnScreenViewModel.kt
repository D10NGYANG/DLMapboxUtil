package com.d10ng.mapbox.activity.navigation

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d10ng.app.base.ActivityManager
import com.d10ng.app.resource.makeBitmapFromDrawable
import com.d10ng.common.toString
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.AppColor
import com.d10ng.latlnglib.bean.DLatLng
import com.d10ng.latlnglib.getDistanceOn2Points
import com.d10ng.mapbox.R
import com.d10ng.mapbox.startup.StartupInitializer
import com.d10ng.mapbox.stores.LocationStore
import com.d10ng.mapbox.stores.MapViewStore
import com.d10ng.mapbox.stores.NavigationStore
import com.d10ng.mapbox.view.MapLayerDialogBuilder
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Timer
import kotlin.concurrent.schedule

/**
 * 地图导航
 * @Author d10ng
 * @Date 2023/9/16 17:46
 */
@OptIn(FlowPreview::class)
class MapNavigationOnScreenViewModel : ViewModel() {

    companion object {
        private const val TARGET = "TARGET"
        private const val CURRENT = "CURRENT"
    }


    /** 地图样式 */
    val layerFlow = MapViewStore.layerTypeFlow

    /** 缩放比例 */
    val zoomFlow = MapViewStore.zoomFlow

    /** 地图中心 */
    val targetFlow = MapViewStore.targetFlow

    /** 地图上的图标 */
    val pointOptionsFlow = MutableStateFlow(mapOf<Int, PointAnnotationOptions>())

    /** 地图上的线 */
    val lineOptionsFlow = MutableStateFlow(mapOf<Int, PolylineAnnotationOptions>())

    /** 距离文本 */
    val distanceTextFlow = MutableStateFlow("")

    /** 用户是否在触摸地图 */
    val isTouchMapFlow = MutableStateFlow(false)

    /** 触摸等待定时器 */
    private var resumeTimer: Timer? = null

    init {
        viewModelScope.launch {
            LocationStore.getValueFlow()
                .combine(NavigationStore.targetFlow) { cur, tar -> Pair(cur, tar) }
                .debounce(1000L)
                .collect {
                    val cur =
                        Point.fromLngLat(it.first?.longitude ?: 0.0, it.first?.latitude ?: 0.0)
                    val curAngle = (it.first?.bearing ?: 0f).toDouble()
                    val tar =
                        Point.fromLngLat(
                            it.second?.longitude() ?: 0.0,
                            it.second?.latitude() ?: 0.0
                        )
                    // 计算距离
                    val distance = getDistanceOn2Points(
                        DLatLng(cur.latitude(), cur.longitude()),
                        DLatLng(tar.latitude(), tar.longitude())
                    ) / 1000.0
                    distanceTextFlow.emit("${distance.toString(2)} km")
                    // 更新地图图标
                    val pointOptions = mapOf(
                        1 to PointAnnotationOptions()
                            .withGeometry(tar)
                            .withIconImage(TARGET)
                            .withIconSize(1.0),
                        2 to PointAnnotationOptions()
                            .withGeometry(cur)
                            .withIconImage(CURRENT)
                            .withIconSize(1.0)
                            .withIconRotate(curAngle)
                    )
                    pointOptionsFlow.emit(pointOptions)
                    // 更新地图线条
                    val lineOptions = mapOf(
                        1 to PolylineAnnotationOptions()
                            .withPoints(listOf(cur, tar))
                            .withLineColor(AppColor.Main.primary.toArgb())
                            .withLineWidth(2.0)
                    )
                    lineOptionsFlow.emit(lineOptions)
                    // 更新地图中心
                    if (!isTouchMapFlow.value) {
                        // 用户已停止触摸地图
                        updateTarget(cur)
                    }
                }
        }
    }

    override fun onCleared() {
        resumeTimer?.cancel()
        super.onCleared()
    }

    /** 点击返回 */
    fun onClickBack(nav: DestinationsNavigator) {
        if (NavigationStore.targetFlow.value != null) {
            ActivityManager.finishTop()
        } else {
            nav.navigateUp()
        }
    }

    /** 地图加载完成 */
    fun onMapStyleLoad(style: Style) {
        StartupInitializer.application.apply {
            makeBitmapFromDrawable(R.drawable.ic_map_location_target_25)?.apply {
                style.addImage(TARGET, this)
            }
            makeBitmapFromDrawable(R.drawable.ic_map_location_maker_24)?.apply {
                style.addImage(CURRENT, this)
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
            onChange = {
                MapViewStore.updateLayer(it)
            }
        ))
    }

    /** 点击移动到位置 */
    fun onClickLocation() {
        MapViewStore.moveToCurrentLocation()
    }

    /** 点击结束 */
    fun onClickCancel() {
        viewModelScope.launch {
            NavigationStore.setTarget(null)
            withContext(Dispatchers.Main) {
                ActivityManager.finishTop()
            }
        }
    }

    /** 用户是否在触摸地图 */
    fun updateUserTouchMap(value: Boolean) {
        if (value) {
            isTouchMapFlow.value = true
            resumeTimer?.cancel()
            resumeTimer = Timer().apply {
                schedule(3000L) {
                    onClickResume()
                }
            }
        }
    }

    /** 点击继续导航 */
    fun onClickResume() {
        isTouchMapFlow.value = false
        onClickLocation()
        resumeTimer?.cancel()
    }
}