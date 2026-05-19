package com.challenge.movieflux.core.data.repository

import com.challenge.movieflux.core.domain.movie.repository.MovieRepository
import com.challenge.movieflux.core.model.data.GenreResponse
import com.challenge.movieflux.core.model.data.MovieDetailResponse
import com.challenge.movieflux.core.model.data.PopularMoviesResponse
import com.challenge.movieflux.core.network.mapper.PopularMoviesResponseMapper
import com.challenge.movieflux.core.network.retrofit.RetrofitMovieFluxNetworkApi
import com.challenge.movieflux.core.common.results.BaseResult
import com.challenge.movieflux.core.data.utils.NetworkBoundResource
import com.challenge.movieflux.core.network.mapper.GenreResponseMapper
import com.challenge.movieflux.core.network.mapper.MovieDetailResponseMapper
import com.challenge.movieflux.core.network.utils.mapFromApiResponse
import com.challenge.movieflux.core.database.dao.FavoriteDao
import com.challenge.movieflux.core.database.model.asEntity
import com.challenge.movieflux.core.database.model.asExternalModel
import com.challenge.movieflux.core.model.data.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val networkApi: RetrofitMovieFluxNetworkApi,
    private val favoriteDao: FavoriteDao,
    private val networkBoundResources: NetworkBoundResource,
    private val popularMoviesMapper: PopularMoviesResponseMapper,
    private val movieDetailMapper: MovieDetailResponseMapper,
    private val genreResponseMapper: GenreResponseMapper
) : MovieRepository {


    override suspend fun getPopularMovies(page: Int): Flow<BaseResult<PopularMoviesResponse>> {
        return mapFromApiResponse(
            baseResult = networkBoundResources.downloadData {
                networkApi.getPopularMovies(page)
            }, popularMoviesMapper
        )
    }

    override suspend fun searchMovies(query: String, page: Int): Flow<BaseResult<PopularMoviesResponse>> {
        return mapFromApiResponse(
            baseResult = networkBoundResources.downloadData {
                networkApi.searchMovies(query,page)
            }, popularMoviesMapper
        )
    }

    override suspend fun getMovieDetails(movieId: Int): Flow<BaseResult<MovieDetailResponse>> {
        return mapFromApiResponse(
            baseResult = networkBoundResources.downloadData {
                networkApi.getMovieDetails(movieId)
            }, movieDetailMapper
        )
    }

    override suspend fun getGenres(): Flow<BaseResult<GenreResponse>> {
        return mapFromApiResponse(
            baseResult = networkBoundResources.downloadData {
                networkApi.getGenres()
            }, genreResponseMapper
        )
    }

    override fun getFavoriteMovies(): Flow<List<Movie>> {
        return favoriteDao.getFavoriteMovies().map { entities ->
            entities.map { it.asExternalModel() }
        }
    }

    override fun getFavoriteMovieIds(): Flow<List<Int>> {
        return favoriteDao.getFavoriteMovieIds()
    }

    override suspend fun addFavoriteMovie(movie: Movie) {
        favoriteDao.insertFavoriteMovie(movie.asEntity())
    }

    override suspend fun removeFavoriteMovie(movie: Movie) {
        favoriteDao.deleteFavoriteMovie(movie.asEntity())
    }

    override fun isMovieFavorite(movieId: Int): Flow<Boolean> {
        return favoriteDao.isMovieFavorite(movieId)
    }
}
