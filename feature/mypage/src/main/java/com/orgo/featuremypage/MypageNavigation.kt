package com.orgo.featuremypage

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val mypageNavigationRoute = "mypage"

fun NavController.navigateToMypage(navOptions: NavOptions? = null){
    this.navigate(mypageNavigationRoute,navOptions)
}

fun NavGraphBuilder.mypageScreen(
    onProfileEditClicked : () -> Unit,
    onSettingClicked : () -> Unit,
    navigateToLogin : () -> Unit
){
    composable(route = mypageNavigationRoute){
        MypageRoute(
            onProfileEditClicked = onProfileEditClicked,
            onSettingClicked = onSettingClicked,
            navigateToLogin = navigateToLogin
        )
    }
}