package com.orgo.core.model.network

sealed class Resource<out R> {
    object Loading: Resource<Nothing>()

    data class Success<out T>(
        val data: T
    ): Resource<T>()

    data class Failure(
        val errorMessage: String,
        val code: Int,
    ): Resource<Nothing>(){
        fun printError() : String{
            return "code : $code, msg : $errorMessage"
        }
    }
}
