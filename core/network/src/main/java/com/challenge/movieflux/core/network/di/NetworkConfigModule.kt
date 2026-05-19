package com.challenge.movieflux.core.network.di

import com.challenge.movieflux.core.network.BuildConfig
import com.challenge.movieflux.core.network.qualifier.AppBaseUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkConfigModule {

    @Provides
    @Singleton
    @AppBaseUrl
    fun provideBaseUrl(): String {
        return BuildConfig.TMDB_BASE_URL
    }
}