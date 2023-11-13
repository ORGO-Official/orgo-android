package com.orgo.core.domain.usecase.auth


import com.orgo.core.common.auth.KakaoAuthManager
import com.orgo.core.common.auth.NaverAuthManager
import com.orgo.core.data.repository.UserAuthRepository
import com.orgo.core.model.data.SocialType
import com.orgo.core.model.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WithdrawUseCase @Inject constructor(
    private val userAuthRepository: UserAuthRepository,
) {
    suspend operator fun invoke(): Flow<Resource<String>> {
        return userAuthRepository.executeWithdraw()
    }
}