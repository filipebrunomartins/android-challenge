package com.challenge.movieflux.core.data.di

import android.content.Context
import com.challenge.movieflux.core.data.repository.LoginRepositoryImpl
import com.challenge.movieflux.core.domain.login.repository.LoginRepository
import com.challenge.movieflux.core.security.SecurePrefs
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsLoginRepository(
        topicsRepository: LoginRepositoryImpl,
    ): LoginRepository

    //Todo repensar local
    @Provides
    fun provideSecurePrefs(
        @ApplicationContext context: Context
    ): SecurePrefs {
        return SecurePrefs(context)
    }
}