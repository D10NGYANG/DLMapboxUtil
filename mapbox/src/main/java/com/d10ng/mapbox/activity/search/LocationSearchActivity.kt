package com.d10ng.mapbox.activity.search

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.compose.AppTheme
import com.d10ng.basicjetpackcomposeapp.view.AnimatedNavHostDefault
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class LocationSearchActivity : BaseActivity() {

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

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                LocationSearchManager.instant.finish(this@LocationSearchActivity, null)
            }
        })
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun Navigation(
        modifier: Modifier,
        controller: NavHostController
    ) {
        AnimatedNavHostDefault(
            controller,
            LocationSearchMainScreenObj.name,
            modifier = modifier
        ) {
            LocationSearchMainScreenObj.composable(this, controller, this@LocationSearchActivity)
            LocationSearchInfoScreenObj.composable(this, controller, this@LocationSearchActivity)
            LocationByLatLngScreenObj.composable(this, controller, this@LocationSearchActivity)
        }
    }
}