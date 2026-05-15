package com.challenge.movieflux.core.network.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.challenge.movieflux.core.common.results.BaseResult

interface Mapper<R,E>{
    fun mapFromApiResponse(type:R):E
}

fun<R,E> mapFromApiResponse(baseResult: Flow<BaseResult<R>>, mapper: Mapper<R, E>): Flow<BaseResult<E>> {
    return baseResult.map {
        when(it){
            is BaseResult.Success-> BaseResult.Success(mapper.mapFromApiResponse(it.data))
            is BaseResult.Error->BaseResult.Error(it.message,it.code)
            is BaseResult.Loading -> BaseResult.Loading
        }
    }
}