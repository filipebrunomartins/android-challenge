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
                    }?: emit(BaseResult.Error(message = "Ocorreu um erro desconhecido", code = 0))
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
            errorMessage.ifEmpty { "Ops! Algo deu errado. Por favor, tente novamente." }
            errorMessage
        }?:"Ops! Ocorreu um erro desconhecido. Tente novamente."
    }
    private fun message(throwable: Throwable?):String{
        when (throwable) {
            is SocketTimeoutException -> return "Ops! Tempo limite de conexão excedido. Tente novamente."
            is IOException -> return "Ops! Sem conexão com a internet. Tente novamente."
            is HttpException -> return try {
                val errorJsonString = throwable.response()?.errorBody()?.string()
                val errorMessage = JsonParser.parseString(errorJsonString).asJsonObject["message"].asString
                errorMessage.ifEmpty { "Ops! Algo deu errado. Por favor, tente novamente." }
            }catch (e:Exception){
                "Ops! Ocorreu um erro desconhecido. Tente novamente."
            }
        }
        return "Ops! Ocorreu um erro desconhecido. Tente novamente."
    }
    private fun code(throwable: Throwable?):Int{
        return if (throwable is HttpException) (throwable).code()
        else  0
    }
}