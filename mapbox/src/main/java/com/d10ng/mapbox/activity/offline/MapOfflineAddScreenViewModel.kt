package com.d10ng.mapbox.activity.offline

import androidx.lifecycle.ViewModel
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.mapbox.activity.destinations.MapOfflineListScreenDestination
import com.d10ng.mapbox.stores.MapboxStore
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.roundToInt

class MapOfflineAddScreenViewModel : ViewModel() {

    /** 输入地图名称 */
    val inputNameFlow = MutableStateFlow("")

    /** 地图层级 */
    val zoomRangeFlow = MutableStateFlow(1f..3f)

    /** 更新输入地图名称 */
    fun updateInputName(value: String) {
        inputNameFlow.value = value
    }

    /** 更新地图层级 */
    fun updateZoomRange(value: ClosedFloatingPointRange<Float>) {
        zoomRangeFlow.value = value
    }

    /** 点击确定 */
    fun onClickSure(nav: DestinationsNavigator) {
        if (inputNameFlow.value.isEmpty()) {
            UiViewModelManager.showErrorNotify("地图名称不能为空！")
            return
        }
        if (inputNameFlow.value.length > 20) {
            UiViewModelManager.showErrorNotify("地图名称不能超过20个字符！")
            return
        }
        MapboxStore.addOfflineDownload(
            minZoom = zoomRangeFlow.value.start.roundToInt(),
            maxZoom = zoomRangeFlow.value.endInclusive.roundToInt(),
            title = inputNameFlow.value
        )
        nav.popBackStack(MapOfflineListScreenDestination, false)
    }
}