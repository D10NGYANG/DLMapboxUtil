package com.d10ng.mapbox.activity.offline

import com.ramcosta.composedestinations.annotation.NavGraph

/**
 * 离线地图
 * @Author d10ng
 * @Date 2023/9/16 13:36
 */
@NavGraph(route = "Offline")
annotation class OfflineNavGraph(
    val start: Boolean = false
)