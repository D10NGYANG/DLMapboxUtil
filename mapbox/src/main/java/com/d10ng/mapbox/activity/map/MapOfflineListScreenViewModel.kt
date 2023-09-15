package com.d10ng.mapbox.activity.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d10ng.mapbox.activity.destinations.MapOfflineAreaScreenDestination
import com.d10ng.mapbox.activity.destinations.MapOfflineEditScreenDestination
import com.d10ng.mapbox.bean.OfflineMapInfo
import com.d10ng.mapbox.stores.OfflineMapStore
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

class MapOfflineListScreenViewModel : ViewModel() {

    /** 离线地图信息 */
    val offlineMapInfoListFlow = OfflineMapStore.offlineMapInfoListFlow

    /** 离线地图快照 */
    val offlineMapSnapshotFlow = OfflineMapStore.offlineMapSnapshotFlow

    init {
        // 刷新离线地图列表
        OfflineMapStore.refreshMapOfflineList()
        viewModelScope.launch {
            offlineMapInfoListFlow.collect {
                // 刷新快照
                OfflineMapStore.updateSnapshot()
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