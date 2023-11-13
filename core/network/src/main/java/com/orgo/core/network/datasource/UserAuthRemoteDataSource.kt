package com.orgo.core.network.datasource

import com.orgo.core.model.data.SocialType
import com.orgo.core.model.data.UserToken
import com.orgo.core.model.network.ApiResult
import com.orgo.core.network.service.UserAuthService
import javax.inject.Inject

class UserAuthRemoteDataSource @Inject constructor(
    private val userAuthService: UserAuthService
) {

    suspend fun getUserToken(socialType: SocialType, token: String): ApiResult<UserToken> =
        userAuthService.getUsetToken(loginType = socialType.value, socialToken = token)

    suspend fun executeLogout(socialToken: String, userAccessToken: String): ApiResult<String> =
        userAuthService.executeLogout(socialToken = socialToken, userAccessToken = userAccessToken)

    suspend fun executeWithdraw(socialToken: String, userAccessToken: String) =
        userAuthService.executeWithdraw(
            socialToken = socialToken,
            userAccessToken = userAccessToken
        )

    suspend fun executeReissue(userAccessToken: String, userRefreshToken: String) =
        userAuthService.executeReissue(
            userAccessToken = userAccessToken,
            userRefreshToken = userRefreshToken
        )
}