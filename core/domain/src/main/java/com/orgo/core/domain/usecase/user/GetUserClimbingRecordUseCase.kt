package com.orgo.core.domain.usecase.user

import com.orgo.core.data.repository.UserRepository
import com.orgo.core.model.data.user.ClimbingRecord
import com.orgo.core.model.network.Resource
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class GetUserClimbingRecordUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Resource<ClimbingRecord>>  {
        Timber.tag("mypage").d("GetUserClimbingRecordUseCase 들어옴")
        return userRepository.getUserClimbingRecords()
    }
}