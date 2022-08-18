package com.d10ng.mapbox.activity.map

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.BaseComposeScreenObject
import com.d10ng.basicjetpackcomposeapp.dialog.builder.BaseDialogBuilder
import com.d10ng.mapbox.model.MapboxModel
import com.google.accompanist.navigation.animation.composable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

object MapOfflineEditScreenObj: BaseComposeScreenObject("MapOfflineEditScreen") {
    @OptIn(ExperimentalAnimationApi::class)
    override fun composable(
        builder: NavGraphBuilder,
        controller: NavHostController,
        act: BaseActivity
    ) {
        builder.composable("$name/{id}") { scope ->
            val id = scope.arguments?.getString("id")?: ""
            MapOfflineEditScreen(controller, act as MapActivity, id)
        }
    }

    fun go(controller: NavHostController, id: String) {
        controller.navigate("$name/$id")
    }
}

class MapOfflineEditScreenViewModel(
    private val controller: NavHostController,
    act: MapActivity,
    private val id: String
): ViewModel() {
    class Factory(
        private val controller: NavHostController,
        private val act: MapActivity,
        private val id: String
    ): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MapOfflineEditScreenViewModel(controller, act, id) as T
        }
    }

    private val weakAct = WeakReference(act)

    /** 离线地图信息 */
    private val infoFlow = MapboxModel.instant.offlineMapInfoListFlow.map { it.find { item -> item.region.id == id } }
    /** 输入名字 */
    val inputNameFlow = MutableStateFlow("")

    init {
        viewModelScope.launch {
            inputNameFlow.emit(infoFlow.first()?.title?: "")
        }
    }

    /** 点击返回 */
    fun onClickBack() {
        controller.navigateUp()
    }

    /** 更新输入名字 */
    fun updateInputName(value: String) {
        inputNameFlow.value = value
    }

    /** 点击删除 */
    fun onClickDelete() {
        weakAct.get()?.apply {
            app.showDialog(BaseDialogBuilder(
                title = "注意",
                message = "确定删除当前离线地图吗？",
                sureButton = "确定",
                cancelButton = "取消",
                onClickSure = {
                    app.hideDialog()
                    MapboxModel.instant.deleteOffline(id)
                    controller.navigateUp()
                },
                onClickCancel = {
                    app.hideDialog()
                }
            ))
        }
    }

    /** 点击确定 */
    fun onClickSure() {
        weakAct.get()?.apply {
            val name = inputNameFlow.value
            if (name.isEmpty()) {
                app.showError("地图名称不能为空！")
                return
            }
            MapboxModel.instant.renameOffline(this, id, name)
            controller.navigateUp()
        }
    }
}