package com.orgo.core.domain.usecase.user

import com.orgo.core.data.repository.UserRepository
import com.orgo.core.model.data.user.UserProfile
import com.orgo.core.model.network.Resource
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Resource<UserProfile>> {
        Timber.tag("mypage").d("GetUserProfileUseCase 들어옴")
        return userRepository.getUserProfile()
    }
}