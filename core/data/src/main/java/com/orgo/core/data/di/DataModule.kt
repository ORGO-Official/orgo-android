package com.orgo.core.data.di

import com.orgo.core.data.repository.BadgeRepository
import com.orgo.core.data.repository.MountainRepository
import com.orgo.core.data.repository.UserAuthRepository
import com.orgo.core.data.repository.UserRepository
import com.orgo.core.data.repositoryImpl.BadgeRepositoryImpl
import com.orgo.core.data.repositoryImpl.MountainRepositoryImpl
import com.orgo.core.data.repositoryImpl.UserAuthRepositoryImpl
import com.orgo.core.data.repositoryImpl.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

    @Singleton
    @Binds
    fun bindUserAuthRepository (
        userAuthRepository: UserAuthRepositoryImpl
    ): UserAuthRepository

    @Singleton
    @Binds
    fun bindMountainRepository (
        mountainRepository: MountainRepositoryImpl
    ): MountainRepository

    @Singleton
    @Binds
    fun bindUserRepository (
        userRepository: UserRepositoryImpl
    ): UserRepository

    @Singleton
    @Binds
    fun bindBadgeRepository (
       badgeRepository: BadgeRepositoryImpl
    ): BadgeRepository

}