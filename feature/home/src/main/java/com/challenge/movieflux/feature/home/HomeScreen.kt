package com.challenge.movieflux.feature.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.challenge.movieflux.core.designsystem.icon.MovieFluxIcons
import com.challenge.movieflux.core.model.data.Movie
import com.challenge.movieflux.core.designsystem.component.MovieCard
import com.challenge.movieflux.core.designsystem.theme.MovieFluxTheme

@Composable
internal fun HomeRoute(
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    HomeScreen(
        uiState = uiState,
        searchQuery = searchQuery,
        onSearchQueryChange = viewModel::onSearchQueryChanged,
        onMovieClick = onMovieClick,
        onToggleFavorite = viewModel::toggleFavorite,
        onLoadMore = viewModel::loadMore,
        modifier = modifier
    )
}

@Composable
internal fun HomeScreen(
    uiState: HomeUiState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onMovieClick: (Int) -> Unit,
    onToggleFavorite: (Movie) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        SearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        when (uiState) {
            HomeUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is HomeUiState.Success -> {
                MovieGrid(
                    movies = uiState.movies,
                    onMovieClick = onMovieClick,
                    onToggleFavorite = onToggleFavorite,
                    onLoadMore = onLoadMore,
                    canLoadMore = uiState.canLoadMore
                )
            }
            is HomeUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = uiState.message,
                        color = MovieFluxTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            HomeUiState.Empty -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Nenhum filme encontrado",
                        style = MovieFluxTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text("Buscar filmes...") },
        leadingIcon = { Icon(MovieFluxIcons.Search, contentDescription = null) },
        singleLine = true
    )
}

@Composable
fun MovieGrid(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit,
    onToggleFavorite: (Movie) -> Unit,
    onLoadMore: () -> Unit,
    canLoadMore: Boolean
) {
    val listState = rememberLazyGridState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val totalItemsCount = listState.layoutInfo.totalItemsCount
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            
            canLoadMore && totalItemsCount > 0 && lastVisibleItemIndex >= totalItemsCount - 4
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = listState,
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(movies) { index, movie ->
            MovieCard(
                title = movie.title,
                posterPath = movie.posterPath,
                isFavorite = movie.isFavorite,
                onMovieClick = { onMovieClick(movie.id) },
                onToggleFavorite = { onToggleFavorite(movie) }
            )
        }
        
        if (canLoadMore) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
