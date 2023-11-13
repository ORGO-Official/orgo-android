package com.orgo.feature.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val settingsNavigationRoute = "settings"

fun NavController.navigateToSettings(navOptions: NavOptions? = null){
    this.navigate(settingsNavigationRoute,navOptions)
}

fun NavGraphBuilder.settingsScreen(
    onBackClicked : () -> Unit,
    navigateToLogin : () -> Unit,
){
    composable(route = settingsNavigationRoute){
        SettingsRoute(
            onBackClicked = onBackClicked,
            navigateToLogin = navigateToLogin
        )
    }
}