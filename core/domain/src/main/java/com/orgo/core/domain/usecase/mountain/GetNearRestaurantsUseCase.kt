package com.orgo.core.domain.usecase.mountain

import com.orgo.core.data.repository.MountainRepository
import com.orgo.core.model.data.Restaurant
import com.orgo.core.model.data.mountain.Mountain
import com.orgo.core.model.network.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNearRestaurantsUseCase @Inject constructor(
    private val mountainRepository: MountainRepository
) {
    operator fun invoke(
        mountainId : Int,
    ) : Flow<Resource<List<Restaurant>>>
        = mountainRepository.getNearRestaurants(mountainId)

}