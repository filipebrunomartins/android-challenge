package com.challenge.movieflux.core.database.di

import android.content.Context
import androidx.room.Room
import com.challenge.movieflux.core.database.MovieFluxDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesMovieFluxDatabase(
        @ApplicationContext context: Context,
    ): MovieFluxDatabase = Room.databaseBuilder(
        context,
        MovieFluxDatabase::class.java,
        "movieflux-database",
    ).build()
}
