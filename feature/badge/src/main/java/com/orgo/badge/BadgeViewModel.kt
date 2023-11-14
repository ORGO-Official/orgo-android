package com.orgo.badge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orgo.core.domain.usecase.badge.GetBadgesUsecase
import com.orgo.core.domain.usecase.user.GetUserClimbingRecordUseCase
import com.orgo.core.domain.usecase.user.GetUserProfileUseCase
import com.orgo.core.model.data.badge.Badge
import com.orgo.core.model.data.mountain.Mountain
import com.orgo.core.model.data.user.ClimbingRecord
import com.orgo.core.model.data.user.UserProfile
import com.orgo.core.model.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BadgeViewModel @Inject constructor(
    private val getBadgesUseCase: GetBadgesUsecase,
) : ViewModel() {

    private val _badgeUiState = MutableStateFlow<BadgeUiState>(BadgeUiState.Loading)
    val badgeUiState : StateFlow<BadgeUiState> = _badgeUiState.asStateFlow()

    init {
        fetchBadges()
    }

    fun fetchBadges() = viewModelScope.launch {
        getBadgesUseCase().collect{ resource ->
            when(resource){
                is Resource.Loading -> Unit
                is Resource.Success -> {
                    _badgeUiState.update {
                        BadgeUiState.Success(resource.data)
                    }
                }
                is Resource.Failure ->{
                    _badgeUiState.update {
                        Timber.e("failure : ${resource.errorMessage}, code : ${resource.code}")
                        if(resource.code == 403){
                            BadgeUiState.Unauthenticated
                        }else BadgeUiState.Failure("뱃지 가져오기에 실패하였습니다")
                    }
                }
            }
        }
    }

    fun updateSelectedBadge(badge: Badge?){
        val state = badgeUiState.value
        if (state is BadgeUiState.Success) {
            _badgeUiState.update {
                state.copy(selectedBadge = badge)
            }
        }

    }



//    private fun mypageUiState(): Flow<BadgeUiState> {
//        val userProfile: Flow<Resource<UserProfile>> = getUserProfileUseCase()
//        val userClimbingRecord: Flow<Resource<ClimbingRecord>> =
//            getUserClimbingRecordUseCase()
//
//        return combine(userProfile, userClimbingRecord) { profile, climbingRecord ->
//            when {
//                profile is Resource.Loading || climbingRecord is Resource.Loading -> {
//                    Timber.tag("mypage").d("emit Uistate.Loading")
//                    BadgeUiState.Loading
//                }
//
//                profile is Resource.Failure -> {
//                    Timber.tag("mypage").d("profile Resource.Failure ${profile.errorMessage}")
//                    if (profile.code == 401) {
//                        BadgeUiState.Unauthenticated
//                    } else {
//                        BadgeUiState.Failure(profile.errorMessage)
//                    }
//                }
//
//                climbingRecord is Resource.Failure -> {
//                    Timber.tag("mypage")
//                        .d("profile Resource.Failure ${climbingRecord.errorMessage}")
//                    if (climbingRecord.code == 401) {
//                        BadgeUiState.Unauthenticated
//                    } else {
//                        BadgeUiState.Failure(climbingRecord.errorMessage)
//                    }
//                }
//
//                else -> {
//                    Timber.tag("mypage").d("emit Uistate.Success")
//                    BadgeUiState.Success(
//                        userProfile = (profile as Resource.Success).data,
//                        userClimbingRecord = (climbingRecord as Resource.Success).data
//                    )
//                }
//            }
//        }
//    }
}