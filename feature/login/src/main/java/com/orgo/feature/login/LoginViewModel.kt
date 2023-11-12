package com.orgo.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.orgo.core.domain.usecase.auth.AutoLoginUseCase
import com.orgo.core.domain.usecase.auth.LoginUseCase
import com.orgo.core.model.data.SocialType
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
class LoginViewModel @Inject constructor(
    private val loginUseCase : LoginUseCase,
    private val autoLoginUseCase: AutoLoginUseCase,
):ViewModel(){

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
    val uiState : StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        autoLogin()
    }

    private fun autoLogin() = viewModelScope.launch {
        autoLoginUseCase().collect { resource ->
            when(resource){
                is Resource.Failure -> _uiState.update { LoginUiState.Success }
                is Resource.Success -> Unit
                is Resource.Loading -> Unit
            }
        }
    }

    fun naverLogin(token : String) = viewModelScope.launch{
        loginUseCase(SocialType.NAVER,token).collect(){ resource ->
           handleLoginUiState(SocialType.NAVER, resource)
        }
    }

    fun kakaoLogin(token : OAuthToken) = viewModelScope.launch{
        loginUseCase(SocialType.KAKAO,token.accessToken).collect(){resource ->
            handleLoginUiState(SocialType.KAKAO, resource)
        }
    }

    private fun handleLoginUiState(socialType: SocialType, resource : Resource<String>){
        when(resource){
            is Resource.Loading -> _uiState.update {
                Timber.d("uistate : Loading")
                LoginUiState.Loading
            }
            is Resource.Success -> {
                Timber.d("${socialType.value} login success")
            }
            is Resource.Failure -> {
                Timber.d("uistate : Failure")
                Timber.e(resource.printError())
            }
        }
    }
}