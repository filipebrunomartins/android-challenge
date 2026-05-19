package com.challenge.movieflux.core.network.retrofit

import com.challenge.movieflux.core.network.model.NetworkGenreResponse
import com.challenge.movieflux.core.network.model.NetworkMovieDetailResponse
import com.challenge.movieflux.core.network.model.NetworkPopularMoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitMovieFluxNetworkApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int
    ): Response<NetworkPopularMoviesResponse>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<NetworkPopularMoviesResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int
    ): Response<NetworkMovieDetailResponse>

    @GET("genre/movie/list")
    suspend fun getGenres(): Response<NetworkGenreResponse>
}