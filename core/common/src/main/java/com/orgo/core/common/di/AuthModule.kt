package com.orgo.core.common.di

import com.orgo.core.common.auth.KakaoAuthManager
import com.orgo.core.common.auth.NaverAuthManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthManagerModule {

    /*
     // TODO : Kakao, Naver AuthManager를 굳이 Singleton으로 유지할 필요가 있는가?
     애초에 Hilt로 의존성 주입을 해주는게 맞나? object를 사용해서 그냥 인스턴스 하나로 사용해도 될 것 같은데
     ViewScope이나 ViewModelScope으로도 충분한 것 같은데,,
     */

    @Singleton
    @Provides
    fun provideKakaoAuthManager() : KakaoAuthManager {
        return KakaoAuthManager()
    }

    @Singleton
    @Provides
    fun provideNaverAuthManager() : NaverAuthManager {
        return NaverAuthManager()
    }

}