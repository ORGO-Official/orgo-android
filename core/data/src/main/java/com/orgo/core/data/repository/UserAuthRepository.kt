package com.orgo.core.data.repository

import com.orgo.core.model.data.SocialType
import com.orgo.core.model.data.UserAuthState
import com.orgo.core.model.data.UserToken
import com.orgo.core.model.network.ApiResult
import com.orgo.core.model.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UserAuthRepository {

    /**
     *  오르GO의 UserToken을 받아옴
     *
     *  @property socialType 로그인 토큰 타입 (네이버,카카오,구글)
     *  @property token 실제 토큰
     * */
    val userAuthState: StateFlow<UserAuthState>

    suspend fun executeLogin(socialType : SocialType, token : String) : Flow<Resource<String>>

    suspend fun executeAutoLogin() : Flow<Resource<String>>

    suspend fun executeLogout() : Flow<Resource<String>>

    suspend fun executeWithdraw() : Flow<Resource<String>>

    suspend fun getSocialType() : SocialType

    fun <T : Any> userAuthActionFlow(
        apiCall: suspend (String) -> ApiResult<T>)
    : Flow<Resource<T>>


}