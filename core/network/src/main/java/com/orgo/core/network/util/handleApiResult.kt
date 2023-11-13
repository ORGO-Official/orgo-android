package com.orgo.core.network.util

import com.orgo.core.model.network.Resource

fun handleApiResultErrorCode(code : Int, message : String?) : Resource.Failure{
    return when(code){
        400 -> Resource.Failure(message ?: "Bad Request", code)
        401 -> Resource.Failure(message ?: "Token Expired", code)
        403 -> Resource.Failure(message ?: "Forbidden", code)
        404 -> Resource.Failure(message ?: "Not Found", code)
        else -> Resource.Failure(message ?: "Unknown Error", code)
    }
}