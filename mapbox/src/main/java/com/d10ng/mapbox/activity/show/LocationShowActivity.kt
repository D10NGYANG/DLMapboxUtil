package com.d10ng.mapbox.activity.show

import android.os.Bundle
import androidx.activity.compose.setContent
import com.d10ng.app.app.getClearTopIntent
import com.d10ng.compose.BaseActivity
import com.d10ng.compose.ui.AppTheme
import com.d10ng.mapbox.model.LocationModel
import com.d10ng.mapbox.model.MapModel
import com.mapbox.geojson.Point
import java.lang.ref.WeakReference

class LocationShowActivity : BaseActivity() {

    companion object {
        const val PARAM_LAT = "lat"
        const val PARAM_LNG = "lng"
        var instant = WeakReference<LocationShowActivity?>(null)
        var initPoint = Point.fromLngLat(0.0, 0.0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instant = WeakReference(this)
        val lat = intent.getDoubleExtra(PARAM_LAT, 0.0)
        val lng = intent.getDoubleExtra(PARAM_LNG, 0.0)
        initPoint = Point.fromLngLat(lng, lat)
        setContent {
            AppTheme(app = app) {
                LocationShowScreen()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        LocationModel.instant.startRequestLocation(this)
        MapModel.instant.initLayer(this)
    }

    override fun onDestroy() {
        LocationModel.instant.stopRequestLocation(this)
        instant = WeakReference(null)
        super.onDestroy()
    }
}

/**
 * 跳转位置信息页面
 * @receiver BaseActivity
 * @param lat Double
 * @param lng Double
 */
fun BaseActivity.goToLocationShowActivity(lat: Double, lng: Double) {
    val intent = getClearTopIntent(LocationShowActivity::class.java).apply {
        putExtra(LocationShowActivity.PARAM_LAT, lat)
        putExtra(LocationShowActivity.PARAM_LNG, lng)
    }
    startActivity(intent)
}