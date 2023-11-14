package com.orgo.climb_complete

import com.orgo.core.model.data.user.ClimbingRecord
import com.orgo.core.model.data.user.ClimbingRecordDto

sealed interface ClimbCompleteUiState {
    object Loading : ClimbCompleteUiState


    data class Success(
        val climbingRecordDto: ClimbingRecordDto
    ) : ClimbCompleteUiState

    data class Failure(
        val message: String = ""
    ) : ClimbCompleteUiState
}