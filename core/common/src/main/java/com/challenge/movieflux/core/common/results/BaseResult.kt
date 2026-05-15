package com.challenge.movieflux.core.common.results

sealed interface BaseResult<out R> {
    data object Loading:BaseResult<Nothing>
    data class Success<out T>(val data: T) : BaseResult<T>
    data class Error<out T>(val message: String,val code:Int) : BaseResult<T>
}