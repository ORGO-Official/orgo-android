package com.orgo.core.network.datasource

import com.orgo.core.common.util.createMultipartBody
import com.orgo.core.model.data.mountain.CompleteClimbingRequest
import com.orgo.core.model.data.user.ClimbingRecord
import com.orgo.core.model.data.user.UserProfile
import com.orgo.core.model.network.ApiResult
import com.orgo.core.model.network.Resource
import com.orgo.core.model.network.profile.UpdateNicknameRequest
import com.orgo.core.model.network.profile.UpdateProfileRequest
import com.orgo.core.network.service.UserService
import com.orgo.core.network.util.apiResultToResourceFlow
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userService: UserService
) {
    suspend fun getUserProfile(userAccessToken: String)
            : ApiResult<UserProfile> {
        Timber.tag("mypage").d("getUserProfile")
        return userService.getUserProfile()
    }

    suspend fun getUserClimbingRecords(userAccessToken: String)
            : ApiResult<ClimbingRecord> {
        Timber.tag("mypage").d("getUserClimbingRecords")
        return userService.getUserClimbingRecords()
    }

    suspend fun updateUserProfile(
        userAccessToken: String,
        updateProfileRequest: UpdateProfileRequest
    ): ApiResult<String> {
        return userService.updateUserProfile(
            nickname = UpdateNicknameRequest(updateProfileRequest.nickname),
            imageFile = createMultipartBody(
                bitmap = updateProfileRequest.profileImage,
                fileName = "text-image.jpg"
            )
        )
    }

    suspend fun completeClimbingRequest(
        userAccessToken: String,
        completeClimbingRequest: CompleteClimbingRequest
    ): ApiResult<String> = userService.completeClimbing(
//        userAccessToken = userAccessToken,
        completeClimbingRequest = completeClimbingRequest
    )
}