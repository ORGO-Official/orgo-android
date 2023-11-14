package com.orgo.core.domain.usecase.badge

import com.orgo.core.data.repository.BadgeRepository
import com.orgo.core.model.data.badge.Badge
import com.orgo.core.model.data.badge.UnacquiredBadge
import com.orgo.core.model.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class GetBadgesUsecase @Inject constructor(
    private val badgeRepository: BadgeRepository,
) {
    suspend operator fun invoke(): Flow<Resource<List<Badge>>> {
        val acquiredBadgesResource = badgeRepository.getAcquiredBadges()
        val unacquiredBadgesResource = badgeRepository.getUnacquiredBadges()

        return acquiredBadgesResource.combine(unacquiredBadgesResource){ acquired, unacquired ->
            Timber.d("acquired : $acquired, unacquired : $unacquired")
            when(acquired) {
                is Resource.Success -> {
                    when(unacquired) {
                        is Resource.Success -> {
                            Resource.Success(acquired.data + unacquired.data)
                        }
                        else -> unacquired
                    }
                }
                is Resource.Failure -> acquired
                is Resource.Loading -> acquired
            }
        }
    }
}
