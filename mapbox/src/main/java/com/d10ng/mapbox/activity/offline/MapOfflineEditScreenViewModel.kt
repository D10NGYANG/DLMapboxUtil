package com.d10ng.mapbox.activity.offline

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.dialog.builder.ConfirmDialogBuilder
import com.d10ng.mapbox.activity.navArgs
import com.d10ng.mapbox.stores.MapboxStore
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class MapOfflineEditScreenNavArg(
    val id: String
)

class MapOfflineEditScreenViewModel constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navArgs = savedStateHandle.navArgs<MapOfflineEditScreenNavArg>()
    private val _id = navArgs.id

    /** 离线地图信息 */
    private val infoFlow =
        MapboxStore.offlineMapInfoListFlow.map { it.find { item -> item.region.id == _id } }

    /** 输入名字 */
    val inputNameFlow = MutableStateFlow("")

    init {
        viewModelScope.launch {
            inputNameFlow.emit(infoFlow.first()?.title ?: "")
        }
    }

    /** 更新输入名字 */
    fun updateInputName(value: String) {
        inputNameFlow.value = value
    }

    /** 点击删除 */
    fun onClickDelete(nav: DestinationsNavigator) {
        UiViewModelManager.showDialog(ConfirmDialogBuilder(
            title = "注意",
            content = "确定删除当前离线地图吗？",
            type = ConfirmDialogBuilder.Type.Danger,
            onConfirmClick = {
                MapboxStore.deleteOffline(_id)
                withContext(Dispatchers.Main) { nav.navigateUp() }
                true
            }
        ))
    }

    /** 点击确定 */
    fun onClickSure(nav: DestinationsNavigator) {
        val name = inputNameFlow.value
        if (name.isEmpty()) {
            UiViewModelManager.showErrorNotify("地图名称不能为空！")
            return
        }
        if (name.length > 20) {
            UiViewModelManager.showErrorNotify("地图名称不能超过20个字符！")
            return
        }
        MapboxStore.renameOffline(_id, name)
        nav.navigateUp()
    }
}