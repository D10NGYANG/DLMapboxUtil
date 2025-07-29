package com.d10ng.mapbox.activity.show

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.d10ng.app.utils.getClearTopIntent
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.AppColor
import com.d10ng.mapbox.activity.BaseMapboxActivity
import com.d10ng.mapbox.stores.MapViewStore
import com.mapbox.geojson.Point

class LocationShowActivity : BaseMapboxActivity() {

    companion object {
        const val PARAM_LAT = "lat"
        const val PARAM_LNG = "lng"
        var initPoint: Point = MapViewStore.targetFlow.value
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lat = intent.getDoubleExtra(PARAM_LAT, 0.0)
        val lng = intent.getDoubleExtra(PARAM_LNG, 0.0)
        if (lat != 0.0 && lng != 0.0) initPoint = Point.fromLngLat(lng, lat)

        setContent {
            MaterialTheme(
                colorScheme = AppColor.toColorScheme()
            ) {
                LocationShowScreen()
                UiViewModelManager.Init()
            }
        }
    }
}

/**
 * 跳转位置信息页面
 * @receiver BaseActivity
 * @param lat Double
 * @param lng Double
 */
fun Activity.goToLocationShowActivity(lat: Double, lng: Double) {
    val intent = getClearTopIntent(LocationShowActivity::class.java).apply {
        putExtra(LocationShowActivity.PARAM_LAT, lat)
        putExtra(LocationShowActivity.PARAM_LNG, lng)
    }
    startActivity(intent)
}