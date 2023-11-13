package com.orgo.core.data.repository

import com.orgo.core.model.data.mountain.CompleteClimbingRequest
import com.orgo.core.model.data.user.ClimbingRecord
import com.orgo.core.model.data.user.UserProfile
import com.orgo.core.model.network.Resource
import com.orgo.core.model.network.profile.UpdateProfileRequest
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUserProfile() : Flow<Resource<UserProfile>>

    fun getUserClimbingRecords() : Flow<Resource<ClimbingRecord>>

    fun updateUserProfile(updateProfileRequest : UpdateProfileRequest) : Flow<Resource<String>>

    fun completeClimbing(completeClimbingRequest: CompleteClimbingRequest) : Flow<Resource<String>>

}