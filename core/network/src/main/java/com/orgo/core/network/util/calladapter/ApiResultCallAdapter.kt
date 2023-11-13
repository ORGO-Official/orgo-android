package com.orgo.core.network.util.calladapter

import com.orgo.core.model.network.ApiResult
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ApiResultCallAdapter(
    private val resultType: Type
) : CallAdapter<Type, Call<ApiResult<Type>>> {

    override fun responseType(): Type = resultType

    override fun adapt(call: Call<Type>): Call<ApiResult<Type>> {
        return ApiResultCall(call)
    }
}