package com.d10ng.mapbox.activity.show

import android.os.Bundle
import androidx.activity.compose.setContent
import com.d10ng.applib.app.getClearTopIntent
import com.d10ng.compose.BaseActivity
import com.d10ng.compose.ui.AppTheme
import com.d10ng.mapbox.model.LocationModel
import com.d10ng.mapbox.model.MapModel
import com.mapbox.geojson.Point

class LocationShowActivity : BaseActivity() {

    companion object {
        const val PARAM_LAT = "lat"
        const val PARAM_LNG = "lng"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lat = intent.getDoubleExtra(PARAM_LAT, 0.0)
        val lng = intent.getDoubleExtra(PARAM_LNG, 0.0)

        setContent {
            AppTheme(app = app) {
                LocationShowScreen(this, Point.fromLngLat(lng, lat))
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