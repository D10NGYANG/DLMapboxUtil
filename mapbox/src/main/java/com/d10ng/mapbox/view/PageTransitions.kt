package com.d10ng.mapbox.view

import androidx.compose.animation.*
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.spec.DestinationStyle

@OptIn(ExperimentalAnimationApi::class)
object PageTransitions: DestinationStyle.Animated {

    override fun AnimatedContentScope<NavBackStackEntry>.enterTransition(): EnterTransition {
        return slideInHorizontally(initialOffsetX = {it})
    }

    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition {
        return slideOutHorizontally(targetOffsetX = {-it})
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popEnterTransition(): EnterTransition {
        return slideInHorizontally(initialOffsetX = {-it})
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popExitTransition(): ExitTransition {
        return slideOutHorizontally(targetOffsetX = {it})
    }
}