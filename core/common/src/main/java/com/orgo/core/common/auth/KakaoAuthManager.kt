package com.orgo.core.common.auth

import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.AuthCodeClient
import com.kakao.sdk.auth.TokenManageable
import com.kakao.sdk.auth.TokenManager
import com.kakao.sdk.auth.TokenManagerProvider
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.AccessTokenInfo
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class KakaoAuthManager {

    suspend fun executeLogin(context : Context): OAuthToken
    = suspendCoroutine { continuation ->

        Timber.d("hasToken : ${AuthApiClient.instance.hasToken()}")
        Timber.d("refreshToken : ${ TokenManagerProvider.instance.manager.getToken()?.refreshToken}")
        TokenManagerProvider.instance.manager.clear()

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Timber.tag("kakaoLogin").e(error, "카카오계정으로 로그인 실패")
                continuation.resumeWithException(error)
            } else if (token != null) {
                Timber.tag("kakaoLogin").i("카카오계정으로 로그인 성공 token : $token")
                continuation.resume(token)
            }
        }
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Timber.tag("kakaoLogin").e(error, "카카오톡으로 로그인 실패")
                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        continuation.resumeWithException(error)
//                        return@loginWithKakaoTalk
                    }
                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    Timber.tag("kakaoLogin").i("카카오톡으로 로그인 성공 token : $token")
                    continuation.resume(token)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    fun getAccessToken() : String? {
        return TokenManager.instance.getToken()?.accessToken

    }

    suspend fun getAccessTokenWithRefresh() : String
    = suspendCoroutine{ continuation ->
        val oldToken = TokenManager.instance.getToken()
        Timber.d("oldToken : $oldToken")
        Timber.d("hasToken : ${AuthApiClient.instance.hasToken()}")
        if(AuthApiClient.instance.hasToken() && oldToken != null){
            AuthApiClient.instance.refreshToken{ token, error ->
                if(error != null || token == null){
                    Timber.e( "토큰 Refresh 실패", error)
                    continuation.resumeWithException(error
                        ?:IOException("An unknown KakaoSdk error occurred in refreshToken method")
                    )
                }
                else{
                    Timber.d("토큰 Refresh 성공 token : ${token}")
//                    TokenManager.instance.setToken(token)
                    continuation.resume(token.accessToken)
                }
            }
        }
    }
}

//            직접 인가코드 받아서 새로 토큰 발행하는 코드
//            AuthCodeClient.instance.authorizeWithKakaoTalk(context){ code, error ->
//                if (error != null ){
//                    Timber.tag("kakaoLogin").e(error, "카카오톡으로 authorize 실패")
//                }else if (code != null ){
//                    Timber.tag("kakaoLogin").i("카카오톡으로 로그인 성공 code : $code")
//                    AuthApiClient.instance.issueAccessToken(code){ token, error ->
//                        Timber.tag("kakaoLogin").i("카카오톡으로 로그인 성공 token : $token")
//                        if(token != null){
////                            TokenManagerProvider.instance.manager.clear()
//                            TokenManagerProvider.instance.manager.setToken(token)
//                            continuation.resume(token)
//                        }
//                    }
//                }
//            }


// 백엔드에서 로그아웃 처리하기 때문에 필요 없음
//    suspend fun executeLogout() = suspendCoroutine { continuation ->
//        Timber.d("hasToken : ${AuthApiClient.instance.hasToken()}")
//        Timber.d("accessToken : ${ TokenManagerProvider.instance.manager.getToken()?.accessToken}")
//        Timber.d("refreshToken : ${ TokenManagerProvider.instance.manager.getToken()?.refreshToken}")
//
//        UserApiClient.instance.accessTokenInfo{ accessToken, error ->
//            Timber.tag("kakaoLogin").d("accessToken :$accessToken")
//            Timber.tag("kakaoLogin").d("accessTokenInfo error :$error")
//        }

//        UserApiClient.instance.logout { error ->
//            if (error != null){
//                Timber.tag("kakaoLogin").e(error, "카카오톡으로 로그아웃 실패")
//                continuation.resumeWithException(error)
//            }else{
//                Timber.tag("kakaoLogin").d("카카오톡으로 로그아웃 성공")
//                continuation.resume(Unit)
//            }
//        }
//    }
//
//    suspend fun executeWithDraw() = suspendCoroutine { continuation ->
//        UserApiClient.instance.unlink { error ->
//            if (error != null){
//                Timber.tag("kakaoLogin").e(error, "카카오톡으로 탈퇴 실패")
//                continuation.resumeWithException(error)
//            }else{
//                Timber.tag("kakaoLogin").d("카카오톡으로 탈퇴 성공")
//                continuation.resume(Unit)
//            }
//        }
//    }