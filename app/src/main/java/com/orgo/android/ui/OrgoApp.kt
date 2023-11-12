package com.orgo.android.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.orgo.android.navigation.OrgoNavHost
import com.orgo.android.navigation.navigateToHome
import com.orgo.core.data.repository.UserAuthRepository
import com.orgo.core.data.repositoryImpl.UserAuthRepositoryImpl
import com.orgo.core.model.data.UserAuthState
import com.orgo.feature.login.navigateToLogin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrgoApp(
    appState: OrgoAppState = rememberOrgoAppState(),
    mainViewModel : MainViewModel = hiltViewModel()
) {
    val navController = appState.navController
    val userAuthState by mainViewModel.userAuthState.collectAsStateWithLifecycle()

    LaunchedEffect(userAuthState){
        when(userAuthState){
            is UserAuthState.Authenticated -> navController.navigateToHome()
            is UserAuthState.Unauthenticated -> Unit
            is UserAuthState.TokenExpired -> navController.navigateToLogin()
            is UserAuthState.LoggedOut -> navController.navigateToLogin()
        }
    }

    Row(
        Modifier.fillMaxSize()
    ) {
        OrgoNavHost(appState,appState.navController)
    }
}