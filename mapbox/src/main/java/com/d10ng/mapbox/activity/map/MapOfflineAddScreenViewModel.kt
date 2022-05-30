package com.d10ng.mapbox.activity.map

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.BaseComposeScreenObject
import com.d10ng.mapbox.model.MapboxModel
import com.google.accompanist.navigation.animation.composable
import kotlinx.coroutines.flow.MutableStateFlow
import java.lang.ref.WeakReference
import kotlin.math.roundToInt

object MapOfflineAddScreenObj: BaseComposeScreenObject("MapOfflineAddScreen") {
    @OptIn(ExperimentalAnimationApi::class)
    override fun composable(
        builder: NavGraphBuilder,
        controller: NavHostController,
        act: BaseActivity
    ) {
        builder.composable(name) {
            MapOfflineAddScreen(controller, act as MapActivity)
        }
    }
}

class MapOfflineAddScreenViewModel(
    private val controller: NavHostController,
    act: MapActivity
): ViewModel() {
    class Factory(
        private val controller: NavHostController,
        private val act: MapActivity
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MapOfflineAddScreenViewModel(controller, act) as T
        }
    }

    private val weakAct = WeakReference(act)

    /** 输入地图名称 */
    val inputNameFlow = MutableStateFlow("")
    /** 地图层级 */
    val zoomRangeFlow = MutableStateFlow(1f .. 3f)

    /** 点击返回 */
    fun onClickBack() {
        controller.navigateUp()
    }

    /** 更新输入地图名称 */
    fun updateInputName(value: String) {
        inputNameFlow.value = value
    }

    /** 更新地图层级 */
    fun updateZoomRange(value: ClosedFloatingPointRange<Float>) {
        zoomRangeFlow.value = value
    }

    /** 点击确定 */
    fun onClickSure() {
        weakAct.get()?.apply {
            if (inputNameFlow.value.isEmpty()) {
                app.showError("地图名称不能为空！")
                return
            }
            MapboxModel.instant.addOfflineDownload(
                context = this,
                minZoom = zoomRangeFlow.value.start.roundToInt(),
                maxZoom = zoomRangeFlow.value.endInclusive.roundToInt(),
                title = inputNameFlow.value
            )
            controller.popBackStack(MapOfflineListScreenObj.name, false)
        }
    }
}