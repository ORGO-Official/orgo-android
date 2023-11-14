package com.orgo.core.network.datasource

import com.orgo.core.model.data.badge.AcquiredBadge
import com.orgo.core.model.data.badge.UnacquiredBadge
import com.orgo.core.model.data.mountain.Mountain
import com.orgo.core.model.network.Resource
import com.orgo.core.network.service.BadgeService
import com.orgo.core.network.service.MountainService
import com.orgo.core.network.util.apiResultToResourceFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BadgeRemoteDataSource @Inject constructor(
    private val badgeService: BadgeService
) {

    suspend fun getAcquiredBadges(): Flow<Resource<List<AcquiredBadge>>> =
        apiResultToResourceFlow { badgeService.getAcquiredBadges() }

    suspend fun getUnacquiredBadges() : Flow<Resource<List<UnacquiredBadge>>> =
        apiResultToResourceFlow { badgeService.getUnacquiredBadges() }

}
