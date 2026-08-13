package com.d10ng.mapbox.activity.offline

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.AppColor
import com.d10ng.mapbox.activity.BaseMapboxActivity

/**
 * 离线地图
 * @Author d10ng
 * @Date 2023/9/16 13:37
 */
class MapOfflineActivity : BaseMapboxActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colorScheme = AppColor.toColorScheme()
            ) {
                val backStack = rememberNavBackStack(OfflineList)
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
                        entry<OfflineList> {
                            MapOfflineListScreen(
                                onNavigateArea = { backStack.add(OfflineArea) },
                                onNavigateEdit = { backStack.add(OfflineEdit(it)) }
                            )
                        }
                        entry<OfflineArea> {
                            MapOfflineAreaScreen(
                                onBack = goBack,
                                onNavigateAdd = { backStack.add(OfflineAdd) }
                            )
                        }
                        entry<OfflineAdd> {
                            MapOfflineAddScreen(
                                onBack = goBack,
                                onComplete = {
                                    while (backStack.size > 1) backStack.removeLastOrNull()
                                }
                            )
                        }
                        entry<OfflineEdit> { key ->
                            val model = viewModel<MapOfflineEditScreenViewModel>(
                                factory = viewModelFactory {
                                    initializer { MapOfflineEditScreenViewModel(key.id) }
                                }
                            )
                            MapOfflineEditScreen(onBack = goBack, model = model)
                        }
                    }
                )
                UiViewModelManager.Init()
            }
        }
    }
}
