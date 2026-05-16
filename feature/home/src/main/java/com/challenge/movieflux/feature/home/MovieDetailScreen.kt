package com.challenge.movieflux.feature.home

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.challenge.movieflux.core.designsystem.icon.MovieFluxIcons
import com.challenge.movieflux.core.model.data.MovieDetailResponse

@Composable
internal fun MovieDetailRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    MovieDetailScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onFavoriteClick = viewModel::toggleFavorite,
        onShareClick = { movie ->
            shareMovie(context, movie)
        },
        modifier = modifier
    )
}

@Composable
internal fun MovieDetailScreen(
    uiState: MovieDetailUiState,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClick: (MovieDetailResponse) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            MovieDetailUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is MovieDetailUiState.Success -> {
                MovieDetailContent(
                    movie = uiState.movieDetail,
                    isFavorite = uiState.isFavorite,
                    onBackClick = onBackClick,
                    onFavoriteClick = onFavoriteClick,
                    onShareClick = onShareClick
                )
            }
            is MovieDetailUiState.Error -> {
                Text(
                    text = uiState.message,
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun MovieDetailContent(
    movie: MovieDetailResponse,
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClick: (MovieDetailResponse) -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Box {
            val imageUrl = movie.backdropPath?.let { "https://image.tmdb.org/t/p/w780$it" }
                ?: movie.posterPath?.let { "https://image.tmdb.org/t/p/w780$it" }
            
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                contentScale = ContentScale.Crop
            )
            
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = MovieFluxIcons.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = if (isFavorite) MovieFluxIcons.Favorite else MovieFluxIcons.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                    )
                }
                
                IconButton(onClick = { onShareClick(movie) }) {
                    Icon(
                        imageVector = MovieFluxIcons.Share,
                        contentDescription = "Share"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = MovieFluxIcons.StarRate,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = String.format("%.1f", movie.voteAverage),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(16.dp))
                movie.releaseDate?.let {
                    if (it.length >= 4) {
                        Text(
                            text = it.take(4),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Gêneros",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = movie.genres.joinToString { it.name },
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Sinopse",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = movie.overview,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun shareMovie(context: Context, movie: MovieDetailResponse) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, movie.title)
        putExtra(Intent.EXTRA_TEXT, "Confira este filme: ${movie.title}\n\n${movie.overview}")
    }
    context.startActivity(Intent.createChooser(intent, "Compartilhar filme"))
}
