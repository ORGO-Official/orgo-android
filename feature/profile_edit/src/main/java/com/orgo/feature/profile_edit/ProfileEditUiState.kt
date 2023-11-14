package com.orgo.feature.profile_edit

import android.graphics.Bitmap
import com.orgo.core.model.data.user.UserProfile


sealed interface ProfileEditUiState{
    object Loading : ProfileEditUiState

    data class Edit(
        val userProfile: UserProfile,
        val changedImage : Bitmap?,
    ): ProfileEditUiState

    object UpdateSuccess : ProfileEditUiState

    data class Failure(
        val message : String = ""
    ) : ProfileEditUiState
}