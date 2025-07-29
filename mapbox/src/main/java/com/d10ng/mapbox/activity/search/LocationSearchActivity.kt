package com.d10ng.mapbox.activity.search

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.AppColor
import com.d10ng.mapbox.NavGraphs
import com.d10ng.mapbox.activity.BaseMapboxActivity
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.rememberNavHostEngine
import kotlinx.coroutines.launch

class LocationSearchActivity : BaseMapboxActivity() {

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = AppColor.toColorScheme()
            ) {
                val engine = rememberNavHostEngine()
                val navController = engine.rememberNavController()

                DestinationsNavHost(
                    engine = engine,
                    navController = navController,
                    navGraph = NavGraphs.LocationSearch,
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                )
                UiViewModelManager.Init()
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                lifecycleScope.launch { LocationSearchManager.finish(null) }
            }
        })
    }
}