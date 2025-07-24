package com.d10ng.mapbox.activity.navigation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.ui.Modifier
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.mapbox.NavGraphs
import com.d10ng.mapbox.activity.BaseMapboxActivity
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.rememberNavHostEngine

/**
 * 地图导航
 * @Author d10ng
 * @Date 2023/9/16 17:04
 */
class MapNavigationActivity : BaseMapboxActivity() {

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val engine = rememberNavHostEngine()
            val navController = engine.rememberNavController()

            DestinationsNavHost(
                engine = engine,
                navController = navController,
                navGraph = NavGraphs.Navigation,
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
            )
            UiViewModelManager.Init()
        }
    }
}