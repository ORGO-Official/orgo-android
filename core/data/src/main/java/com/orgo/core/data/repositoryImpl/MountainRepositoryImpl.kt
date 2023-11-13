package com.orgo.core.data.repositoryImpl

import com.orgo.core.data.repository.MountainRepository
import com.orgo.core.data.repository.UserAuthRepository
import com.orgo.core.model.data.Restaurant
import com.orgo.core.model.data.mountain.CompleteClimbingRequest
import com.orgo.core.model.data.mountain.Mountain
import com.orgo.core.model.network.Resource
import com.orgo.core.network.datasource.MountainRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MountainRepositoryImpl @Inject constructor(
    private val remoteDataSource: MountainRemoteDataSource
): MountainRepository {

    override suspend fun getMountains(): Flow<Resource<List<Mountain>>> {
        return remoteDataSource.getMountains()
    }
    override fun getMountain(mountainId : Int) : Flow<Resource<Mountain>>{
        return remoteDataSource.getMountain(mountainId = mountainId)
    }

    override fun searchMountains(keyword : String) : Flow<Resource<List<Mountain>>>{
        return remoteDataSource.searchMountains(keyword)
    }

    override fun getNearRestaurants(mountainId: Int): Flow<Resource<List<Restaurant>>> {
        return remoteDataSource.getNearRestaurants(mountainId = mountainId)
    }


}
