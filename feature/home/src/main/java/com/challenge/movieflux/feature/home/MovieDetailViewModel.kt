package com.challenge.movieflux.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.movieflux.core.common.results.BaseResult
import com.challenge.movieflux.core.domain.movie.usecase.GetGenresUseCase
import com.challenge.movieflux.core.domain.movie.usecase.GetMovieDetailParams
import com.challenge.movieflux.core.domain.movie.usecase.GetMovieDetailUseCase
import com.challenge.movieflux.core.domain.movie.usecase.IsMovieFavoriteUseCase
import com.challenge.movieflux.core.domain.movie.usecase.ToggleFavoriteUseCase
import com.challenge.movieflux.core.model.data.MovieDetailResponse
import com.challenge.movieflux.core.model.data.asMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MovieDetailUiState {
    data object Loading : MovieDetailUiState
    data class Success(
        val movieDetail: MovieDetailResponse,
        val isFavorite: Boolean = false
    ) : MovieDetailUiState
    data class Error(val message: String) : MovieDetailUiState
}

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getGenresUseCase: GetGenresUseCase,
    private val isMovieFavoriteUseCase: IsMovieFavoriteUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"])

    private val _uiState = MutableStateFlow<MovieDetailUiState>(MovieDetailUiState.Loading)
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    init {
        fetchMovieDetail()
        observeFavoriteStatus()
    }

    private fun observeFavoriteStatus() {
        viewModelScope.launch {
            isMovieFavoriteUseCase(movieId).collect { isFavorite ->
                _uiState.update { state ->
                    if (state is MovieDetailUiState.Success) {
                        state.copy(isFavorite = isFavorite)
                    } else state
                }
            }
        }
    }

    private fun fetchMovieDetail() {
        viewModelScope.launch {
            getMovieDetailUseCase.execute(GetMovieDetailParams(movieId)).collect { result ->
                when (result) {
                    is BaseResult.Loading -> _uiState.value = MovieDetailUiState.Loading
                    is BaseResult.Success -> {
                        val movie = result.data
                        if (movie.genres.isEmpty() || movie.genres.all { it.name.isEmpty() }) {
                            fetchGenresAndMap(movie)
                        } else {
                            _uiState.update { 
                                MovieDetailUiState.Success(movieDetail = movie)
                            }
                        }
                    }
                    is BaseResult.Error -> _uiState.value = MovieDetailUiState.Error(result.message)
                }
            }
        }
    }

    private fun fetchGenresAndMap(movie: MovieDetailResponse) {
        viewModelScope.launch {
            getGenresUseCase.execute().collect { result ->
                if (result is BaseResult.Success) {
                    val genreMap = result.data.genres.associateBy { it.id }
                    val updatedGenres = movie.genres.map { genre ->
                        genreMap[genre.id] ?: genre
                    }
                    _uiState.update {
                        MovieDetailUiState.Success(movieDetail = movie.copy(genres = updatedGenres))
                    }
                } else if (result is BaseResult.Error) {
                    _uiState.update {
                        MovieDetailUiState.Success(movieDetail = movie)
                    }
                }
            }
        }
    }

    fun toggleFavorite() {
        val state = _uiState.value
        if (state is MovieDetailUiState.Success) {
            viewModelScope.launch {
                toggleFavoriteUseCase(state.movieDetail.asMovie())
            }
        }
    }
}
