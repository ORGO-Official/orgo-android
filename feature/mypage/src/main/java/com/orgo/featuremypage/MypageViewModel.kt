package com.orgo.featuremypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orgo.core.domain.usecase.user.GetUserClimbingRecordUseCase
import com.orgo.core.domain.usecase.user.GetUserProfileUseCase
import com.orgo.core.model.data.user.ClimbingRecord
import com.orgo.core.model.data.user.UserProfile
import com.orgo.core.model.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserClimbingRecordUseCase: GetUserClimbingRecordUseCase
) : ViewModel() {

    private val _mypageUiState = MutableStateFlow<MypageUiState>(MypageUiState.Loading)
    val mypageUiState : StateFlow<MypageUiState> = _mypageUiState.asStateFlow()

    init {
        fetchUserData()
    }

    fun fetchUserData()  = viewModelScope.launch {
        mypageUiState().collect { newState ->
            _mypageUiState.value = newState
        }
    }

    private fun mypageUiState(): Flow<MypageUiState> {
        val userProfile: Flow<Resource<UserProfile>> = getUserProfileUseCase()
        val userClimbingRecord: Flow<Resource<ClimbingRecord>> =
            getUserClimbingRecordUseCase()

        return combine(userProfile, userClimbingRecord) { profile, climbingRecord ->
            when {
                profile is Resource.Loading || climbingRecord is Resource.Loading -> {
                    Timber.tag("mypage").d("emit Uistate.Loading")
                    MypageUiState.Loading
                }

                profile is Resource.Failure -> {
                    Timber.tag("mypage").d("profile Resource.Failure ${profile.errorMessage}")
                    if (profile.code == 401) {
                        MypageUiState.Unauthenticated
                    } else {
                        MypageUiState.Failure(profile.errorMessage)
                    }
                }

                climbingRecord is Resource.Failure -> {
                    Timber.tag("mypage")
                        .d("profile Resource.Failure ${climbingRecord.errorMessage}")
                    if (climbingRecord.code == 401) {
                        MypageUiState.Unauthenticated
                    } else {
                        MypageUiState.Failure(climbingRecord.errorMessage)
                    }
                }

                else -> {
                    Timber.tag("mypage").d("emit Uistate.Success")
                    MypageUiState.Success(
                        userProfile = (profile as Resource.Success).data,
                        userClimbingRecord = (climbingRecord as Resource.Success).data
                    )
                }
            }
        }
    }
}