package com.orgo.core.domain.usecase.auth

import com.orgo.core.data.repository.UserAuthRepository
import com.orgo.core.model.data.SocialType
import com.orgo.core.model.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userAuthRepository: UserAuthRepository,
){
    suspend operator fun invoke(socialType : SocialType, token : String) : Flow<Resource<String>> {
        return userAuthRepository.executeLogin(socialType,token)
    }
}