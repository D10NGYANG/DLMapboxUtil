package com.d10ng.mapbox.activity.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.BaseComposeScreenObject
import com.d10ng.mapbox.view.LocationSureDialogBuilder
import com.d10ng.tianditu.bean.LocationSearch
import com.google.accompanist.navigation.animation.composable
import com.mapbox.geojson.Point
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

object LocationSearchInfoScreenObj: BaseComposeScreenObject("LocationSearchInfoScreen") {
    @OptIn(ExperimentalAnimationApi::class)
    override fun composable(
        builder: NavGraphBuilder,
        controller: NavHostController,
        act: BaseActivity
    ) {
        builder.composable("$name/{search}/{area}/{areaCode}") { scope ->
            val search = scope.arguments?.getString("search")?: ""
            val area = scope.arguments?.getString("area")?: ""
            val areaCode = scope.arguments?.getString("areaCode")?.toIntOrNull()?: 0
            LocationSearchInfoScreen(controller, act, search, area, areaCode)
        }
    }

    fun go(controller: NavHostController, search: String, area: String, areaCode: Int) {
        controller.navigate("$name/$search/$area/$areaCode")
    }
}

class LocationSearchInfoScreenViewModel(
    private val controller: NavHostController,
    act: BaseActivity,
    private val search: String,
    private val area: String,
    private val areaCode: Int
): ViewModel() {
    class Factory(
        private val controller: NavHostController,
        private val act: BaseActivity,
        private val search: String,
        private val area: String,
        private val areaCode: Int
    ): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LocationSearchInfoScreenViewModel(controller, act, search, area, areaCode) as T
        }
    }

    private val weakAct = WeakReference(act)

    /** 结果 */
    val resultFlow = MutableStateFlow<LocationSearch?>(null)

    init {
        viewModelScope.launch {
            val result = LocationSearchManager.instant.search(act, search, areaCode.toString())
            resultFlow.emit(result)
        }
    }

    /** 点击返回 */
    fun onClickBack() {
        controller.navigateUp()
    }

    /** 点击区域 */
    fun onClickItem(value: LocationSearch.Area) {
        LocationSearchInfoScreenObj.go(controller, search, value.name, value.adminCode)
    }

    /** 点击区域 */
    fun onClickItem(value: LocationSearch.Statistics.AllAdmin) {
        LocationSearchInfoScreenObj.go(controller, search, value.adminName, value.adminCode)
    }

    /** 点击搜索结果 */
    fun onClickItem(value: LocationSearch.Poi) {
        weakAct.get()?.apply {
            val ls = value.lonlat.split(",")
            val lng = ls[0].toDoubleOrNull() ?: 0.0
            val lat = ls[1].toDoubleOrNull() ?: 0.0
            val target = Point.fromLngLat(lng, lat)
            app.showDialog(LocationSureDialogBuilder(
                title = "位置确定",
                message = value.address,
                target = target,
                onClickSure = {
                    app.hideDialog()
                    LocationSearchManager.instant.finish(this, target)
                },
                onClickCancel = {
                    app.hideDialog()
                }
            ))
        }
    }
}