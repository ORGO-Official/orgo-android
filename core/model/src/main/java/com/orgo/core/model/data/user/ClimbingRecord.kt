package com.orgo.core.model.data.user

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ClimbingRecord(
    val climbedAltitude: Double,
    val climbingCnt: Int,
    val climbingRecordDtoList: List<ClimbingRecordDto>
){

    fun getLatestRecord(): ClimbingRecordDto? {
        return climbingRecordDtoList.maxByOrNull { LocalDateTime.parse(it.date, DateTimeFormatter.ISO_DATE_TIME) }
    }

    fun getRecordsByMountainId(mountainId: Int): List<ClimbingRecordDto> {
        return climbingRecordDtoList.filter { it.mountainId == mountainId }
    }

    companion object {
        val DUMMY_RECORD = ClimbingRecord(
            0.0, 3,
            climbingRecordDtoList = listOf(
                ClimbingRecordDto(
                    1,
                    1,
                    "에베레스트산",
                    "2023-09-12T05:22:46.726631243",
                    0.0,
                    1
                ),
                ClimbingRecordDto(
                    2,
                    1,
                    "에베레스트산",
                    "2023-09-13T05:22:46.726631243",
                    100.0,
                    2
                )
            )
        )
    }
}