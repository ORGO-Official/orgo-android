package com.orgo.feature.mountain_detail

import com.orgo.core.model.data.Restaurant
import com.orgo.core.model.data.mountain.Mountain

sealed interface MountainDetailUiState {

    object Loading : MountainDetailUiState

    data class Success(
        val isLoadingRestaurants : Boolean,
        val mountain : Mountain,
        val restaurants : List<Restaurant>,
    ) : MountainDetailUiState

    data class Failure(
        val message : String = ""
    ) : MountainDetailUiState
}