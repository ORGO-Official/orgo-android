package com.orgo.android.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.orgo.android.ui.HomeNavigationState
import com.orgo.android.ui.OrgoAppState
import com.orgo.android.ui.OrgoBottomBar
import com.orgo.android.ui.rememberHomeNavigationState
import com.orgo.badge.badgeScreen
import com.orgo.climb_complete.ClimbCompleteNavigationRoute
import com.orgo.climb_complete.climbCompleteScreen
import com.orgo.climb_complete.navigateToClimbComplete
import com.orgo.core.data.repository.UserAuthRepository
import com.orgo.feature.login.loginNavigationRoute
import com.orgo.feature.login.loginScreen
import com.orgo.feature.login.navigateToLogin
import com.orgo.feature.map.mapNavigationRoute
import com.orgo.feature.map.mapScreen
import com.orgo.feature.mountain_detail.mountainDetailScreen
import com.orgo.feature.mountain_detail.navigateToMountainDetail
import com.orgo.feature.mountain_detail.restaurant_webview.navigateToRestaurantWebView
import com.orgo.feature.mountain_detail.restaurant_webview.restaurantWebView
import com.orgo.feature.profile_edit.navigateToProfileEdit
import com.orgo.feature.profile_edit.profileEditScreen
import com.orgo.feature.settings.navigateToSettings
import com.orgo.feature.settings.settingsScreen
import com.orgo.featuremypage.mypageNavigationRoute
import com.orgo.featuremypage.mypageScreen

const val homeNavigationRoute = "home"

@ExperimentalMaterial3Api
@Composable
fun OrgoNavHost(
    appState: OrgoAppState,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = loginNavigationRoute
) {
    val context = LocalContext.current
    val homeNavigationState = rememberHomeNavigationState()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        // 각 feature 모듈에서 빌더로 Destination 설정
        loginScreen(
            navigateToHome = {
                navController.navigate(homeNavigationRoute)
            },
        )
        composable(route = homeNavigationRoute) {
            HomeScreens(appState = appState, homeNavigationState)
        }
        mountainDetailScreen(
            onBackClicked = { navController.popBackStack() },
            onRestaurantClicked = {
                navController.navigateToRestaurantWebView(it)
            }
        )
        restaurantWebView(
            onBackClicked = { navController.popBackStack() }
        )
        profileEditScreen(
            onDoneClicked = {
                homeNavigationState.setStartDestination(mypageNavigationRoute)
                navController.navigate(homeNavigationRoute)
            },
            onCancelClicked = { appState.navController.popBackStack() }
        )
        settingsScreen(
            onBackClicked = { navController.popBackStack() },
            navigateToLogin = { navController.navigateToLogin() }
        )
        climbCompleteScreen(
            onDoneClicked = {
                homeNavigationState.setStartDestination(mapNavigationRoute)
                navController.navigate(homeNavigationRoute)

            }
        )
    }
}

// bottomBar를 따로 처리해주기 위해서 NavHost를 하나 더 만들었음
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreens(
    appState: OrgoAppState,
    homeNavigationState: HomeNavigationState
) {
    var showBottomBar by rememberSaveable { mutableStateOf(true)}
    Scaffold(
        bottomBar = {
            if(showBottomBar){
                OrgoBottomBar(
                    destinations = homeNavigationState.topLevelDestinations,
                    onNavigateToDestination = homeNavigationState::navigateToTopLevelDestination,
                    currentDestination = homeNavigationState.currentDestination
                )
            }
        }
    ) { padding ->
        // 다른 페이지에서 homeNavHost쪽으로 넘어올 때 상태 초기화 되므로 NavController 새로 생성
        homeNavigationState.setNavController(rememberNavController())
        NavHost(
            navController = homeNavigationState.getNavController,
            startDestination = homeNavigationState.getStartDestination,
            modifier = Modifier.padding(padding),
            route = homeNavigationRoute
        ) {
            mapScreen(
                onBackClicked = { appState.navController.popBackStack() },
                onDetailClicked = { mountainId ->
                    appState.navController.navigateToMountainDetail(mountainId)
                },
                navigateToComplete = { appState.navController.navigateToClimbComplete() },
                onSearchScreenShowed = { isSearchScreen ->
                    showBottomBar = !isSearchScreen
                }
            )
            badgeScreen()
            mypageScreen(
                onProfileEditClicked = {
                    appState.navController.navigateToProfileEdit()
                },
                onSettingClicked = {
                    appState.navController.navigateToSettings()
                },
                navigateToLogin = {
                    appState.navController.navigateToLogin()
                }
            )
        }
    }
}

fun NavController.navigateToHome(
    navOptions: NavOptions? = null
){
    this.navigate(homeNavigationRoute,navOptions)
}

