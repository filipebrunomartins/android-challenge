package com.challenge.movieflux.core.data.repository

import com.challenge.movieflux.core.domain.movie.repository.MovieRepository
import com.challenge.movieflux.core.model.data.Movie
import com.challenge.movieflux.core.model.data.PopularMoviesResponse
import com.challenge.movieflux.core.network.mapper.PopularMoviesResponseMapper
import com.challenge.movieflux.core.network.retrofit.RetrofitMovieFluxNetworkApi
import com.challenge.movieflux.core.common.results.BaseResult
import com.challenge.movieflux.core.data.utils.NetworkBoundResource
import com.challenge.movieflux.core.network.mapper.MovieMapper
import com.challenge.movieflux.core.network.utils.mapFromApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val networkApi: RetrofitMovieFluxNetworkApi,
    private val networkBoundResources: NetworkBoundResource,
    private val popularMoviesMapper: PopularMoviesResponseMapper
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
}
