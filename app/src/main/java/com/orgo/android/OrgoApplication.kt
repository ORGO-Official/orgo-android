package com.orgo.android

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class OrgoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        NaverIdLoginSDK.initialize(
            this,
            BuildConfig.NAVER_CLIENT_ID,
            BuildConfig.NAVER_CLIENT_SECRET,
            BuildConfig.NAVER_CLIENT_NAME
        )
        KakaoSdk.init(this,  BuildConfig.KAKAO_NATIVE_APP_KEY)
    }

}

