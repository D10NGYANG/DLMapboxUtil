package com.d10ng.mapbox.view

import com.mapbox.geojson.Point
import com.mapbox.maps.EdgeInsets
import kotlin.math.abs

private const val CAMERA_ZOOM_EPSILON = 0.0001
private const val CAMERA_CENTER_EPSILON = 0.000001

internal fun shouldUpdateCamera(
    currentZoom: Double,
    desiredZoom: Double,
    currentCenter: Point,
    desiredCenter: Point,
    currentPadding: EdgeInsets,
    desiredPadding: EdgeInsets
): Boolean {
    return abs(currentZoom - desiredZoom) > CAMERA_ZOOM_EPSILON ||
        abs(currentCenter.longitude() - desiredCenter.longitude()) > CAMERA_CENTER_EPSILON ||
        abs(currentCenter.latitude() - desiredCenter.latitude()) > CAMERA_CENTER_EPSILON ||
        currentPadding != desiredPadding
}
