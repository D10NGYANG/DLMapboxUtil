package com.d10ng.mapbox.activity.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d10ng.mapbox.activity.destinations.MapOfflineAreaScreenDestination
import com.d10ng.mapbox.activity.destinations.MapOfflineEditScreenDestination
import com.d10ng.mapbox.bean.OfflineMapInfo
import com.d10ng.mapbox.model.MapboxModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

class MapOfflineListScreenViewModel : ViewModel() {

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
                MapActivity.instant.get()?.apply {
                    MapboxModel.instant.updateSnapshot(applicationContext)
                }
            }
        }
    }

    /** 点击添加 */
    fun onClickAdd(nav: DestinationsNavigator) {
        nav.navigate(MapOfflineAreaScreenDestination)
    }

    /** 点击其中一项 */
    fun onClickItem(nav: DestinationsNavigator, value: OfflineMapInfo) {
        nav.navigate(MapOfflineEditScreenDestination(value.region.id))
    }
}