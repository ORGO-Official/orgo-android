package com.orgo.climb_complete

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val ClimbCompleteNavigationRoute = "climb_complete"

fun NavController.navigateToClimbComplete(navOptions: NavOptions? = null){
    this.navigate(ClimbCompleteNavigationRoute,navOptions)
}

fun NavGraphBuilder.climbCompleteScreen(
    onDoneClicked : () -> Unit,
){
    composable(route = ClimbCompleteNavigationRoute){
        ClimbCompleteRoute(
            onDoneClicked = onDoneClicked
        )
    }
}