package com.challenge.movieflux.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.challenge.movieflux.core.database.dao.FavoriteDao
import com.challenge.movieflux.core.database.model.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = 1,
    exportSchema = false
)
internal abstract class MovieFluxDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}
