package com.orgo.core.model.data.mountain

data class CompleteClimbingRequest(
    val mountainId: Int,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val date: String,
){
    companion object{
        val DUMMY_REQUEST = CompleteClimbingRequest(
            mountainId = 1,
            latitude = 36.103958,
            longitude = 128.423648,
            altitude =  352.168,
            date = "2023-09-14T05:18:41.912871187"
        )
    }
}