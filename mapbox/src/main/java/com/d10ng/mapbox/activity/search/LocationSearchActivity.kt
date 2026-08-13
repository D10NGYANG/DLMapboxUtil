package com.d10ng.mapbox.activity.search

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.lifecycleScope
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.AppColor
import com.d10ng.mapbox.activity.BaseMapboxActivity
import kotlinx.coroutines.launch

class LocationSearchActivity : BaseMapboxActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = AppColor.toColorScheme()
            ) {
                val backStack = rememberNavBackStack(LocationSearchMain)
                val goBack: () -> Unit = {
                    if (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    } else {
                        lifecycleScope.launch { LocationSearchManager.finish(null) }
                    }
                }
                val navigateInfo: (String, String, Int) -> Unit = { search, area, code ->
                    backStack.add(LocationSearchInfo(search, area, code))
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
                        entry<LocationSearchMain> {
                            LocationSearchMainScreen(
                                onNavigateByLatLng = { backStack.add(LocationByLatLng) },
                                onNavigateInfo = navigateInfo
                            )
                        }
                        entry<LocationByLatLng> {
                            LocationByLatLngScreen(onBack = goBack)
                        }
                        entry<LocationSearchInfo> { key ->
                            val model = viewModel<LocationSearchInfoScreenViewModel>(
                                factory = viewModelFactory {
                                    initializer {
                                        LocationSearchInfoScreenViewModel(
                                            search = key.search,
                                            area = key.area,
                                            areaCode = key.areaCode
                                        )
                                    }
                                }
                            )
                            LocationSearchInfoScreen(
                                onBack = goBack,
                                onNavigateInfo = navigateInfo,
                                model = model
                            )
                        }
                    }
                )
                UiViewModelManager.Init()
            }
        }
    }
}
