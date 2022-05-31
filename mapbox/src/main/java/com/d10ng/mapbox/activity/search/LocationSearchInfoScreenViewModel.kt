package com.d10ng.mapbox.activity.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.BaseComposeScreenObject
import com.google.accompanist.navigation.animation.composable
import java.lang.ref.WeakReference

object LocationSearchInfoScreenObj: BaseComposeScreenObject("LocationSearchInfoScreen") {
    @OptIn(ExperimentalAnimationApi::class)
    override fun composable(
        builder: NavGraphBuilder,
        controller: NavHostController,
        act: BaseActivity
    ) {
        builder.composable("$name/{search}/{area}/{areaCode}") { scope ->
            val search = scope.arguments?.getString("search")?: ""
            val area = scope.arguments?.getString("area")?: ""
            val areaCode = scope.arguments?.getString("areaCode")?.toIntOrNull()?: 0
            LocationSearchInfoScreen(controller, act, search, area, areaCode)
        }
    }

    fun go(controller: NavHostController, search: String, area: String, areaCode: Int) {
        controller.navigate("$name/$search/$area/$areaCode")
    }
}

class LocationSearchInfoScreenViewModel(
    private val controller: NavHostController,
    act: BaseActivity,
    private val search: String,
    private val area: String,
    private val areaCode: Int
): ViewModel() {
    class Factory(
        private val controller: NavHostController,
        private val act: BaseActivity,
        private val search: String,
        private val area: String,
        private val areaCode: Int
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LocationSearchInfoScreenViewModel(controller, act, search, area, areaCode) as T
        }
    }

    private val weakAct = WeakReference(act)

    /** 点击返回 */
    fun onClickBack() {
        controller.navigateUp()
    }
}