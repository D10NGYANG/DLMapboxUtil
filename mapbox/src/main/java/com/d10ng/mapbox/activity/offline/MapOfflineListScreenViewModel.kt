package com.d10ng.mapbox.activity.offline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d10ng.app.managers.ActivityManager
import com.d10ng.mapbox.stores.MapboxStore
import kotlinx.coroutines.launch

class MapOfflineListScreenViewModel : ViewModel() {

    /** 离线地图信息 */
    val offlineMapInfoListFlow = MapboxStore.offlineMapInfoListFlow

    /** 离线地图快照 */
    val offlineMapSnapshotFlow = MapboxStore.offlineMapSnapshotFlow

    init {
        // 刷新离线地图列表
        MapboxStore.refreshMapOfflineList()
        viewModelScope.launch {
            offlineMapInfoListFlow.collect {
                // 刷新快照
                MapboxStore.updateSnapshot()
            }
        }
    }

    /** 点击返回 */
    fun onClickBack() {
        ActivityManager.finishTop()
    }

}
