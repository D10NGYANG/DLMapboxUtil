package com.d10ng.mapbox.activity.map

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.mapbox.activity.BaseMapboxActivity
import com.d10ng.mapbox.activity.NavGraphs
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import java.lang.ref.WeakReference

class MapActivity : BaseMapboxActivity() {

    companion object {
        var instant: WeakReference<MapActivity?> = WeakReference(null)
    }

    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        instant = WeakReference(this)

        setContent {
            val engine = rememberAnimatedNavHostEngine()
            val navController = engine.rememberNavController()

            DestinationsNavHost(
                engine = engine,
                navController = navController,
                navGraph = NavGraphs.Map,
                modifier = Modifier.fillMaxSize()
            )
            UiViewModelManager.Init(act = this)
        }
    }

    override fun onDestroy() {

        instant = WeakReference(null)
        super.onDestroy()
    }
}