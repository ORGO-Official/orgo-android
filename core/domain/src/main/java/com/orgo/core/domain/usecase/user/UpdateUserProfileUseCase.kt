package com.orgo.core.domain.usecase.user

import android.graphics.Bitmap
import com.orgo.core.data.repository.UserRepository
import com.orgo.core.model.data.user.ClimbingRecord
import com.orgo.core.model.data.user.UserProfile
import com.orgo.core.model.network.Resource
import com.orgo.core.model.network.profile.UpdateProfileRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

const val DUMMY_IMAGE = ""

class UpdateUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(nickname : String, profileImage : Bitmap): Flow<Resource<String>> {
        return userRepository.updateUserProfile(
            UpdateProfileRequest(
                nickname = nickname,
                profileImage = profileImage
            )
        )
    }
}