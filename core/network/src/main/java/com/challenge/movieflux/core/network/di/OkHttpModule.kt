package com.challenge.movieflux.core.network.di

import android.util.Log
import com.challenge.movieflux.core.network.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object OkHttpModule {

    @Provides
    @Singleton
    fun provideLoggerInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { Log.d("OkHttp", it) }
            .apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggerInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {

        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggerInterceptor)
            .addInterceptor { chain ->

                val original = chain.request()
                val originalHttpUrl = original.url

                val url = originalHttpUrl.newBuilder()
                    .setQueryParameter("language", "pt-BR")
                    .build()

                val request = original.newBuilder()
                    .url(url)
                    .addHeader("Accept", "application/json")
                    .addHeader("Accept-Encoding", "identity")
                    .addHeader(
                        "Authorization",
                        "Bearer ${BuildConfig.TMDB_TOKEN}"
                    )
                    .build()

                chain.proceed(request)
            }
            .build()
    }
}