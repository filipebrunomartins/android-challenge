package com.challenge.movieflux.core.data.di

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.Window
import com.challenge.movieflux.core.data.repository.LoginRepositoryImpl
import com.challenge.movieflux.core.domain.login.repository.LoginRepository
import com.challenge.movieflux.core.security.SecurePrefs
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsLoginRepository(
        impl: LoginRepositoryImpl,
    ): LoginRepository
}

//Todo remover daqui
//@Module
//@InstallIn(SingletonComponent::class)
//object SecurityModule {
//
//    @Provides
//    fun provideSecurePrefs(
//        @ApplicationContext context: Context
//    ): SecurePrefs {
//        return SecurePrefs(context)
//    }
//}