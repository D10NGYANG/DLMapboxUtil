package com.d10ng.mapbox.activity.map

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.BaseComposeScreenObject
import com.d10ng.basicjetpackcomposeapp.BaseViewModel
import com.d10ng.mapbox.bean.OfflineMapInfo
import com.d10ng.mapbox.model.MapboxModel
import com.google.accompanist.navigation.animation.composable
import kotlinx.coroutines.launch

object MapOfflineListScreenObj: BaseComposeScreenObject("MapOfflineListScreen") {
    @OptIn(ExperimentalAnimationApi::class)
    override fun composable(
        builder: NavGraphBuilder,
        controller: NavHostController,
        act: BaseActivity
    ) {
        builder.composable(name) {
            MapOfflineListScreen(controller, act)
        }
    }
}

class MapOfflineListScreenViewModel: BaseViewModel() {

    /** 离线地图信息 */
    val offlineMapInfoListFlow = MapboxModel.instant.offlineMapInfoListFlow
    /** 离线地图快照 */
    val offlineMapSnapshotFlow = MapboxModel.instant.offlineMapSnapshotFlow

    override fun init(act: BaseActivity, controller: NavHostController) {
        super.init(act, controller)
        // 刷新离线地图列表
        MapboxModel.instant.refreshMapOfflineList()
        viewModelScope.launch {
            offlineMapInfoListFlow.collect {
                // 刷新快照
                MapboxModel.instant.updateSnapshot(act.applicationContext)
            }
        }
    }

    /** 点击返回 */
    fun onClickBack() {
        controller?.navigateUp()
    }

    /** 点击添加 */
    fun onClickAdd() {
        controller?.let { MapOfflineAreaScreenObj.go(it) }
    }

    /** 点击其中一项 */
    fun onClickItem(value: OfflineMapInfo) {
        controller?.let { MapOfflineEditScreenObj.go(it, value.region.id) }
    }
}