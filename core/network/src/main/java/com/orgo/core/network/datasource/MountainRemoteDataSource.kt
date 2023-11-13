package com.orgo.core.network.datasource

import com.orgo.core.model.data.Restaurant
import com.orgo.core.model.data.mountain.CompleteClimbingRequest
import com.orgo.core.model.data.mountain.Mountain
import com.orgo.core.model.network.Resource
import com.orgo.core.network.service.MountainService
import com.orgo.core.network.util.apiResultToResourceFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MountainRemoteDataSource @Inject constructor(
    private val mountainService: MountainService
) {

    suspend fun getMountains(): Flow<Resource<List<Mountain>>> =
        apiResultToResourceFlow { mountainService.getMountains() }

    fun getMountain(
        mountainId : Int,
    ): Flow<Resource<Mountain>> =
        apiResultToResourceFlow { mountainService.getMountain(mountainId = mountainId) }

    fun searchMountains(
        keyword: String,
    ): Flow<Resource<List<Mountain>>> =
        apiResultToResourceFlow { mountainService.searchMountains(keyword) }

    fun getNearRestaurants(
        mountainId: Int
    ): Flow<Resource<List<Restaurant>>> =
        apiResultToResourceFlow { mountainService.getNearRestaurants(mountainId = mountainId) }

}