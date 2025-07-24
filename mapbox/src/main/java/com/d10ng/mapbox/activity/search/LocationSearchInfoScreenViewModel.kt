package com.d10ng.mapbox.activity.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.dialog.builder.ConfirmDialogBuilder
import com.d10ng.mapbox.destinations.LocationSearchInfoScreenDestination
import com.d10ng.mapbox.navArgs
import com.d10ng.mapbox.utils.toPoint
import com.d10ng.mapbox.view.LocationConfirmView
import com.d10ng.tianditu.bean.LocationSearch
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
            UiViewModelManager.showLoading()
            val result = LocationSearchManager.search(_search, _areaCode.toString())
            resultFlow.emit(result)
            UiViewModelManager.hideLoading()
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
        UiViewModelManager.showDialog(ConfirmDialogBuilder(
            title = "位置确定",
            content = "",
            contentSlot = {
                LocationConfirmView(
                    label = value.name,
                    description = value.address,
                    point = value.toPoint()
                )
            },
            onConfirmClick = {
                LocationSearchManager.finish(value)
                true
            }
        ))
    }
}