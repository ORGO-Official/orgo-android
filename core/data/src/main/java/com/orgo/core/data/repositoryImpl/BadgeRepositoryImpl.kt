package com.orgo.core.data.repositoryImpl

import com.orgo.core.data.repository.BadgeRepository
import com.orgo.core.data.repository.MountainRepository
import com.orgo.core.model.data.badge.AcquiredBadge
import com.orgo.core.model.data.badge.UnacquiredBadge
import com.orgo.core.model.data.mountain.Mountain
import com.orgo.core.model.network.Resource
import com.orgo.core.network.datasource.BadgeRemoteDataSource
import com.orgo.core.network.datasource.MountainRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BadgeRepositoryImpl @Inject constructor(
    private val remoteDataSource: BadgeRemoteDataSource
): BadgeRepository {

    override suspend fun getAcquiredBadges(): Flow<Resource<List<AcquiredBadge>>> {
        return remoteDataSource.getAcquiredBadges()

    }

    override suspend fun getUnacquiredBadges(): Flow<Resource<List<UnacquiredBadge>>> {
        return remoteDataSource.getUnacquiredBadges()
    }
}