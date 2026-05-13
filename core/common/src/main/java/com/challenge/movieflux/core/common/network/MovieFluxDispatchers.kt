package com.challenge.movieflux.core.common.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val dispatcher: MovieFluxDispatchers)

enum class MovieFluxDispatchers {
    Default,
    IO,
    Main
}