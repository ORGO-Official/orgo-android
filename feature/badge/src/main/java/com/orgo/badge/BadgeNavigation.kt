package com.orgo.badge

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable


const val badgeNavigationRoute = "badge"

fun NavController.navigateToBadge(navOptions: NavOptions? = null){
    this.navigate(badgeNavigationRoute,navOptions)
}

fun NavGraphBuilder.badgeScreen(
){
    composable(route = com.orgo.badge.badgeNavigationRoute){
        BadgeRoute()
    }
}