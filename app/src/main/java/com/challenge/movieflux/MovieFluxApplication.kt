package com.challenge.movieflux

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MovieFluxApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        Log.e("teste","teste")
    }
}