package com.d10ng.mapbox.activity.map

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.d10ng.compose.BaseActivity
import com.d10ng.compose.BaseComposeScreenObject
import com.d10ng.compose.BaseViewModel
import com.d10ng.latlnglib.toLatitudeString
import com.d10ng.latlnglib.toLongitudeString
import com.d10ng.mapbox.model.LocationModel
import com.d10ng.mapbox.model.MapModel
import com.d10ng.mapbox.view.MapLayerDialogBuilder
import com.google.accompanist.navigation.animation.composable
import com.mapbox.geojson.Point
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

object MapMainScreenObj : BaseComposeScreenObject("MapMainScreen") {
    @OptIn(ExperimentalAnimationApi::class)
    override fun composable(
        builder: NavGraphBuilder,
        controller: NavHostController,
        act: BaseActivity
    ) {
        builder.composable(name) {
            MapMainScreen(controller, act)
        }
    }
}

class MapMainScreenViewModel : BaseViewModel() {

    /** 位置文本 */
    val locationTextFlow = MutableStateFlow("正在获取当前位置...")

    /** 地图样式 */
    val layerFlow = MapModel.instant.layerTypeFlow

    /** 缩放比例 */
    val zoomFlow = MapModel.instant.zoomFlow

    /** 地图中心 */
    val targetFlow = MapModel.instant.targetFlow

    init {
        viewModelScope.launch {
            val pattern = "Fd°m′S.ss″"
            LocationModel.instant.locationFlow.collect {
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
        weakAct.get()?.finish()
    }

    /** 点击离线地图 */
    fun onClickOffline() {
        controller?.let { MapOfflineListScreenObj.go(it) }
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

    /** 点击移动到当前位置 */
    fun onClickLocation() {
        MapModel.instant.move2CurrentLocation()
    }
}