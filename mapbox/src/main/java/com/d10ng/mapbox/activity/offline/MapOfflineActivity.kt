package com.d10ng.mapbox.activity.offline

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

/**
 * 离线地图
 * @Author d10ng
 * @Date 2023/9/16 13:37
 */
class MapOfflineActivity : BaseMapboxActivity() {

    @OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val engine = rememberAnimatedNavHostEngine()
            val navController = engine.rememberNavController()

            DestinationsNavHost(
                engine = engine,
                navController = navController,
                navGraph = NavGraphs.Offline,
                modifier = Modifier.fillMaxSize()
            )
            UiViewModelManager.Init(act = this)
        }
    }
}