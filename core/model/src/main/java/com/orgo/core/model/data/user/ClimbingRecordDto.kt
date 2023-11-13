package com.orgo.core.model.data.user

data class ClimbingRecordDto(
    val id: Int,
    val mountainId: Int,
    val mountainName: String,
    val date: String,
    val altitude: Double,
    val climbingOrder: Int
)