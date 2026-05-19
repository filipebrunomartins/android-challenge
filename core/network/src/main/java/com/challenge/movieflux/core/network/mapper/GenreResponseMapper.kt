package com.challenge.movieflux.core.network.mapper

import com.challenge.movieflux.core.model.data.Genre
import com.challenge.movieflux.core.model.data.GenreResponse
import com.challenge.movieflux.core.network.model.NetworkGenre
import com.challenge.movieflux.core.network.model.NetworkGenreResponse
import com.challenge.movieflux.core.network.utils.Mapper
import javax.inject.Inject

class GenreResponseMapper @Inject constructor(
    private val genresMapper: GenreMapper
) : Mapper<NetworkGenreResponse, GenreResponse> {

    override fun mapFromApiResponse(type: NetworkGenreResponse): GenreResponse {
        return  GenreResponse(
            genres = type.genres.map { genresMapper.mapFromApiResponse(it) },
        )
    }
}

class GenreMapper @Inject constructor() : Mapper<NetworkGenre, Genre> {

    override fun mapFromApiResponse(type: NetworkGenre): Genre {
        return  Genre(
            id = type.id,
            name = type.name
        )
    }
}