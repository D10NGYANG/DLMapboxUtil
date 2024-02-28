package com.d10ng.mapbox.activity.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.dialog.builder.ConfirmDialogBuilder
import com.d10ng.mapbox.activity.destinations.LocationByLatLngScreenDestination
import com.d10ng.mapbox.activity.destinations.LocationSearchInfoScreenDestination
import com.d10ng.mapbox.bean.HistoryInfo
import com.d10ng.mapbox.stores.HistoryStore
import com.d10ng.mapbox.utils.toPoint
import com.d10ng.mapbox.view.LocationConfirmView
import com.d10ng.tianditu.bean.LocationSearch
import com.d10ng.tianditu.bean.PerimeterSearch
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

    /** 是否正在执行搜索 */
    val isSearchingFlow = MutableStateFlow(false)

    /** 历史记录 */
    val historyFlow = HistoryStore.valueFlow

    /** 结果 */
    val resultFlow = MutableStateFlow<Pair<PerimeterSearch?, LocationSearch?>>(null to null)

    init {
        viewModelScope.launch {
            inputFlow.debounce(500).collect {
                search(it)
            }
        }
    }

    /** 点击返回 */
    fun onClickBack() {
        viewModelScope.launch { LocationSearchManager.finish(null) }
    }

    /** 更新搜索内容 */
    fun updateInput(value: String) {
        inputFlow.value = value
    }

    /** 点击搜索 */
    fun onClickSearch() {
        search(inputFlow.value)
    }

    /**
     * 搜索
     * @param value String
     */
    private fun search(value: String) {
        viewModelScope.launch {
            isSearchingFlow.value = true
            val result = LocationSearchManager.searchWithPerimeter(value)
            if (inputFlow.value == value) resultFlow.emit(result)
            isSearchingFlow.value = false
        }
    }

    /** 点击通过经纬度查询 */
    fun onClickByLatLng(nav: DestinationsNavigator) {
        nav.navigate(LocationByLatLngScreenDestination)
    }

    /**
     * 点击搜索结果
     * @param value PerimeterSearch.Poi
     */
    fun onClickItem(value: PerimeterSearch.Poi) {
        showLocationConfirmView(value.name, value.address, value.toPoint())
    }

    /** 点击区域 */
    fun onClickItem(nav: DestinationsNavigator, value: LocationSearch.Area) {
        nav.navigateArea(value.name, value.adminCode)
    }

    /** 点击区域 */
    fun onClickItem(nav: DestinationsNavigator, value: LocationSearch.Statistics.AllAdmin) {
        nav.navigateArea(value.adminName, value.adminCode)
    }

    /**
     * 跳转区域查询
     * @receiver DestinationsNavigator
     * @param name String
     * @param code Int
     */
    private fun DestinationsNavigator.navigateArea(name: String, code: Int) {
        navigate(LocationSearchInfoScreenDestination(inputFlow.value, name, code))
    }

    /**
     * 点击搜索结果
     * @param value Poi
     */
    fun onClickItem(value: LocationSearch.Poi) {
        showLocationConfirmView(value.name, value.address, value.toPoint())
    }

    /**
     * 显示位置确定视图
     * @param name String
     * @param address String
     * @param point Point
     */
    private fun showLocationConfirmView(name: String, address: String, point: Point) {
        UiViewModelManager.showDialog(ConfirmDialogBuilder(
            title = "位置确定",
            content = "",
            contentSlot = {
                LocationConfirmView(
                    label = name,
                    description = address,
                    point = point
                )
            },
            onConfirmClick = {
                LocationSearchManager.finish(name, address, point)
                true
            }
        ))
    }

    /**
     * 点击清除历史记录
     */
    fun onClickClearHistory() {
        UiViewModelManager.showDialog(ConfirmDialogBuilder(
            title = "提示",
            content = "确定要清除历史记录吗？",
            onConfirmClick = {
                HistoryStore.clear()
                true
            }
        ))
    }

    /**
     * 点击删除历史记录
     * @param value HistoryInfo
     */
    fun onClickRemoveHistory(value: HistoryInfo) {
        viewModelScope.launch { HistoryStore.remove(value) }
    }

    /**
     * 点击历史记录
     * @param value HistoryInfo
     */
    fun onClickHistory(value: HistoryInfo) {
        viewModelScope.launch { LocationSearchManager.finish(value) }
    }
}