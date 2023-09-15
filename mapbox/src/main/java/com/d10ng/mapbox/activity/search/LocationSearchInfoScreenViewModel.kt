package com.d10ng.mapbox.activity.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d10ng.mapbox.activity.destinations.LocationSearchInfoScreenDestination
import com.d10ng.mapbox.activity.navArgs
import com.d10ng.tianditu.bean.LocationSearch
import com.mapbox.geojson.Point
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

data class LocationSearchInfoScreenNavArgs(
    val search: String,
    val area: String,
    val areaCode: Int
)

class LocationSearchInfoScreenViewModel constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navArgs = savedStateHandle.navArgs<LocationSearchInfoScreenNavArgs>()

    private val _search = navArgs.search
    private val _area = navArgs.area
    private val _areaCode = navArgs.areaCode

    /** 结果 */
    val resultFlow = MutableStateFlow<LocationSearch?>(null)

    init {
        viewModelScope.launch {
            val act = LocationSearchActivity.instant.get() ?: return@launch
            val result = LocationSearchManager.instant.search(act, _search, _areaCode.toString())
            resultFlow.emit(result)
        }
    }

    fun getArea() = _area


    /** 点击区域 */
    fun onClickItem(nav: DestinationsNavigator, value: LocationSearch.Area) {
        nav.navigate(LocationSearchInfoScreenDestination(_search, value.name, value.adminCode))
    }

    /** 点击区域 */
    fun onClickItem(nav: DestinationsNavigator, value: LocationSearch.Statistics.AllAdmin) {
        nav.navigate(LocationSearchInfoScreenDestination(_search, value.adminName, value.adminCode))
    }

    /** 点击搜索结果 */
    fun onClickItem(value: LocationSearch.Poi) {
        LocationSearchActivity.instant.get()?.apply {
            val ls = value.lonlat.split(",")
            val lng = ls[0].toDoubleOrNull() ?: 0.0
            val lat = ls[1].toDoubleOrNull() ?: 0.0
            val target = Point.fromLngLat(lng, lat)
            // TODO
//            app.showDialog(LocationSureDialogBuilder(
//                title = "位置确定",
//                message = value.address,
//                target = target,
//                onClickSure = {
//                    app.hideDialog()
//                    LocationSearchManager.instant.finish(target)
//                },
//                onClickCancel = {
//                    app.hideDialog()
//                }
//            ))
        }
    }
}