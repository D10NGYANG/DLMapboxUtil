package com.d10ng.mapbox.activity.navigation

import com.ramcosta.composedestinations.annotation.NavGraph

/**
 * 地图导航
 * @Author d10ng
 * @Date 2023/9/16 17:07
 */
@NavGraph(route = "Navigation")
annotation class NavigationNavGraph(
    val start: Boolean = false
)