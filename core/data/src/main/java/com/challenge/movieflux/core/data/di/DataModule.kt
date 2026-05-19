package com.challenge.movieflux.core.data.di

import com.challenge.movieflux.core.data.repository.LoginRepositoryImpl
import com.challenge.movieflux.core.data.repository.MovieRepositoryImpl
import com.challenge.movieflux.core.domain.login.repository.LoginRepository
import com.challenge.movieflux.core.domain.movie.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindsLoginRepository(
        impl: LoginRepositoryImpl,
    ): LoginRepository

    @Binds
    internal abstract fun bindsMovieRepository(
        impl: MovieRepositoryImpl,
    ): MovieRepository
}