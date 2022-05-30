package com.d10ng.mapbox.activity.map

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.BaseComposeScreenObject
import com.d10ng.mapbox.model.MapModel
import com.google.accompanist.navigation.animation.composable
import com.mapbox.geojson.Point
import kotlinx.coroutines.flow.MutableStateFlow
import java.lang.ref.WeakReference

object MapAreaSearchScreenObj: BaseComposeScreenObject("MapAreaSearchScreen") {
    @OptIn(ExperimentalAnimationApi::class)
    override fun composable(
        builder: NavGraphBuilder,
        controller: NavHostController,
        act: BaseActivity
    ) {
        builder.composable(name) {
            MapAreaSearchScreen(controller, act as MapActivity)
        }
    }
}

class MapAreaSearchScreenViewModel(
    private val controller: NavHostController,
    act: MapActivity
): ViewModel() {
    class Factory(
        private val controller: NavHostController,
        private val act: MapActivity
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MapAreaSearchScreenViewModel(controller, act) as T
        }
    }

    private val weakAct = WeakReference(act)

    /** 输入维度 */
    val inputLatFlow = MutableStateFlow("")
    /** 输入经度 */
    val inputLngFlow = MutableStateFlow("")

    /** 点击返回 */
    fun onClickBack() {
        controller.navigateUp()
    }

    /** 更新纬度 */
    fun updateInputLat(value: String) {
        inputLatFlow.value = value
    }

    /** 更新经度 */
    fun updateInputLng(value: String) {
        inputLngFlow.value = value
    }

    /** 点击确定 */
    fun onClickSure() {
        weakAct.get()?.apply {
            val lat = inputLatFlow.value.toDoubleOrNull()
            if (lat == null || lat !in -90.0 .. 90.0) {
                app.showError("纬度数值不正确！")
                return
            }
            val lng = inputLngFlow.value.toDoubleOrNull()
            if (lng == null || lng !in -180.0 .. 180.0) {
                app.showError("经度数值不正确！")
                return
            }
            MapModel.instant.updateTarget(Point.fromLngLat(lng, lat))
            controller.navigateUp()
        }
    }
}