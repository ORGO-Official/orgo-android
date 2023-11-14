package com.orgo.feature.profile_edit

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orgo.core.common.util.downloadBitmapFromUrl
import com.orgo.core.domain.usecase.user.GetUserProfileUseCase
import com.orgo.core.domain.usecase.user.UpdateUserProfileUseCase
import com.orgo.core.model.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileEditUiState>(ProfileEditUiState.Loading)
    val uiState: StateFlow<ProfileEditUiState> = _uiState.asStateFlow()

    init {
        getUserProfile()
    }

    fun getUserProfile() = viewModelScope.launch {
        _uiState.update { ProfileEditUiState.Loading }
        getUserProfileUseCase().collect { resource ->
            _uiState.update {
                when (resource) {
                    is Resource.Loading -> ProfileEditUiState.Loading
                    is Resource.Success -> {
                        ProfileEditUiState.Edit(
                            userProfile = resource.data,
                            changedImage = downloadBitmapFromUrl(resource.data.profileImage)
                        )
                    }
                    is Resource.Failure -> ProfileEditUiState.Failure(resource.errorMessage)
                }
            }
        }
    }

    fun updateNickname(nickname: String) {
        val state = uiState.value
        if (state is ProfileEditUiState.Edit) {
            _uiState.update {
                state.copy(userProfile = state.userProfile.copy(nickname = nickname))
            }
        }
    }

    fun updateProfileImage(bitmap: Bitmap) {
        val state = uiState.value
        if (state is ProfileEditUiState.Edit) {
            _uiState.update {
                state.copy(changedImage = bitmap)
            }
        } else {
            _uiState.update { ProfileEditUiState.Failure("wrong state") }
        }
    }

    fun submit() = viewModelScope.launch {
        val state = uiState.value
        if (state is ProfileEditUiState.Edit) {
            _uiState.update { ProfileEditUiState.Loading}
            updateUserProfileUseCase(
                nickname = state.userProfile.nickname,
                profileImage = state.changedImage ?: throw IllegalArgumentException("profile Image Bitmap is null")
            )
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> Unit
                        is Resource.Success -> _uiState.update {
                            ProfileEditUiState.UpdateSuccess
                        }

                        is Resource.Failure -> _uiState.update {
                            ProfileEditUiState.Failure(message = resource.errorMessage)
                        }
                    }
                }
        } else {
            _uiState.update { ProfileEditUiState.Failure("wrong state") }
        }
    }
}

