package com.orgo.core.model.network

sealed class ApiResult<T : Any> {
    class Success<T: Any>(val data: T) : ApiResult<T>()
    class Error<T: Any>(val code: Int, val message: String?) : ApiResult<T>()
    class Exception<T: Any>(val e: Throwable) : ApiResult<T>()
}

suspend fun <T : Any> ApiResult<T>.onSuccess(
    executable: suspend (T) -> Unit
): ApiResult<T> = apply {
    if (this is ApiResult.Success<T>) {
        executable(data)
    }
}

suspend fun <T : Any> ApiResult<T>.onError(
    executable: suspend (code: Int, message: String?) -> Unit
): ApiResult<T> = apply {
    if (this is ApiResult.Error<T>) {
        executable(code, message)
    }
}

suspend fun <T : Any> ApiResult<T>.onException(
    executable: suspend (e: Throwable) -> Unit
): ApiResult<T> = apply {
    if (this is ApiResult.Exception<T>) {
        executable(e)
    }
}

suspend fun <T : Any> ApiResult<T>.onErrorOrException(
    executable: suspend (code: Int, message: String?) -> Unit
): ApiResult<T> = apply {
    if (this is ApiResult.Error<T>) {
        executable(code, message)
    }else if (this is ApiResult.Exception<T>) {
        executable(999, e.message)
    }
}
