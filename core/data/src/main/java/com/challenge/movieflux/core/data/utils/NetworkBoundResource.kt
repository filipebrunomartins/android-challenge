package com.challenge.movieflux.core.data.utils

import com.challenge.movieflux.core.common.network.Dispatcher
import com.challenge.movieflux.core.common.network.MovieFluxDispatchers.IO
import com.challenge.movieflux.core.common.results.BaseResult
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import kotlin.text.ifEmpty

class NetworkBoundResource @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
){

    suspend fun<ResultType> downloadData(api : suspend () -> Response<ResultType>): Flow<BaseResult<ResultType>> {
        return withContext(ioDispatcher) {
            flow {
                emit(BaseResult.Loading)
                val response:Response<ResultType> = api()
                if (response.isSuccessful){
                    response.body()?.let {
                        emit(BaseResult.Success(data = it))
                    }?: emit(BaseResult.Error(message = "Unknown error occurred", code = 0))
                }else{
                    emit(BaseResult.Error(message = parserErrorBody(response.errorBody()), code = response.code()))
                }

            }.catch { error->
                emit(BaseResult.Error(message = message(error), code = code(error)))
            }
        }
    }

    private fun parserErrorBody(response: ResponseBody?):String{
        return response?.let {
            val errorMessage = JsonParser.parseString(it.string()).asJsonObject["message"].asString
            errorMessage.ifEmpty { "Whoops! Something went wrong. Please try again." }
            errorMessage
        }?:"Whoops! Unknown error occurred. Please try again"
    }
    private fun message(throwable: Throwable?):String{
        when (throwable) {
            is SocketTimeoutException -> return "Whoops! Connection time out. Please try again"
            is IOException -> return "Whoops! No Internet Connection. Please try again"
            is HttpException -> return try {
                val errorJsonString = throwable.response()?.errorBody()?.string()
                val errorMessage = JsonParser.parseString(errorJsonString).asJsonObject["message"].asString
                errorMessage.ifEmpty { "Whoops! Something went wrong. Please try again." }
            }catch (e:Exception){
                "Whoops! Unknown error occurred. Please try again"
            }
        }
        return "Whoops! Unknown error occurred. Please try again"
    }
    private fun code(throwable: Throwable?):Int{
        return if (throwable is HttpException) (throwable).code()
        else  0
    }
}