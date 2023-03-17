package com.d10ng.mapbox.activity.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d10ng.mapbox.activity.destinations.LocationByLatLngScreenDestination
import com.d10ng.mapbox.activity.destinations.LocationSearchInfoScreenDestination
import com.d10ng.mapbox.view.LocationSureDialogBuilder
import com.d10ng.tianditu.bean.LocationSearch
import com.mapbox.geojson.Point
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class LocationSearchMainScreenViewModel : ViewModel() {

    /** 输入搜索内容 */
    val inputFlow = MutableStateFlow("")

    /** 结果 */
    val resultFlow = MutableStateFlow<LocationSearch?>(null)

    init {
        viewModelScope.launch {
            inputFlow.debounce(500).collect {
                search(it)
            }
        }
    }

    /** 点击返回 */
    fun onClickBack() {
        LocationSearchManager.instant.finish(null)
    }

    /** 更新搜索内容 */
    fun updateInput(value: String) {
        inputFlow.value = value
    }

    /** 点击搜索 */
    fun onClickSearch() {
        search(inputFlow.value)
    }

    /** 搜索 */
    private fun search(value: String) {
        viewModelScope.launch {
            LocationSearchActivity.instant.get()?.apply {
                val result = LocationSearchManager.instant.search(this, value)
                resultFlow.emit(result)
            }
        }
    }

    /** 点击通过经纬度查询 */
    fun onClickByLatLng(nav: DestinationsNavigator) {
        nav.navigate(LocationByLatLngScreenDestination)
    }

    /** 点击区域 */
    fun onClickItem(nav: DestinationsNavigator, value: LocationSearch.Area) {
        nav.navigate(LocationSearchInfoScreenDestination(inputFlow.value, value.name, value.adminCode))
    }

    /** 点击区域 */
    fun onClickItem(nav: DestinationsNavigator, value: LocationSearch.Statistics.AllAdmin) {
        nav.navigate(LocationSearchInfoScreenDestination(inputFlow.value, value.adminName, value.adminCode))
    }

    /** 点击搜索结果 */
    fun onClickItem(value: LocationSearch.Poi) {
        LocationSearchActivity.instant.get()?.apply {
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
                    LocationSearchManager.instant.finish(target)
                },
                onClickCancel = {
                    app.hideDialog()
                }
            ))
        }
    }
}