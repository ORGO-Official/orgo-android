package com.orgo.core.domain.usecase.mountain

import com.orgo.core.data.repository.UserRepository
import com.orgo.core.domain.usecase.user.GetUserClimbingRecordUseCase
import com.orgo.core.model.data.mountain.CompleteClimbingRequest
import com.orgo.core.model.data.user.ClimbingRecordDto
import com.orgo.core.model.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CompleteClimbingUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val getUserClimbingRecordUseCase: GetUserClimbingRecordUseCase
) {
    suspend operator fun invoke(
        completeClimbingRequest: CompleteClimbingRequest
    ): Flow<Resource<String>> {

        val mountainId = completeClimbingRequest.mountainId
        var mountainClimbingRecords: List<ClimbingRecordDto> = listOf()

        // 하루에 산은 한 번만 가능함
        getUserClimbingRecordUseCase().collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    mountainClimbingRecords = resource.data.getRecordsByMountainId(mountainId)
                }
                else -> Unit
            }
        }

        // 최근에 완등한 기록이 있는 경우
        return if (mountainClimbingRecords.isNotEmpty() && !checkPossibleCompleteTime(mountainClimbingRecords)) {
            flow { emit(Resource.Failure("12시간 내에 완등한 기록이 있습니다", 400)) }
        } else userRepository.completeClimbing(completeClimbingRequest)
    }

}

private fun checkPossibleCompleteTime(
    mountainClimbingRecords : List<ClimbingRecordDto>
): Boolean {
    mountainClimbingRecords.forEach { record ->
        val recordTime = LocalDateTime.parse(record.date, DateTimeFormatter.ISO_DATE_TIME)
        if(Duration.between(recordTime, LocalDateTime.now()).toHours() < 12) return false
    }
    return true
}