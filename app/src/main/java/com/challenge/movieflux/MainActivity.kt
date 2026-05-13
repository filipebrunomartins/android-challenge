package com.challenge.movieflux

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.challenge.movieflux.core.designsystem.theme.MovieFluxTheme
import com.challenge.movieflux.navigation.MovieFluxNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MovieFluxTheme {
                MovieFluxNavHost()
            }
        }
    }
}