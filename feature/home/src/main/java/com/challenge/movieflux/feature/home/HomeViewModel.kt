package com.challenge.movieflux.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.movieflux.core.domain.movie.usecase.GetPopularMoviesUseCase
import com.challenge.movieflux.core.domain.movie.usecase.SearchMoviesUseCase
import com.challenge.movieflux.core.model.data.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val movies: List<Movie>,
        val isSearching: Boolean = false,
        val canLoadMore: Boolean = true
    ) : HomeUiState
    data class Error(val message: String) : HomeUiState
    data object Empty : HomeUiState
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var currentPage = 1
    private var isFetching = false
    private val allMovies = mutableListOf<Movie>()

    init {
        loadPopularMovies()
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
                    if (query.isBlank()) {
                        resetAndFetchPopular()
                    } else {
                        searchMovies(query)
                    }
                }
        }
    }

    private fun resetAndFetchPopular() {
        currentPage = 1
        allMovies.clear()
        loadPopularMovies()
    }

    fun loadPopularMovies() {
        if (isFetching) return
        isFetching = true

        viewModelScope.launch {
//            getPopularMoviesUseCase(currentPage)
//                .onStart {
//                    if (currentPage == 1) _uiState.value = HomeUiState.Loading
//                }
//                .catch { e ->
//                    _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
//                    isFetching = false
//                }
//                .collect { movies ->
//                    if (movies.isEmpty() && currentPage == 1) {
//                        _uiState.value = HomeUiState.Empty
//                    } else {
//                        allMovies.addAll(movies)
//                        _uiState.value = HomeUiState.Success(
//                            movies = allMovies.toList(),
//                            canLoadMore = movies.isNotEmpty()
//                        )
//                        currentPage++
//                    }
//                    isFetching = false
//                }
        }
    }

    private fun searchMovies(query: String) {
        currentPage = 1
        allMovies.clear()
        isFetching = true

        viewModelScope.launch {
//            searchMoviesUseCase(query, currentPage)
//                .onStart {
//                    _uiState.value = HomeUiState.Loading
//                }
//                .catch { e ->
//                    _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
//                    isFetching = false
//                }
//                .collect { movies ->
//                    if (movies.isEmpty()) {
//                        _uiState.value = HomeUiState.Empty
//                    } else {
//                        allMovies.addAll(movies)
//                        _uiState.value = HomeUiState.Success(
//                            movies = allMovies.toList(),
//                            isSearching = true,
//                            canLoadMore = movies.isNotEmpty()
//                        )
//                        currentPage++
//                    }
//                    isFetching = false
//                }
        }
    }

    fun loadMore() {
        val currentQuery = _searchQuery.value
        if (currentQuery.isBlank()) {
            loadPopularMovies()
        } else {
            // Logic for paginated search if needed
            loadMoreSearch(currentQuery)
        }
    }

    private fun loadMoreSearch(query: String) {
        if (isFetching) return
        isFetching = true

        viewModelScope.launch {
//            searchMoviesUseCase(query, currentPage)
//                .catch { e ->
//                    // Handle error silently or update state
//                    isFetching = false
//                }
//                .collect { movies ->
//                    if (movies.isNotEmpty()) {
//                        allMovies.addAll(movies)
//                        _uiState.value = HomeUiState.Success(
//                            movies = allMovies.toList(),
//                            isSearching = true,
//                            canLoadMore = true
//                        )
//                        currentPage++
//                    } else {
//                        _uiState.value = (uiState.value as? HomeUiState.Success)?.copy(canLoadMore = false) ?: uiState.value
//                    }
//                    isFetching = false
//                }
        }
    }
}
