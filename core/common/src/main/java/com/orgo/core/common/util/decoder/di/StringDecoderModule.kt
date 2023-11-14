package com.orgo.core.common.util.decoder.di

import com.orgo.core.common.util.decoder.StringDecoder
import com.orgo.core.common.util.decoder.UriDecoder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class StringDecoderModule {
    @Binds
    abstract fun bindStringDecoder(uriDecoder: UriDecoder): StringDecoder
}
