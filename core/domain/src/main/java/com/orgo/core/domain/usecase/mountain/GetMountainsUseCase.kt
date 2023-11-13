package com.orgo.core.domain.usecase.mountain

import com.orgo.core.data.repository.MountainRepository
import com.orgo.core.model.data.mountain.Mountain
import com.orgo.core.model.network.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMountainsUseCase @Inject constructor(
    private val mountainRepository : MountainRepository
){
    suspend operator fun invoke() : Flow<Resource<List<Mountain>>>
        = mountainRepository.getMountains()
}