package com.orgo.feature.map

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.orgo.feature.map.ui.mapview.MapRoute


const val mapNavigationRoute = "map"

fun NavController.navigateToMap(navOptions: NavOptions? = null){
    this.navigate(mapNavigationRoute,navOptions)
}

@ExperimentalMaterial3Api
fun NavGraphBuilder.mapScreen(
    onBackClicked : () -> Unit,
    onDetailClicked : (Int) -> Unit,
    navigateToComplete : () -> Unit,
    onSearchScreenShowed : (Boolean) -> Unit,
){
    composable(route = mapNavigationRoute){
        MapRoute(
            onBackClicked = onBackClicked,
            onDetailClicked = onDetailClicked,
            navigateToComplete = navigateToComplete,
            onSearchScreenShowed = onSearchScreenShowed
        )
    }
}