package com.d10ng.mapbox.activity.show

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.d10ng.app.base.getClearTopIntent
import com.d10ng.app.view.lockScreenOrientation
import com.d10ng.app.view.setStatusBar
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.mapbox.model.LocationModel
import com.d10ng.mapbox.model.MapModel
import com.mapbox.geojson.Point
import java.lang.ref.WeakReference

class LocationShowActivity : ComponentActivity() {

    companion object {
        const val PARAM_LAT = "lat"
        const val PARAM_LNG = "lng"
        var instant = WeakReference<LocationShowActivity?>(null)
        var initPoint: Point = Point.fromLngLat(0.0, 0.0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 锁定屏幕方向
        lockScreenOrientation()
        // 设置状态栏颜色
        setStatusBar()

        instant = WeakReference(this)

        val lat = intent.getDoubleExtra(PARAM_LAT, 0.0)
        val lng = intent.getDoubleExtra(PARAM_LNG, 0.0)
        initPoint = Point.fromLngLat(lng, lat)

        LocationModel.instant.startRequestLocation(this)

        setContent {
            LocationShowScreen()
            UiViewModelManager.Init(act = this)
        }
    }

    override fun onStart() {
        super.onStart()
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
fun Activity.goToLocationShowActivity(lat: Double, lng: Double) {
    val intent = getClearTopIntent(LocationShowActivity::class.java).apply {
        putExtra(LocationShowActivity.PARAM_LAT, lat)
        putExtra(LocationShowActivity.PARAM_LNG, lng)
    }
    startActivity(intent)
}