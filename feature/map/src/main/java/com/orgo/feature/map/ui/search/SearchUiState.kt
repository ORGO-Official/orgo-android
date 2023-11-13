package com.orgo.feature.map.ui.search

import com.orgo.core.model.data.mountain.Mountain

interface SearchUiState {
    object Loading : SearchUiState

    object NoResult : SearchUiState

    data class Success(
        val mountains : List<Mountain>,
    ) : SearchUiState

    data class Failure(
        val errMsg : String
    ) : SearchUiState
}