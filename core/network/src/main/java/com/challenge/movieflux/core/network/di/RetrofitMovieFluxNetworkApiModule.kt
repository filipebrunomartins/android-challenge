package com.challenge.movieflux.core.network.di

import com.challenge.movieflux.core.network.qualifier.AppBaseUrl
import com.challenge.movieflux.core.network.retrofit.RetrofitMovieFluxNetworkApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import retrofit2.Retrofit
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {
    @Provides
    @Singleton
    fun provideApiService(@AppBaseUrl retrofit: Retrofit): RetrofitMovieFluxNetworkApi {
        return retrofit.create(RetrofitMovieFluxNetworkApi::class.java)
    }
}