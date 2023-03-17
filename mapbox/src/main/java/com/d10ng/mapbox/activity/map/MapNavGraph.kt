package com.d10ng.mapbox.activity.map

import com.ramcosta.composedestinations.annotation.NavGraph

@NavGraph(route = "Map")
annotation class MapNavGraph(
    val start: Boolean = false
)