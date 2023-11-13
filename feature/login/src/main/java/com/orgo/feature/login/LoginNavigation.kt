package com.orgo.feature.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions

const val loginNavigationRoute = "login"

fun NavController.navigateToLogin(
    navOptions: NavOptions? = navOptions {
        popUpTo(loginNavigationRoute) { inclusive = true}
    }
){
    this.navigate(loginNavigationRoute,navOptions)
}

fun NavGraphBuilder.loginScreen(
    navigateToHome : () -> Unit,
){
    composable(route = loginNavigationRoute){
        LoginRoute(
            navigateToHome = navigateToHome,
        )
    }
}