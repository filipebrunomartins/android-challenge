package com.challenge.movieflux.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.movieflux.core.common.results.BaseResult
import com.challenge.movieflux.core.domain.movie.usecase.GetPopularMoviesParams
import com.challenge.movieflux.core.domain.movie.usecase.GetPopularMoviesUseCase
import com.challenge.movieflux.core.domain.movie.usecase.SearchMoviesParams
import com.challenge.movieflux.core.domain.movie.usecase.SearchMoviesUseCase
import com.challenge.movieflux.core.model.data.Movie
import com.challenge.movieflux.core.model.data.PopularMoviesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
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
                    Log.d("HomeViewModel", "Query changed: $query, resetting pagination")
                    currentPage = 1
                    allMovies.clear()
                    isFetching = false
                    fetchMovies(query)
                }
        }
    }

    private fun fetchMovies(query: String) {
        if (isFetching && currentPage > 1) {
            Log.d("HomeViewModel", "Already fetching page $currentPage, skipping")
            return
        }
        
        fetchJob?.cancel()
        isFetching = true
        Log.d("HomeViewModel", "Fetching page $currentPage for query: '$query'")
        
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
                if (currentPage == 1 && allMovies.isEmpty()) {
                    _uiState.value = HomeUiState.Loading
                }
            }

            is BaseResult.Success -> {
                isFetching = false
                val newMovies = response.data.results
                allMovies.addAll(newMovies)
                
                Log.d("HomeViewModel", "Success! Loaded ${newMovies.size} movies. Total: ${allMovies.size}")

                if (allMovies.isEmpty()) {
                    _uiState.value = HomeUiState.Empty
                } else {
                    val canLoadMore = response.data.page < response.data.totalPages
                    _uiState.value = HomeUiState.Success(
                        movies = allMovies.toList(),
                        isSearching = _searchQuery.value.isNotEmpty(),
                        canLoadMore = canLoadMore
                    )
                    if (canLoadMore) {
                        currentPage = response.data.page + 1
                    }
                }
            }

            is BaseResult.Error -> {
                isFetching = false
                Log.e("HomeViewModel", "Error fetching page $currentPage: ${response.message}")
                if (allMovies.isEmpty()) {
                    _uiState.value = HomeUiState.Error(response.message)
                } else {
                    _uiState.update { state ->
                        if (state is HomeUiState.Success) {
                            state.copy(canLoadMore = false)
                        } else state
                    }
                }
            }
        }
    }

    fun loadMore() {
        if (isFetching) return
        
        val state = _uiState.value
        if (state is HomeUiState.Success && state.canLoadMore) {
             Log.d("HomeViewModel", "loadMore triggered")
             fetchMovies(_searchQuery.value)
        }
    }
}
