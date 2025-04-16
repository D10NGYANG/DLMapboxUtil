package com.d10ng.mapbox.stores

import com.d10ng.datastore.annotation.PreferenceDataStore
import com.d10ng.datastore.annotation.PreferenceKey
import com.d10ng.mapbox.constant.MapLayerType

/**
 * 配置数据存储(使用DataStore)
 * @Author d10ng
 * @Date 2023/9/15 18:05
 */
@PreferenceDataStore("mapbox_config.preferences_pb")
interface MapboxConfigData {

    // 选择地图图层
    @PreferenceKey
    val layer: MapLayerType

    // 选择位置历史记录
    @PreferenceKey
    val history: Set<String>

    // 导航目标点
    @PreferenceKey
    val navigationTarget: String
}