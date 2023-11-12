package com.orgo.core.network.util.calladapter

import com.orgo.core.model.network.ApiResult
import com.orgo.core.model.network.BackendError
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

fun <T : Any> handleApi(
    execute: () -> Response<T>
): ApiResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            Timber.tag("network").d("handleApi : response.iwsSuccessful && body not null")
//            if (body is BackendError){
//                ApiResult.Error(code = body.status, message = body.message)
//            }else
                ApiResult.Success(body)
        }else if(response.code() in 200..299){
            @Suppress("UNCHECKED_CAST")
                ApiResult.Success("success" as T)
        }
        else{
            Timber.tag("network").e("handleApi : response not Successful or body is null ")
            val responseJson = JSONObject(response.errorBody()?.string()!!)
            val errMsg = "${responseJson.getString("status")} ${responseJson.getString("error")}"
            Timber.tag("network").e("errMsg : $errMsg")
            ApiResult.Error(code = response.code(), message = errMsg)
        }
    } catch (e: HttpException) {
        Timber.tag("network").d("handleApi : Error HttpException")
        ApiResult.Error(code = e.code(), message = e.message())
    } catch (e: Throwable) {
        Timber.tag("network").d("handleApi : Exception")
        ApiResult.Exception(e)
    }
}
