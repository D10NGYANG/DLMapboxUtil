package com.d10ng.mapbox.activity.map

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.BaseComposeScreenObject
import com.d10ng.basicjetpackcomposeapp.BaseViewModel
import com.d10ng.mapbox.activity.search.LocationSearchManager
import com.d10ng.mapbox.constant.MapLayerType
import com.d10ng.mapbox.model.MapModel
import com.d10ng.mapbox.view.MapLayerDialogBuilder
import com.google.accompanist.navigation.animation.composable
import com.mapbox.geojson.Point
import kotlinx.coroutines.launch

object MapOfflineAreaScreenObj : BaseComposeScreenObject("MapOfflineAreaScreen") {
    @OptIn(ExperimentalAnimationApi::class)
    override fun composable(
        builder: NavGraphBuilder,
        controller: NavHostController,
        act: BaseActivity
    ) {
        builder.composable(name) {
            MapOfflineAreaScreen(controller, act)
        }
    }
}

class MapOfflineAreaScreenViewModel : BaseViewModel() {

    /** 地图样式 */
    val layerFlow = MapModel.instant.layerTypeFlow

    /** 缩放比例 */
    val zoomFlow = MapModel.instant.zoomFlow

    /** 地图中心 */
    val targetFlow = MapModel.instant.targetFlow

    override fun init(act: BaseActivity, controller: NavHostController) {
        super.init(act, controller)
        viewModelScope.launch {
            layerFlow.collect { type ->
                if (!type.isCanDown) {
                    MapModel.instant.updateLayer(
                        act.applicationContext,
                        MapLayerType.MAPBOX_STREETS
                    )
                }
            }
        }
    }

    /** 点击返回 */
    fun onClickBack() {
        controller?.navigateUp()
    }

    /** 点击搜索 */
    fun onClickSearch() {
        weakAct.get()?.apply {
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
        weakAct.get()?.apply {
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
    fun onClickDownload() {
        controller?.let { MapOfflineAddScreenObj.go(it) }
    }
}