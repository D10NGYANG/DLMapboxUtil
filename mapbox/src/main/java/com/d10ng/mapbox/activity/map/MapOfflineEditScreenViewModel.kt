package com.d10ng.mapbox.activity.map

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.mapbox.activity.navArgs
import com.d10ng.mapbox.model.MapboxModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

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
        MapboxModel.instant.offlineMapInfoListFlow.map { it.find { item -> item.region.id == _id } }

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
        // TODO
//        MapActivity.instant.get()?.apply {
//            app.showDialog(BaseDialogBuilder(
//                title = "注意",
//                message = "确定删除当前离线地图吗？",
//                sureButton = "确定",
//                cancelButton = "取消",
//                onClickSure = {
//                    app.hideDialog()
//                    MapboxModel.instant.deleteOffline(_id)
//                    nav.navigateUp()
//                },
//                onClickCancel = {
//                    app.hideDialog()
//                }
//            ))
//        }
    }

    /** 点击确定 */
    fun onClickSure(nav: DestinationsNavigator) {
        MapActivity.instant.get()?.apply {
            val name = inputNameFlow.value
            if (name.isEmpty()) {
                UiViewModelManager.showErrorNotify("地图名称不能为空！")
                return
            }
            MapboxModel.instant.renameOffline(this, _id, name)
            nav.navigateUp()
        }
    }
}