package com.orgo.feature.profile_edit

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val profileEditNavigationRoute = "profile_edit"

fun NavController.navigateToProfileEdit(navOptions: NavOptions? = null){
    this.navigate(profileEditNavigationRoute,navOptions)
}

fun NavGraphBuilder.profileEditScreen(
    onCancelClicked : () -> Unit,
    onDoneClicked : () -> Unit,
){
    composable(route = profileEditNavigationRoute){
        ProfileEditRoute(
            onDoneClicked = onDoneClicked,
            onCancelClicked = onCancelClicked
        )
    }
}