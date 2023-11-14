package com.orgo.core.data.repository

import com.orgo.core.model.data.badge.AcquiredBadge
import com.orgo.core.model.data.badge.UnacquiredBadge
import com.orgo.core.model.data.mountain.Mountain
import com.orgo.core.model.network.Resource
import kotlinx.coroutines.flow.Flow

interface BadgeRepository {

    suspend fun getAcquiredBadges() : Flow<Resource<List<AcquiredBadge>>>

    suspend fun getUnacquiredBadges() : Flow<Resource<List<UnacquiredBadge>>>
}