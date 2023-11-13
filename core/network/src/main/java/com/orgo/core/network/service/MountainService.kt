package com.orgo.core.network.service

import com.orgo.core.model.data.Restaurant
import com.orgo.core.model.data.mountain.CompleteClimbingRequest
import com.orgo.core.model.data.mountain.Mountain
import com.orgo.core.model.network.ApiResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MountainService {

    @GET("api/mountains")
    suspend fun getMountains() : ApiResult<List<Mountain>>

    @GET("api/mountains/{mountainId}")
    suspend fun getMountain(
        @Path("mountainId") mountainId: Int
    ) : ApiResult<Mountain>

    @GET("api/mountains")
    suspend fun searchMountains(
        @Query("keyword") keyword : String,
    ) : ApiResult<List<Mountain>>

    @GET("api/mountains/{mountainId}/restaurants")
    suspend fun getNearRestaurants(
        @Path("mountainId") mountainId : Int
    ) : ApiResult<List<Restaurant>>


}