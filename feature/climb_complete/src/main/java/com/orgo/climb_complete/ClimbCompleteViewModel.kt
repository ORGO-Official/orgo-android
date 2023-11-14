package com.orgo.climb_complete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orgo.core.domain.usecase.user.GetUserClimbingRecordUseCase
import com.orgo.core.model.data.user.ClimbingRecord
import com.orgo.core.model.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ClimbCompleteViewModel @Inject constructor(
    private val getUserClimbingRecordUseCase: GetUserClimbingRecordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ClimbCompleteUiState>(ClimbCompleteUiState.Loading)
    val uiState: StateFlow<ClimbCompleteUiState> = _uiState.asStateFlow()

    init {
        fetchRecentRecord()
    }

    fun fetchRecentRecord() = viewModelScope.launch {
        getUserClimbingRecordUseCase().collect { resource ->
            when (resource) {
                is Resource.Loading -> _uiState.update { ClimbCompleteUiState.Loading }
                is Resource.Success -> {
                    val climbingRecord = resource.data.getLatestRecord()
                    if (climbingRecord != null) {
                        _uiState.update { ClimbCompleteUiState.Success(climbingRecord) }
                    } else {
                        _uiState.update {
                            ClimbCompleteUiState.Failure(
                                message = "최근 기록이 없습니다."
                            )
                        }
                    }
                }

                is Resource.Failure -> {
                    _uiState.update {
                        ClimbCompleteUiState.Success(ClimbingRecord.DUMMY_RECORD.getLatestRecord()!!)
//                        ClimbCompleteUiState.Failure(
//                            message = "완등 인증 조회에 실패하였습니다."
//                        )
                    }
                    Timber.e(resource.printError())
                }
            }

        }


    }


}

