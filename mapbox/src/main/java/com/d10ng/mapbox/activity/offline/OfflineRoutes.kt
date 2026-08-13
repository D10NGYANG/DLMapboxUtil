package com.d10ng.mapbox.activity.offline

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object OfflineList : NavKey

@Serializable
data object OfflineArea : NavKey

@Serializable
data object OfflineAdd : NavKey

@Serializable
data class OfflineEdit(val id: String) : NavKey
