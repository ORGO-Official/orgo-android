package com.orgo.core.network.service

import com.orgo.core.model.data.badge.AcquiredBadge
import com.orgo.core.model.data.badge.UnacquiredBadge
import com.orgo.core.model.data.mountain.Mountain
import com.orgo.core.model.network.ApiResult
import retrofit2.http.GET

interface BadgeService {

    @GET("/api/badges/acquired")
    suspend fun getAcquiredBadges() : ApiResult<List<AcquiredBadge>>

    @GET("/api/badges/not-acquired")
    suspend fun getUnacquiredBadges() : ApiResult<List<UnacquiredBadge>>

}