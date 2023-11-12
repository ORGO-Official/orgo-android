package com.orgo.core.domain.usecase.auth

import com.orgo.core.data.repository.UserAuthRepository
import com.orgo.core.model.network.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AutoLoginUseCase @Inject constructor(
    private val userAuthRepository: UserAuthRepository,
){
    suspend operator fun invoke() : Flow<Resource<String>> {
        return userAuthRepository.executeAutoLogin()
    }
}