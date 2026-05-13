package com.challenge.movieflux

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MovieFluxApplication : Application(){
    override fun onCreate() {
        super.onCreate()
    }
}