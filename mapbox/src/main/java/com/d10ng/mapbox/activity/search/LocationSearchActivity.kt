package com.d10ng.mapbox.activity.search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.d10ng.app.view.lockScreenOrientation
import com.d10ng.app.view.setStatusBar
import com.d10ng.mapbox.activity.NavGraphs
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import java.lang.ref.WeakReference

class LocationSearchActivity : ComponentActivity() {

    companion object {
        var instant: WeakReference<LocationSearchActivity?> = WeakReference(null)
    }

    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 锁定屏幕方向
        lockScreenOrientation()
        // 设置状态栏颜色
        setStatusBar()

        instant = WeakReference(this)
        setContent {
            val engine = rememberAnimatedNavHostEngine()
            val navController = engine.rememberNavController()

            DestinationsNavHost(
                engine = engine,
                navController = navController,
                navGraph = NavGraphs.LocationSearch,
                modifier = Modifier.fillMaxSize()
            )
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                LocationSearchManager.instant.finish(null)
            }
        })
    }

    override fun onDestroy() {
        instant = WeakReference(null)
        super.onDestroy()
    }
}