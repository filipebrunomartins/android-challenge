package com.challenge.movieflux.core.common.network.di

import com.challenge.movieflux.core.common.network.Dispatcher
import com.challenge.movieflux.core.common.network.MovieFluxDispatchers.IO
import com.challenge.movieflux.core.common.network.MovieFluxDispatchers.Default
import com.challenge.movieflux.core.common.network.MovieFluxDispatchers.Main
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    @Dispatcher(IO)
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Dispatcher(Default)
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Dispatcher(Main)
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}