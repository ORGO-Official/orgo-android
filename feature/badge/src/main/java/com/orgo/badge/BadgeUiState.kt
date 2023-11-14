package com.orgo.badge

import com.orgo.core.model.data.badge.Badge
import com.orgo.core.model.data.mountain.Mountain
import com.orgo.core.model.data.user.ClimbingRecord
import com.orgo.core.model.data.user.UserProfile

sealed interface BadgeUiState{
    object Loading : BadgeUiState

    data class Success(
        val badges : List<Badge>,
        val selectedBadge : Badge? = null,
    ) : BadgeUiState

    object Unauthenticated : BadgeUiState

    data class Failure(
        val message : String = ""
    ) : BadgeUiState
}