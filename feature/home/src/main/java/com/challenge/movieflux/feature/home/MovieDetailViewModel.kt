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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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

    private val _detailState = MutableStateFlow<MovieDetailUiState>(MovieDetailUiState.Loading)
    
    val uiState: StateFlow<MovieDetailUiState> = combine(
        _detailState,
        isMovieFavoriteUseCase(movieId)
    ) { state, isFavorite ->
        if (state is MovieDetailUiState.Success) {
            state.copy(isFavorite = isFavorite)
        } else state
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MovieDetailUiState.Loading
    )

    init {
        fetchMovieDetail()
    }

    private fun fetchMovieDetail() {
        viewModelScope.launch {
            getMovieDetailUseCase.execute(GetMovieDetailParams(movieId)).collect { result ->
                when (result) {
                    is BaseResult.Loading -> _detailState.value = MovieDetailUiState.Loading
                    is BaseResult.Success -> {
                        val movie = result.data
                        if (movie.genres.isEmpty() || movie.genres.all { it.name.isEmpty() }) {
                            fetchGenresAndMap(movie)
                        } else {
                            _detailState.update { 
                                MovieDetailUiState.Success(movieDetail = movie)
                            }
                        }
                    }
                    is BaseResult.Error -> _detailState.value = MovieDetailUiState.Error(result.message)
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
                    _detailState.update {
                        MovieDetailUiState.Success(movieDetail = movie.copy(genres = updatedGenres))
                    }
                } else if (result is BaseResult.Error) {
                    _detailState.update {
                        MovieDetailUiState.Success(movieDetail = movie)
                    }
                }
            }
        }
    }

    fun toggleFavorite() {
        val state = uiState.value
        if (state is MovieDetailUiState.Success) {
            viewModelScope.launch {
                toggleFavoriteUseCase(state.movieDetail.asMovie())
            }
        }
    }

    fun retry() {
        fetchMovieDetail()
    }
}
