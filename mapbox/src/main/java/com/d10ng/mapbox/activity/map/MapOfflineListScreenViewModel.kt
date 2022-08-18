package com.d10ng.mapbox.activity.map

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.BaseComposeScreenObject
import com.d10ng.mapbox.bean.OfflineMapInfo
import com.d10ng.mapbox.model.MapboxModel
import com.google.accompanist.navigation.animation.composable
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

object MapOfflineListScreenObj: BaseComposeScreenObject("MapOfflineListScreen") {
    @OptIn(ExperimentalAnimationApi::class)
    override fun composable(
        builder: NavGraphBuilder,
        controller: NavHostController,
        act: BaseActivity
    ) {
        builder.composable(name) {
            MapOfflineListScreen(controller, act as MapActivity)
        }
    }
}

class MapOfflineListScreenViewModel(
    private val controller: NavHostController,
    act: MapActivity
): ViewModel() {
    class Factory(
        private val controller: NavHostController,
        private val act: MapActivity
    ): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MapOfflineListScreenViewModel(controller, act) as T
        }
    }

    private val weakAct = WeakReference(act)

    /** 离线地图信息 */
    val offlineMapInfoListFlow = MapboxModel.instant.offlineMapInfoListFlow
    /** 离线地图快照 */
    val offlineMapSnapshotFlow = MapboxModel.instant.offlineMapSnapshotFlow

    init {
        // 刷新离线地图列表
        MapboxModel.instant.refreshMapOfflineList()
        viewModelScope.launch {
            offlineMapInfoListFlow.collect {
                // 刷新快照
                weakAct.get()?.apply {
                    MapboxModel.instant.updateSnapshot(this.applicationContext)
                }
            }
        }
    }

    /** 点击返回 */
    fun onClickBack() {
        controller.navigateUp()
    }

    /** 点击添加 */
    fun onClickAdd() {
        MapOfflineAreaScreenObj.go(controller)
    }

    /** 点击其中一项 */
    fun onClickItem(value: OfflineMapInfo) {
        MapOfflineEditScreenObj.go(controller, value.region.id)
    }
}