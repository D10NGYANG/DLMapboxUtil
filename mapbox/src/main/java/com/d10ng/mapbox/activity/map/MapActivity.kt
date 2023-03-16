package com.d10ng.mapbox.activity.map

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.d10ng.compose.BaseActivity
import com.d10ng.compose.ui.AppTheme
import com.d10ng.compose.view.AnimatedNavHostDefault
import com.d10ng.mapbox.model.LocationModel
import com.d10ng.mapbox.model.MapModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class MapActivity : BaseActivity() {

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(app = app) {
                val controller = rememberAnimatedNavController()
                Navigation(
                    modifier = Modifier
                        .fillMaxSize(),
                    controller = controller
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
        super.onDestroy()
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun Navigation(
        modifier: Modifier,
        controller: NavHostController
    ) {
        AnimatedNavHostDefault(
            controller,
            MapMainScreenObj.name,
            modifier = modifier
        ) {
            MapMainScreenObj.composable(this, controller, this@MapActivity)
            MapOfflineListScreenObj.composable(this, controller, this@MapActivity)
            MapOfflineAreaScreenObj.composable(this, controller, this@MapActivity)
            MapOfflineAddScreenObj.composable(this, controller, this@MapActivity)
            MapOfflineEditScreenObj.composable(this, controller, this@MapActivity)
        }
    }
}