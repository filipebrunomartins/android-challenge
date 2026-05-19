package com.challenge.movieflux.core.domain.movie.repository

import com.challenge.movieflux.core.common.results.BaseResult
import com.challenge.movieflux.core.model.data.GenreResponse
import com.challenge.movieflux.core.model.data.Movie
import com.challenge.movieflux.core.model.data.MovieDetailResponse
import com.challenge.movieflux.core.model.data.PopularMoviesResponse
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getPopularMovies(page: Int): Flow<BaseResult<PopularMoviesResponse>>
    suspend fun searchMovies(query: String, page: Int): Flow<BaseResult<PopularMoviesResponse>>
    suspend fun getMovieDetails(movieId: Int): Flow<BaseResult<MovieDetailResponse>>
    suspend fun getGenres(): Flow<BaseResult<GenreResponse>>

    //Todo rever
    fun getFavoriteMovies(): Flow<List<Movie>>
    fun getFavoriteMovieIds(): Flow<List<Int>>
    suspend fun addFavoriteMovie(movie: Movie)
    suspend fun removeFavoriteMovie(movie: Movie)
    fun isMovieFavorite(movieId: Int): Flow<Boolean>
}
