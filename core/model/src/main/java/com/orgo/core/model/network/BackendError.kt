package com.orgo.core.model.network

data class BackendError(
    val code : String,
    val name : String,
    val message : String,
    val status : Int
)