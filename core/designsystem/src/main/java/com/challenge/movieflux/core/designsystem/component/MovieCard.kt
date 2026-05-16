package com.challenge.movieflux.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.challenge.movieflux.core.designsystem.theme.MovieFluxTheme

@Composable
fun MovieCard(
    title: String,
    posterPath: String?,
    isFavorite: Boolean,
    onMovieClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onMovieClick)
    ) {
        Column {
            Box {
                val imageUrl = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f / 3f),
                    contentScale = ContentScale.Crop,
                )
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                        tint = if (isFavorite) Color.Red else Color.White
                    )
                }
            }
            Text(
                text = title,
                style = MovieFluxTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
