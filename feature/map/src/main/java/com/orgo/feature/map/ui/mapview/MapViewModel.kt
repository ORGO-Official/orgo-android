package com.orgo.feature.map.ui.mapview

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orgo.core.common.util.date.getCurrentDateTimeAsString
import com.orgo.core.domain.usecase.mountain.CompleteClimbingUseCase
import com.orgo.core.domain.usecase.mountain.GetMountainsUseCase
import com.orgo.core.domain.usecase.mountain.SearchMountainsUseCase
import com.orgo.core.model.data.mountain.CompleteClimbingRequest
import com.orgo.core.model.data.mountain.OrgoLocation
import com.orgo.core.model.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

const val SEARCH_DEBOUNCE_TIME = 200L

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getMountainsUseCase: GetMountainsUseCase,
    private val searchMountainsUseCase: SearchMountainsUseCase,
    private val completeClimbingUseCase: CompleteClimbingUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MapUiState>(MapUiState.Loading)
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch { loadMountains() }
    }

    fun refreshScreen(){
        loadMountains()
    }


    private fun loadMountains(selectedMountainName: String? = null) =
        viewModelScope.launch {
            getMountainsUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { MapUiState.Loading }
                        delay(500L)
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            MapUiState.Map(
                                selectedMountain = if (selectedMountainName != null) resource.data.first { it.name == selectedMountainName } else null,
                                mountains = resource.data
                            )
                        }
                    }
                    is Resource.Failure -> {
                        _uiState.update { MapUiState.Failure(resource.errorMessage) }
                        Timber.e("code : ${resource.code}, msg : ${resource.errorMessage}")
                    }
                }
            }
        }

    private fun searchMountain(keyword: String) = viewModelScope.launch {
        if(keyword.isEmpty()){
            getMountainsUseCase().collect{ resource ->
                when(resource){
                    is Resource.Success -> {
                        _uiState.update { state ->
                            (state as MapUiState.Search).copy(mountains = resource.data)
                        }
                    }
                    is Resource.Failure -> _uiState.update { MapUiState.Failure(resource.errorMessage) }
                    is Resource.Loading -> Unit
                }
            }
        } else {
            searchMountainsUseCase(keyword = keyword).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update { state ->
                            (state as MapUiState.Search).copy(mountains = resource.data)
                        }
                    }
                    is Resource.Failure -> _uiState.update { MapUiState.Failure(resource.errorMessage) }
                    is Resource.Loading -> Unit
                }
            }
        }
    }

    // 현재 위치랑 선택한 산의 위치 차이를 계산해서 완등 가능한 거리인지 확인하는 함수
    fun checkCompletePossible(location: Location?) {
        val state = uiState.value
        if (state is MapUiState.Map && location != null && state.selectedMountain != null) {
            val currentLocation = OrgoLocation(
                altitude = location.altitude,
                latitude = location.latitude,
                longitude = location.longitude
            )

            _uiState.update {
                state.copy(
                    currentLocation = currentLocation,
                    isCompleteBtnEnabled = checkCompleteDistance(
                        currentLocation = currentLocation,
                        mountainLocation = state.selectedMountain.location
                    )
                )
            }
        }
    }

    private fun checkCompleteDistance(
        currentLocation: OrgoLocation,
        mountainLocation: OrgoLocation
    ): Boolean {
        val distance = calDistance(
            latitude1 = currentLocation.latitude,
            longitude1 = currentLocation.longitude,
            latitude2 = mountainLocation.latitude,
            longitude2 = mountainLocation.longitude
        )
        return distance <= 0.5
    }

    private fun calDistance(
        latitude1: Double,
        longitude1: Double,
        latitude2: Double,
        longitude2: Double,
    ): Double {
        val RADIUS_OF_EARTH_KM = 6371.0
        val latitudeRad1 = Math.toRadians(latitude1)
        val longitudeRad1 = Math.toRadians(longitude1)
        val latitudeRad2 = Math.toRadians(latitude2)
        val longitudeRad2 = Math.toRadians(longitude2)

        val latitudeDiff = latitudeRad2 - latitudeRad1
        val longitudeDiff = longitudeRad2 - longitudeRad1

        val a = sin(latitudeDiff / 2) * sin(latitudeDiff / 2) +
                cos(latitudeRad1) * cos(latitudeRad2) *
                sin(longitudeDiff / 2) * sin(longitudeDiff / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return RADIUS_OF_EARTH_KM * c
    }


    // 완등 버튼 클릭 했을 때 이 함수 호출
    fun completeClimbing() = viewModelScope.launch {
        val state = uiState.value
        if (state is MapUiState.Map && state.currentLocation != null && state.selectedMountain != null) {
            completeClimbingUseCase(
                CompleteClimbingRequest(
                    mountainId = state.selectedMountain.id,
                    latitude = state.currentLocation.latitude,
                    longitude = state.currentLocation.longitude,
                    altitude = state.currentLocation.altitude,
                    date = getCurrentDateTimeAsString()
                )
//                 완등 인증 테스트용
//                CompleteClimbingRequest(
//                    mountainId = state.selectedMountain.id,
//                    latitude = state.selectedMountain.location.latitude,
//                    longitude = state.selectedMountain.location.longitude,
//                    altitude = state.selectedMountain.location.altitude,
//                    date = getCurrentDateTimeAsString()
//                )
            ).collect { resource ->
                when (resource) {
                    is Resource.Loading -> Unit
                    is Resource.Success -> _uiState.update { MapUiState.CompleteSuccess }
                    is Resource.Failure -> {
                        _uiState.update{ state ->
                            (state as MapUiState.Map).copy(
                                selectedMountain = null,
                                errMsg = resource.errorMessage
                            )
                        }
                        Timber.e(resource.errorMessage)
                    }
                }
            }
        }
    }

    fun updateSelectedMountain(mountainName: String?) {
        val state = uiState.value
        if (state is MapUiState.Map) {
            _uiState.update {
                state.copy(selectedMountain = if (mountainName == null) null else state.mountains.first { it.name == mountainName })
            }
        }
        if (state is MapUiState.Search) {
            loadMountains(mountainName)
        }
    }

    fun updateSearchBarActive(active: Boolean) = viewModelScope.launch {
        if (active) {
            _uiState.update { state ->
                if (state is MapUiState.Map) {
                    MapUiState.Search(mountains = state.mountains)
                } else {
                    MapUiState.Search()
                }
            }
//            startDebounce()
        } else {
            loadMountains()
        }
    }

    fun updateKeyword(keyword: String) = viewModelScope.launch {
        _uiState.update { state ->
            (state as MapUiState.Search).copy(keyword = keyword)
        }
        searchMountain(keyword)
//        Timber.d("keyword : ${(uiState.value as MapUiState.Search).keyword.value}")
    }

    fun updateLocationPermission(isGranted : Boolean){
        if(uiState.value is MapUiState.Map){
            _uiState.update { state ->
                (state as MapUiState.Map).copy(isLocationPermissionGranted = isGranted)
            }
        }
    }
    
    fun resetErrorMsg(){
        if(uiState.value is MapUiState.Map){
            _uiState.update {  state ->
                (state as MapUiState.Map).copy(errMsg = "")
            }
        }
    }

//    } TODO: Debounce 적용해보기 -> 현재 오류 너무 많이 떠서 나중에 처리해야할듯

//    private fun startDebounce() = viewModelScope.launch {
//        if (uiState.value is MapUiState.Search) {
//            Timber.d("startDebounce")
//            (uiState.value as MapUiState.Search).keyword.debounce(SEARCH_DEBOUNCE_TIME)
//                .filter {
//                    Timber.d("keyword flow inside keyword : $it")
//                    it.isNotEmpty()
//                }
//                .collect { keyword ->
//                    Timber.d("flow keyword : $keyword")
//                    searchMountain(keyword)
//                }
//        }
//    }

}
