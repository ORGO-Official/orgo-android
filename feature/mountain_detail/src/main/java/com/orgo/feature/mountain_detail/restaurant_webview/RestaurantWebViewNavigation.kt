package com.orgo.feature.mountain_detail.restaurant_webview

import android.net.Uri
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val restaurantWebViewRoute = "restaurant_webview"

internal const val restaurantUrlArg = "restaurantUrl"

fun NavController.navigateToRestaurantWebView(
    url : String,
    navOptions: NavOptions? = null
){
    val restaurantUrl = Uri.encode(url)
    this.navigate("$restaurantWebViewRoute/$restaurantUrl",navOptions)
}

@ExperimentalMaterial3Api
fun NavGraphBuilder.restaurantWebView(
    onBackClicked : () -> Unit
){
    composable(
    route = "$restaurantWebViewRoute/{$restaurantUrlArg}",
    arguments = listOf(
        navArgument(restaurantUrlArg){ type = NavType.StringType }
    )
    ){
        // viewModel이 따로 없기 때문에 다음과 같이 직접 argument를 받아서 넘겨준다
        val url = Uri.decode(checkNotNull(it.arguments?.getString(restaurantUrlArg)))
        RestaurantWebView(
            url = url,
            onBackClicked = onBackClicked
        )
    }
}

