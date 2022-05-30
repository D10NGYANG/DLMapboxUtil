package com.d10ng.mapbox.bean

import com.d10ng.mapbox.constant.MapLayerType
import com.mapbox.common.TileRegion
import com.mapbox.geojson.Geometry
import com.mapbox.geojson.Point

data class OfflineMapInfo(
    var region: TileRegion,
    var geometry: Geometry = Point.fromLngLat(0.0, 0.0),
    var style: MapLayerType = MapLayerType.MAPBOX_STREETS,
    var minZoom: Int = 0,
    var maxZoom: Int = 4,
    var title: String = ""
)
