package com.orgo.core.network.util

import com.orgo.core.datastore.UserTokenDataSource
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val userTokenDataSource: UserTokenDataSource,
): Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val authRequestBuilder = chain.request().newBuilder()
        val userToken = runBlocking {
            withTimeoutOrNull(1000){
                userTokenDataSource.getUserAccessToken()
            }
        }
        authRequestBuilder.addHeader("auth", "$userToken")
//        authRequestBuilder.addHeader("auth", "asdasdasd")
        val response = chain.proceed(authRequestBuilder.build())
        if(response.header("Authorization") != null){
            val newAccessToken = response.header("Authorization")!!
            Timber.tag("interceptor").d("header Authorization : $newAccessToken")
            runBlocking {
                withTimeoutOrNull(1000){
                    userTokenDataSource.refreshUserToken(newAccessToken)
                }
            }
        }
        return response
    }
}