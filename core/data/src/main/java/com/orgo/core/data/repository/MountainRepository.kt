package com.orgo.core.data.repository

import com.orgo.core.model.data.Restaurant
import com.orgo.core.model.data.mountain.CompleteClimbingRequest
import com.orgo.core.model.data.mountain.Mountain
import com.orgo.core.model.network.Resource
import kotlinx.coroutines.flow.Flow

interface MountainRepository {

    suspend fun getMountains() : Flow<Resource<List<Mountain>>>

    fun getMountain(mountainId : Int)  : Flow<Resource<Mountain>>

    fun searchMountains(keyword : String) : Flow<Resource<List<Mountain>>>

    fun getNearRestaurants(mountainId : Int) : Flow<Resource<List<Restaurant>>>

}