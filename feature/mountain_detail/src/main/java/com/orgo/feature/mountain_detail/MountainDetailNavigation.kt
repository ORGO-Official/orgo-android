package com.orgo.feature.mountain_detail

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.orgo.core.common.util.decoder.StringDecoder
import com.orgo.feature.mountain_detail.mountain_detail.MountainDetailRoute

const val mountainDetailNavigationRoute = "mountain_detail"

internal const val mountainIdArg = "mountainId"

internal class MountainArgs(val mountainId: Int) {
    constructor(savedStateHandle: SavedStateHandle, stringDecoder: StringDecoder) :
            this(stringDecoder.decodeString(checkNotNull(savedStateHandle[mountainIdArg])).toInt())
}

fun NavController.navigateToMountainDetail(
    mountainId: Int,
    navOptions: NavOptions? = null
){
    this.navigate("$mountainDetailNavigationRoute/$mountainId",navOptions)
}

@ExperimentalMaterial3Api
fun NavGraphBuilder.mountainDetailScreen(
    onBackClicked : () -> Unit,
    onRestaurantClicked : (String) -> Unit
){
    composable(route = "${mountainDetailNavigationRoute}/{$mountainIdArg}"){
        MountainDetailRoute(
            onBackClicked = onBackClicked,
            onRestaurantClicked = onRestaurantClicked
        )
    }

}