package com.orgo.core.network.service

import com.orgo.core.model.data.UserToken
import com.orgo.core.model.network.ApiResult
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface UserAuthService {

    @POST("api/auth/login/{loginType}")
    suspend fun getUsetToken(
        @Header("social") socialToken : String,
        @Path("loginType") loginType: String
    ): ApiResult<UserToken>

    @POST("api/auth/logout")
    suspend fun executeLogout(
        @Header("auth") userAccessToken : String,
        @Header("social") socialToken: String
    ): ApiResult<String>

    @POST("api/auth/withdraw")
    suspend fun executeWithdraw(
        @Header("auth") userAccessToken : String,
        @Header("social") socialToken: String
    ): ApiResult<String>

    // AccessToken : String 반환
    @POST("api/auth/reissue")
    suspend fun executeReissue(
        @Header("auth") userAccessToken : String,
        @Header("refresh") userRefreshToken : String,
    ) : ApiResult<String>

}