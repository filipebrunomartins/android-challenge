package com.challenge.movieflux.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.movieflux.core.common.results.BaseResult
import com.challenge.movieflux.core.domain.movie.usecase.GetPopularMoviesParams
import com.challenge.movieflux.core.domain.movie.usecase.GetPopularMoviesUseCase
import com.challenge.movieflux.core.domain.movie.usecase.SearchMoviesParams
import com.challenge.movieflux.core.domain.movie.usecase.SearchMoviesUseCase
import com.challenge.movieflux.core.domain.movie.usecase.GetFavoriteMovieIdsUseCase
import com.challenge.movieflux.core.domain.movie.usecase.ToggleFavoriteUseCase
import com.challenge.movieflux.core.model.data.Movie
import com.challenge.movieflux.core.model.data.PopularMoviesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val movies: List<Movie>,
        val isSearching: Boolean = false,
        val canLoadMore: Boolean = true,
        val isPaginationError: Boolean = false
    ) : HomeUiState

    data class Error(val message: String) : HomeUiState
    data object Empty : HomeUiState
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val getFavoriteMovieIdsUseCase: GetFavoriteMovieIdsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)
    private val _canLoadMore = MutableStateFlow(true)
    private val _isPaginationError = MutableStateFlow(false)

    val uiState: StateFlow<HomeUiState> = combine(
        _movies,
        getFavoriteMovieIdsUseCase(),
        _searchQuery,
        combine(_isLoading, _error, _canLoadMore, _isPaginationError) { isLoading, error, canLoadMore, isPaginationError ->
            NetworkState(isLoading, error, canLoadMore, isPaginationError)
        }
    ) { movies, favoriteIds, query, networkState ->
        val (isLoading, error, canLoadMore, isPaginationError) = networkState
        val updatedMovies = movies.map { movie ->
            movie.copy(isFavorite = favoriteIds.contains(movie.id))
        }

        when {
            isLoading && updatedMovies.isEmpty() -> HomeUiState.Loading
            error != null && updatedMovies.isEmpty() -> HomeUiState.Error(error)
            updatedMovies.isEmpty() && !isLoading -> HomeUiState.Empty
            else -> HomeUiState.Success(
                movies = updatedMovies,
                isSearching = query.isNotEmpty(),
                canLoadMore = canLoadMore,
                isPaginationError = isPaginationError
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState.Loading
    )

    private var currentPage = 1
    private var isFetching = false
    private var fetchJob: Job? = null
    private val allMovies = mutableListOf<Movie>()

    init {
        observeSearchQuery()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (allMovies.isNotEmpty() && query == lastQuery) {
                        return@collectLatest
                    }
                    lastQuery = query
                    currentPage = 1
                    allMovies.clear()
                    _movies.value = emptyList()
                    isFetching = false
                    _error.value = null
                    fetchMovies(query)
                }
        }
    }

    private var lastQuery: String? = ""

    private fun fetchMovies(query: String) {
        if (isFetching && currentPage > 1) {
            return
        }
        
        fetchJob?.cancel()
        isFetching = true
        _isLoading.value = true

        fetchJob = viewModelScope.launch {
            val flow = if (query.isBlank()) {
                getPopularMoviesUseCase.execute(GetPopularMoviesParams(currentPage))
            } else {
                searchMoviesUseCase.execute(SearchMoviesParams(query, currentPage))
            }

            flow.collect { response ->
                handleResponse(response)
            }
        }
    }

    private fun handleResponse(response: BaseResult<PopularMoviesResponse>) {
        when (response) {
            is BaseResult.Loading -> {
                if (allMovies.isEmpty()) {
                    _isLoading.value = true
                }
            }

            is BaseResult.Success -> {
                isFetching = false
                _isLoading.value = false
                _error.value = null
                _isPaginationError.value = false
                val newMovies = response.data.results
                allMovies.addAll(newMovies)
                _movies.value = allMovies.toList()

                val canLoadMore = response.data.page < response.data.totalPages
                _canLoadMore.value = canLoadMore
                if (canLoadMore) {
                    currentPage = response.data.page + 1
                }
            }

            is BaseResult.Error -> {
                isFetching = false
                _isLoading.value = false
                if (allMovies.isEmpty()) {
                    _error.value = response.message
                } else {
                    _isPaginationError.value = true
                }
            }
        }
    }

    fun retry() {
        if (_error.value != null) {
            _error.value = null
            fetchMovies(_searchQuery.value)
        } else if (_isPaginationError.value) {
            _isPaginationError.value = false
            fetchMovies(_searchQuery.value)
        }
    }

    fun loadMore() {
        if (isFetching || _isPaginationError.value) return
        
        if (_canLoadMore.value) {
             fetchMovies(_searchQuery.value)
        }
    }

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            toggleFavoriteUseCase(movie)
        }
    }
}

private data class NetworkState(
    val isLoading: Boolean,
    val error: String?,
    val canLoadMore: Boolean,
    val isPaginationError: Boolean
)
