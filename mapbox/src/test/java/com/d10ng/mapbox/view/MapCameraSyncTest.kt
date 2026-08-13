package com.d10ng.mapbox.view

import com.mapbox.geojson.Point
import com.mapbox.maps.EdgeInsets
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MapCameraSyncTest {

    private val center = Point.fromLngLat(113.0, 23.0)
    private val padding = EdgeInsets(0.0, 0.0, 48.0, 0.0)

    @Test
    fun unchangedCameraDoesNotRestartAnUpdate() {
        assertFalse(shouldUpdateCamera(16.0, 16.0, center, center, padding, padding))
    }

    @Test
    fun zoomChangeUpdatesImmediately() {
        assertTrue(shouldUpdateCamera(16.0, 17.0, center, center, padding, padding))
    }

    @Test
    fun centerChangeUpdatesImmediately() {
        assertTrue(
            shouldUpdateCamera(
                16.0,
                16.0,
                center,
                Point.fromLngLat(114.0, 23.0),
                padding,
                padding
            )
        )
    }
}
