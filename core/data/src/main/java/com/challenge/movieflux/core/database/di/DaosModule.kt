package com.challenge.movieflux.core.database.di

import com.challenge.movieflux.core.database.MovieFluxDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun providesFavoriteDao(
        database: MovieFluxDatabase,
    ) = database.favoriteDao()
}