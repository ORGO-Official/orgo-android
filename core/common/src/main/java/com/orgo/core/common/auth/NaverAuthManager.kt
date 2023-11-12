package com.orgo.core.common.auth

import android.content.Context
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import java.io.IOException
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class NaverAuthManager {

    suspend fun executeLogin(context: Context): String = suspendCoroutine { continuation ->
        var naverToken: String? = ""

        NaverIdLoginSDK.authenticate(context, object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                naverToken = NaverIdLoginSDK.getAccessToken()
                Timber.tag("naverLogin").d("naverToken : $naverToken")
                if (naverToken.isNullOrEmpty()) {
                    continuation.resumeWithException(RuntimeException("Invalid Naver Access Token"))
                } else continuation.resume(naverToken!!)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Timber.tag("naverLogin").d("code : $errorCode, des : $errorDescription")
                continuation.resumeWithException(RuntimeException(errorDescription))
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        })
    }

    suspend fun executeLogout() = suspendCoroutine { continuation ->
        NaverIdLoginSDK.logout()
        continuation.resume(Unit)
    }

    suspend fun executeWithdraw() = suspendCoroutine { continuation ->
        NidOAuthLogin().callDeleteTokenApi(object : OAuthLoginCallback {
            override fun onSuccess() {
                Timber.d("Naver 연동해제 성공")
                continuation.resume(Unit)
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                continuation.resumeWithException(IOException(errorDescription))
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        })
    }

    suspend fun getAccessTokenWithRefresh(): String = suspendCoroutine { continuation ->
        NidOAuthLogin().callRefreshAccessTokenApi(object : OAuthLoginCallback {
            override fun onSuccess() {
                // 접근 토큰 갱신에 성공한 경우 수행할 코드 추가
                Timber.d("Naver Token Refresh 성공")
                val accessToken = NaverIdLoginSDK.getAccessToken()
                if (accessToken.isNullOrEmpty()) {
                    continuation.resumeWithException(IOException("Invalid Naver Access Token"))
                } else continuation.resume(accessToken)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                continuation.resumeWithException(IOException(errorDescription))
            }

            override fun onError(errorCode: Int, message: String) {
                Timber.d("errorCode : $errorCode, message: $message")
                onFailure(errorCode, message)
            }
        })
    }
}

//  백엔드에서 회원탈퇴 처리
//    suspend fun executeWithdraw() = suspendCoroutine { continuation ->
//        NidOAuthLogin().callDeleteTokenApi(object : OAuthLoginCallback {
//            override fun onSuccess() {
//                //서버에서 토큰 삭제에 성공한 상태입니다.
//                Timber.d("naverLogin Withdraw success")
//                continuation.resume(Unit)
//            }
//
//            override fun onFailure(httpStatus: Int, message: String) {
//                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
//                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
//                Timber.e("errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
//                Timber.e("errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
//                continuation.resumeWithException(RuntimeException(NaverIdLoginSDK.getLastErrorDescription()))
//            }
//
//            override fun onError(errorCode: Int, message: String) {
//                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
//                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
//                onFailure(errorCode, message)
//                Timber.e("errorCode: $errorCode")
//                Timber.e("errorDesc: $message")
//            }
//        })
//
//    }
