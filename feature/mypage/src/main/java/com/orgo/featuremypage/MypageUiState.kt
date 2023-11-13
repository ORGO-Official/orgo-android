package com.orgo.featuremypage

import com.orgo.core.model.data.user.ClimbingRecord
import com.orgo.core.model.data.user.UserProfile

sealed interface MypageUiState{
    object Loading : MypageUiState

    data class Success(
        val userProfile: UserProfile,
        val userClimbingRecord: ClimbingRecord
    ) : MypageUiState

    object Unauthenticated : MypageUiState

    data class Failure(
        val message : String = ""
    ) : MypageUiState
}