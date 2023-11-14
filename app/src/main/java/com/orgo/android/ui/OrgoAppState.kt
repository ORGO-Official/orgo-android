package com.orgo.android.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.orgo.android.navigation.TopLevelDestination
import com.orgo.android.navigation.homeNavigationRoute
import com.orgo.android.navigation.navigateToHome
import com.orgo.badge.badgeNavigationRoute
import com.orgo.badge.navigateToBadge
import com.orgo.core.model.data.UserAuthState
import com.orgo.feature.map.mapNavigationRoute
import com.orgo.feature.map.navigateToMap
import com.orgo.featuremypage.mypageNavigationRoute
import com.orgo.featuremypage.navigateToMypage
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@Composable
fun rememberOrgoAppState (
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
//    homeNavController : NavHostController = rememberNavController(),
): OrgoAppState {
    return remember(navController,coroutineScope) {
        OrgoAppState(navController,coroutineScope)
    }
}

@Composable
fun rememberHomeNavigationState(
    homeNavController : NavHostController = rememberNavController(),
) :HomeNavigationState{
    return remember(homeNavController){
        HomeNavigationState(homeNavController)
    }
}

class HomeNavigationState(
    var homeNavController: NavHostController,
    val startDestination : String = mapNavigationRoute
){

    fun setNavController(_navController: NavHostController) {
        homeNavController = _navController
    }
    var getNavController: NavHostController = homeNavController
        get() {
            return homeNavController
        }
        private set

    var getStartDestination : String = startDestination
        private set

    fun setStartDestination(route:String){
        getStartDestination = route
    }
    val currentDestination: NavDestination?
        @Composable get() = homeNavController
            .currentBackStackEntryAsState().value?.destination

    val topLevelDestinations : List<TopLevelDestination> = TopLevelDestination.values().asList()

    val currentTopLevelDestination : TopLevelDestination?
        @Composable get() = when(currentDestination?.route){
            homeNavigationRoute -> TopLevelDestination.MAP
            mapNavigationRoute -> TopLevelDestination.MAP
            mypageNavigationRoute -> TopLevelDestination.MYPAGE
            else -> null
        }

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination){

        val topLevelNavOptions = navOptions{
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(homeNavController.graph.findStartDestination().id){
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }

        when (topLevelDestination){
            TopLevelDestination.MAP -> homeNavController.navigateToMap(topLevelNavOptions)
            TopLevelDestination.BADGE -> homeNavController.navigateToBadge(topLevelNavOptions)
            TopLevelDestination.MYPAGE -> homeNavController.navigateToMypage(topLevelNavOptions)
        }
    }
}

@Stable
class OrgoAppState @Inject constructor(
    val navController: NavHostController,
//    val homeNavController: NavHostController,
    val coroutineScope: CoroutineScope,
){
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination
//
//    val currentTopLevelDestination : TopLevelDestination?
//        @Composable get() = when(currentDestination?.route){
//            homeNavigationRoute -> TopLevelDestination.MAP
//            mapNavigationRoute -> TopLevelDestination.MAP
//            mypageNavigationRoute -> TopLevelDestination.MYPAGE
//            else -> null
//        }
//
//    val shouldShowBottomBar : Boolean
//        @Composable get() = currentTopLevelDestination != null
//
//    val topLevelDestinations : List<TopLevelDestination> = TopLevelDestination.values().asList()
//
//    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination){
//        val topLevelNavOptions = navOptions{
//            // Pop up to the start destination of the graph to
//            // avoid building up a large stack of destinations
//            // on the back stack as users select items
//            popUpTo(homeNavController.graph.findStartDestination().id){
//                saveState = true
//            }
//            // Avoid multiple copies of the same destination when
//            // reselecting the same item
//            launchSingleTop = true
//            // Restore state when reselecting a previously selected item
//            restoreState = true
//        }
//
//        when (topLevelDestination){
//            TopLevelDestination.MAP -> homeNavController.navigateToMap(topLevelNavOptions)
//            TopLevelDestination.MYPAGE -> homeNavController.navigateToMypage(topLevelNavOptions)
//        }
//    }
}