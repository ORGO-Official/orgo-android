package com.orgo.feature.login

sealed interface LoginUiState{
    object Loading : LoginUiState

    object Success : LoginUiState

    data class Failure(
        val message : String = ""
    ) : LoginUiState
}