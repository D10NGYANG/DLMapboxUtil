package com.d10ng.mapbox.activity.map

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.d10ng.compose.BaseActivity
import com.d10ng.compose.ui.AppTheme
import com.d10ng.mapbox.activity.NavGraphs
import com.d10ng.mapbox.model.LocationModel
import com.d10ng.mapbox.model.MapModel
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import java.lang.ref.WeakReference

class MapActivity : BaseActivity() {

    companion object {
        var instant: WeakReference<MapActivity?> = WeakReference(null)
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
                    navGraph = NavGraphs.Map,
                    modifier = Modifier.fillMaxSize()
                )
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