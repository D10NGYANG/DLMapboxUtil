package com.d10ng.mapbox.activity.navigation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.AppColor
import com.d10ng.mapbox.activity.BaseMapboxActivity

/**
 * 地图导航
 * @Author d10ng
 * @Date 2023/9/16 17:04
 */
class MapNavigationActivity : BaseMapboxActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colorScheme = AppColor.toColorScheme()
            ) {
                val backStack = rememberNavBackStack(NavigationHome)
                val navigateOn: () -> Unit = {
                    if (backStack.lastOrNull() != NavigationOn) backStack.add(NavigationOn)
                }
                val goBack: () -> Unit = {
                    if (backStack.size > 1) backStack.removeLastOrNull() else finish()
                }

                NavDisplay(
                    backStack = backStack,
                    onBack = goBack,
                    modifier = Modifier.fillMaxSize(),
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator()
                    ),
                    transitionSpec = {
                        slideInHorizontally(initialOffsetX = { it }) togetherWith
                            slideOutHorizontally(targetOffsetX = { -it })
                    },
                    popTransitionSpec = {
                        slideInHorizontally(initialOffsetX = { -it }) togetherWith
                            slideOutHorizontally(targetOffsetX = { it })
                    },
                    predictivePopTransitionSpec = {
                        slideInHorizontally(initialOffsetX = { -it }) togetherWith
                            slideOutHorizontally(targetOffsetX = { it })
                    },
                    entryProvider = entryProvider {
                        entry<NavigationHome> {
                            MapNavigationHomeScreen(onNavigateOn = navigateOn)
                        }
                        entry<NavigationOn> {
                            MapNavigationOnScreen(onNavigateBack = goBack)
                        }
                    }
                )
                UiViewModelManager.Init()
            }
        }
    }
}
