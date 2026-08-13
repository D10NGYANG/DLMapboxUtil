package com.d10ng.mapbox.activity

import android.os.Bundle
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.d10ng.app.view.lockScreenOrientation
import com.d10ng.mapbox.stores.LocationStore
import com.d10ng.mapbox.stores.MapViewStore
import kotlinx.coroutines.launch

/**
 * 基础Activity
 * @Author d10ng
 * @Date 2023/9/15 17:54
 */
open class BaseMapboxActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT
            )
        )
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        // 锁定屏幕方向
        lockScreenOrientation()
        // 开启定位
        lifecycleScope.launch {
            LocationStore.startRequest()
            MapViewStore.initializeTargetFromLocation()
        }
    }

    override fun onDestroy() {
        // 停止定位
        LocationStore.stopRequest()
        super.onDestroy()
    }
}
