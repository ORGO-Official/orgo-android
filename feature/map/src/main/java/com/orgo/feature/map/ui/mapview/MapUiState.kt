package com.orgo.feature.map.ui.mapview

import android.location.Location
import com.orgo.core.model.data.mountain.Mountain
import com.orgo.core.model.data.mountain.OrgoLocation

sealed interface MapUiState{
    object Loading : MapUiState

    data class Map(
        val selectedMountain : Mountain? = null,
        val mountains : List<Mountain>,
        val isSearchBarActive : Boolean = false,
        val isCompleteBtnEnabled : Boolean = false,
        val isLocationPermissionGranted : Boolean = false,
        val currentLocation : OrgoLocation? = null,
        val errMsg: String = ""
    ) : MapUiState

    data class Search(
        val keyword : String = "",
        val mountains : List<Mountain> = listOf(),
    ) : MapUiState

    data class Failure(
        val errMsg : String
    ) : MapUiState

    object CompleteSuccess : MapUiState
}