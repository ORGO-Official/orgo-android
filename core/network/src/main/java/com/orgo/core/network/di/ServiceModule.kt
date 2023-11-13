package com.orgo.core.network.di

import com.orgo.core.network.service.BadgeService
import com.orgo.core.network.service.MountainService
import com.orgo.core.network.service.UserAuthService
import com.orgo.core.network.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideUserAuthService(
        @Named("default")retrofit: Retrofit
    ): UserAuthService =retrofit.create(UserAuthService::class.java)

    @Provides
    @Singleton
    fun provideMountainService(
        @Named("default")retrofit: Retrofit
    ): MountainService = retrofit.create(MountainService::class.java)

    @Provides
    @Singleton
    fun provideUserService(
        @Named("token")retrofit: Retrofit
    ): UserService  = retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun provideBadgeService(
        @Named("token")retrofit: Retrofit
    ): BadgeService = retrofit.create(BadgeService::class.java)



}
