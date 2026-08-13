package com.d10ng.mapbox.activity.search

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object LocationSearchMain : NavKey

@Serializable
data object LocationByLatLng : NavKey

@Serializable
data class LocationSearchInfo(
    val search: String,
    val area: String,
    val areaCode: Int
) : NavKey
