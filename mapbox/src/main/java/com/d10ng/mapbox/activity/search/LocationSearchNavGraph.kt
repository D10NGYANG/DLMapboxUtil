package com.d10ng.mapbox.activity.search

import com.ramcosta.composedestinations.annotation.NavGraph

@NavGraph(route = "LocationSearch")
annotation class LocationSearchNavGraph(
    val start: Boolean = false
)