package com.orgo.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orgo.core.data.repository.UserAuthRepository
import com.orgo.core.domain.usecase.auth.LogoutUseCase
import com.orgo.core.domain.usecase.auth.WithdrawUseCase
import com.orgo.core.model.data.UserAuthState
import com.orgo.core.model.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val logoutUseCase : LogoutUseCase,
    private val withdrawUseCase: WithdrawUseCase,
    private val userAuthRepository: UserAuthRepository
): ViewModel(){

    private val _uiState = MutableStateFlow<SettingsUiState>(SettingsUiState.Loading)
    val uiState : StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        setDefaultUiState()
    }

    fun setDefaultUiState() {
        when(userAuthRepository.userAuthState.value){
            is UserAuthState.Unauthenticated -> _uiState.update { SettingsUiState.Unauthenticated }
            else -> _uiState.update { SettingsUiState.Default }
        }
    }

    fun logout() = viewModelScope.launch{
        logoutUseCase().collect(){ resource ->
            when(resource){
                is Resource.Loading -> _uiState.update { SettingsUiState.Loading }
                is Resource.Success -> Unit // authRespository 단에서 로그아웃 처리함
                is Resource.Failure -> {
                    _uiState.update { SettingsUiState.Failure(
                        message = "로그아웃에 실패하였습니다."
                    ) }
                    Timber.e(resource.printError())
                }
            }
        }
    }

    fun withdraw() = viewModelScope.launch {
        withdrawUseCase().collect(){ resource ->
            when(resource){
                is Resource.Loading -> _uiState.update { SettingsUiState.Loading }
                is Resource.Success -> Unit
                is Resource.Failure -> {
                    _uiState.update { SettingsUiState.Failure(
                        message = "회원탈퇴에 실패하였습니다."
                    ) }
                    Timber.e(resource.printError())
                }
            }
        }
    }

    fun updateUiState (settingsUiState: SettingsUiState){
        _uiState.update { settingsUiState }
    }

}

