package com.d10ng.mapbox.activity.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.dialog.builder.ConfirmDialogBuilder
import com.d10ng.mapbox.utils.toPoint
import com.d10ng.mapbox.view.LocationConfirmView
import com.d10ng.tianditu.bean.LocationSearch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LocationSearchInfoScreenViewModel(
    private val search: String,
    private val area: String,
    private val areaCode: Int
) : ViewModel() {

    /** 结果 */
    val resultFlow = MutableStateFlow<LocationSearch?>(null)

    init {
        viewModelScope.launch {
            UiViewModelManager.showLoading()
            val result = LocationSearchManager.search(search, areaCode.toString())
            resultFlow.emit(result)
            UiViewModelManager.hideLoading()
        }
    }

    fun getArea() = area


    /** 点击区域 */
    fun onClickItem(
        value: LocationSearch.Area,
        onNavigateInfo: (String, String, Int) -> Unit
    ) {
        onNavigateInfo(search, value.name, value.adminCode)
    }

    /** 点击区域 */
    fun onClickItem(
        value: LocationSearch.Statistics.AllAdmin,
        onNavigateInfo: (String, String, Int) -> Unit
    ) {
        onNavigateInfo(search, value.adminName, value.adminCode)
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
