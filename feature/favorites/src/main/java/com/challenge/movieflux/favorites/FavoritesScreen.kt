package com.challenge.movieflux.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.challenge.movieflux.core.designsystem.component.MovieCard
import com.challenge.movieflux.core.designsystem.theme.MovieFluxTheme
import com.challenge.movieflux.core.model.data.Movie

@Composable
internal fun FavoritesRoute(
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    FavoritesScreen(
        uiState = uiState,
        onMovieClick = onMovieClick,
        onToggleFavorite = viewModel::toggleFavorite,
        modifier = modifier
    )
}

@Composable
internal fun FavoritesScreen(
    uiState: FavoritesUiState,
    onMovieClick: (Int) -> Unit,
    onToggleFavorite: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Meus Favoritos",
            style = MovieFluxTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        when (uiState) {
            FavoritesUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is FavoritesUiState.Success -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.movies, key = { it.id }) { movie ->
                        MovieCard(
                            title = movie.title,
                            posterPath = movie.posterPath,
                            isFavorite = movie.isFavorite,
                            onMovieClick = { onMovieClick(movie.id) },
                            onToggleFavorite = { onToggleFavorite(movie) }
                        )
                    }
                }
            }
            FavoritesUiState.Empty -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Você ainda não favoritou nenhum filme.",
                        style = MovieFluxTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(32.dp)
                    )
                }
            }
        }
    }
}
