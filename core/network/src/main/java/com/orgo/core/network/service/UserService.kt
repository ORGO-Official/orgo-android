package com.orgo.core.network.service

import com.orgo.core.model.data.mountain.CompleteClimbingRequest
import com.orgo.core.model.data.user.ClimbingRecord
import com.orgo.core.model.data.user.UserProfile
import com.orgo.core.model.network.ApiResult
import com.orgo.core.model.network.profile.UpdateNicknameRequest
import com.orgo.core.model.network.profile.UpdateProfileRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface UserService {

    @GET("api/users/profile")
    suspend fun getUserProfile(
//        @Header("auth") userAccessToken : String,
    ): ApiResult<UserProfile>

    @Multipart
    @PUT("api/users/profile")
    suspend fun updateUserProfile(
//        @Header("auth") userAccessToken : String,
        @Part("requestDto") nickname : UpdateNicknameRequest,
        @Part imageFile : MultipartBody.Part,
    ) : ApiResult<String>

    @GET("/api/climbing-records")
    suspend fun getUserClimbingRecords(
//        @Header("auth") userAccessToken : String,
    ) : ApiResult<ClimbingRecord>


    @POST("api/climbing-records")
    suspend fun completeClimbing(
//        @Header("auth") userAccessToken : String,
        @Body completeClimbingRequest: CompleteClimbingRequest
    ) : ApiResult<String>


}