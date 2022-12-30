package com.d10ng.mapbox.activity.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.BaseComposeScreenObject
import com.d10ng.basicjetpackcomposeapp.BaseViewModel
import com.d10ng.mapbox.view.LocationSureDialogBuilder
import com.d10ng.tianditu.bean.LocationSearch
import com.google.accompanist.navigation.animation.composable
import com.mapbox.geojson.Point
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

object LocationSearchInfoScreenObj : BaseComposeScreenObject("LocationSearchInfoScreen") {

    const val SEARCH = "search"
    const val AREA = "area"
    const val AREA_CODE = "areaCode"

    @OptIn(ExperimentalAnimationApi::class)
    override fun composable(
        builder: NavGraphBuilder,
        controller: NavHostController,
        act: BaseActivity
    ) {
        builder.composable(
            route = "$name/{$SEARCH}/{$AREA}/{$AREA_CODE}",
            arguments = listOf(
                navArgument(SEARCH) { type = NavType.StringType },
                navArgument(AREA) { type = NavType.StringType },
                navArgument(AREA_CODE) { type = NavType.IntType }
            )
        ) {
            LocationSearchInfoScreen(controller, act)
        }
    }

    fun go(controller: NavHostController, search: String, area: String, areaCode: Int) {
        controller.navigate("$name/$search/$area/$areaCode")
    }
}

class LocationSearchInfoScreenViewModel(
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _search = savedStateHandle.get<String>(LocationSearchInfoScreenObj.SEARCH) ?: ""
    private val _area = savedStateHandle.get<String>(LocationSearchInfoScreenObj.AREA) ?: ""
    private val _areaCode = savedStateHandle.get<Int>(LocationSearchInfoScreenObj.AREA_CODE) ?: 0

    /** 结果 */
    val resultFlow = MutableStateFlow<LocationSearch?>(null)

    override fun init(act: BaseActivity, controller: NavHostController) {
        super.init(act, controller)
        viewModelScope.launch {
            val result = LocationSearchManager.instant.search(act, _search, _areaCode.toString())
            resultFlow.emit(result)
        }
    }

    fun getArea() = _area

    /** 点击返回 */
    fun onClickBack() {
        controller?.navigateUp()
    }

    /** 点击区域 */
    fun onClickItem(value: LocationSearch.Area) {
        controller?.let { LocationSearchInfoScreenObj.go(it, _search, value.name, value.adminCode) }
    }

    /** 点击区域 */
    fun onClickItem(value: LocationSearch.Statistics.AllAdmin) {
        controller?.let {
            LocationSearchInfoScreenObj.go(
                it,
                _search,
                value.adminName,
                value.adminCode
            )
        }
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