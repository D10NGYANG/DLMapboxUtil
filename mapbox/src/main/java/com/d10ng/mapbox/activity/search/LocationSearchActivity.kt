package com.d10ng.mapbox.activity.search

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.d10ng.compose.BaseActivity
import com.d10ng.compose.ui.AppTheme
import com.d10ng.mapbox.activity.NavGraphs
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import java.lang.ref.WeakReference

class LocationSearchActivity : BaseActivity() {

    companion object {
        var instant: WeakReference<LocationSearchActivity?> = WeakReference(null)
    }

    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instant = WeakReference(this)
        setContent {
            AppTheme(app = app) {
                val engine = rememberAnimatedNavHostEngine()
                val navController = engine.rememberNavController()

                DestinationsNavHost(
                    engine = engine,
                    navController = navController,
                    navGraph = NavGraphs.LocationSearch,
                    modifier = Modifier.fillMaxSize()
                )
            }
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