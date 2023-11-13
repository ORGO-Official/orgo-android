package com.orgo.feature.mountain_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orgo.core.common.util.decoder.StringDecoder
import com.orgo.core.domain.usecase.mountain.GetMountainDetailUseCase
import com.orgo.core.domain.usecase.mountain.GetNearRestaurantsUseCase
import com.orgo.core.model.data.Restaurant
import com.orgo.core.model.data.mountain.Mountain
import com.orgo.core.model.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MountainDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    stringDecoder: StringDecoder,
    private val getMountainDetailUseCase: GetMountainDetailUseCase,
    private val getNearRestaurantsUseCase: GetNearRestaurantsUseCase
) : ViewModel() {

    private val mountainArgs: MountainArgs = MountainArgs(savedStateHandle, stringDecoder)

    private val mountainId = mountainArgs.mountainId

    private val _mountainDetailUiState = MutableStateFlow<MountainDetailUiState>(MountainDetailUiState.Loading)
    val mountainDetailUiState: StateFlow<MountainDetailUiState> = _mountainDetailUiState.asStateFlow()

    init {
        fetchMountainDetail()
    }
    fun fetchMountainDetail() = viewModelScope.launch {
        mountainDetailUiState(mountainId).collect{ newState ->
            _mountainDetailUiState.value = newState
        }
    }

    private fun mountainDetailUiState(
        mountainId: Int,
    ): Flow<MountainDetailUiState> {

        val mountainDetail: Flow<Resource<Mountain>> = getMountainDetailUseCase(mountainId)

        val nearRestaurants: Flow<Resource<List<Restaurant>>> =
            getNearRestaurantsUseCase(mountainId)

        // 2개의 Resource 타입을 확인하고 UiState로 합쳐야함
        return mountainDetail.combine(nearRestaurants) { mountainDetail, nearRestaurants ->
            when(mountainDetail){
                is Resource.Loading -> MountainDetailUiState.Loading
                is Resource.Success -> {
                    when(nearRestaurants){
                        is Resource.Loading -> MountainDetailUiState.Success(
                            isLoadingRestaurants = true,
                            mountain = mountainDetail.data,
                            restaurants = listOf()
                        )
                        is Resource.Success -> MountainDetailUiState.Success(
                            isLoadingRestaurants = false,
                            mountain = mountainDetail.data,
                            restaurants = nearRestaurants.data
                        )
                        is Resource.Failure -> MountainDetailUiState.Success(
                            isLoadingRestaurants = false,
                            mountain = mountainDetail.data,
                            restaurants = listOf()
                        )
                    }
                }
                is Resource.Failure -> MountainDetailUiState.Failure(
                    message = mountainDetail.errorMessage
                )
            }
        }
    }
}