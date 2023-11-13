package com.orgo.core.data.repositoryImpl

import com.orgo.core.data.repository.UserAuthRepository
import com.orgo.core.data.repository.UserRepository
import com.orgo.core.datastore.UserTokenDataSource
import com.orgo.core.model.data.mountain.CompleteClimbingRequest
import com.orgo.core.model.data.user.ClimbingRecord
import com.orgo.core.model.data.user.UserProfile
import com.orgo.core.model.network.Resource
import com.orgo.core.model.network.profile.UpdateProfileRequest
import com.orgo.core.network.datasource.UserRemoteDataSource
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val userTokenDataSource: UserTokenDataSource,
    private val userAuthRepository: UserAuthRepository
) : UserRepository {


    override fun getUserProfile(): Flow<Resource<UserProfile>> {
        Timber.d("UserRepositoryImpl getUserProfile")
        return userAuthRepository.userAuthActionFlow { userAccessToken ->
            Timber.d("userAccessToken : $userAccessToken")
            remoteDataSource.getUserProfile(userAccessToken)
        }
    }

    override fun getUserClimbingRecords(): Flow<Resource<ClimbingRecord>> {
        return userAuthRepository.userAuthActionFlow { userAccessToken ->
            remoteDataSource.getUserClimbingRecords(userAccessToken)
        }
    }

    override fun updateUserProfile(
        updateProfileRequest: UpdateProfileRequest
    ): Flow<Resource<String>> {
        return userAuthRepository.userAuthActionFlow { userAccessToken ->
            remoteDataSource.updateUserProfile(
                userAccessToken = userAccessToken,
                updateProfileRequest = updateProfileRequest
            )
        }
    }

    override fun completeClimbing(
        completeClimbingRequest: CompleteClimbingRequest
    ): Flow<Resource<String>> {
        return userAuthRepository.userAuthActionFlow { userAccessToken ->
            remoteDataSource.completeClimbingRequest(
                userAccessToken = userAccessToken,
                completeClimbingRequest = completeClimbingRequest
            )
        }
    }

}