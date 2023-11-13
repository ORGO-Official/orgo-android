package com.orgo.feature.settings

sealed interface SettingsUiState{
    object Loading : SettingsUiState

    object ShowLogoutDialog : SettingsUiState

    object ShowWithdrawDialog : SettingsUiState

    object PrivacyPolicyWebView : SettingsUiState

    object TermsOfServiceWebView : SettingsUiState

    object Default : SettingsUiState

    object Unauthenticated : SettingsUiState

    data class Failure(
        val message : String = ""
    ) : SettingsUiState
}