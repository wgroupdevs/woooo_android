package com.wgroup.woooo_app.woooo.shared.base
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import java.io.IOException

abstract class BaseRepository {

    suspend fun <T> safeApiCall(
        dispatcher:CoroutineDispatcher = Dispatchers.IO,
        apiCall:suspend ()-> Response<T>
    ):Flow<APIResult<T>> = flow {
        emit(APIResult.Loading)
        val response = apiCall()

        Log.d("SafeAPICall Message",response.message())
        Log.d("SafeAPICall isSuccessful",response.isSuccessful.toString())
        Log.d("SafeAPICall headers",response.headers().toString())
        Log.d("SafeAPICall raw",response.raw().toString())
        Log.d("SafeAPICall",response.body().toString())
        Log.d("SafeAPICall Error",response.errorBody().toString())
        if (response.isSuccessful){
            val data = response.body()
            if(data != null){
                emit(APIResult.Success(data))
            }else{
                val error = response.errorBody()
                if(error != null){
                    emit(APIResult.Failure(IOException(error.toString())))
                }else{
                    emit(APIResult.Failure(IOException("something went wrong")))
                }
            }
        }else{
            emit(APIResult.Failure(Throwable(response.errorBody().toString())))
        }
    }.catch { e->
        e.printStackTrace()
        emit(APIResult.Failure(Exception(e)))
    }.flowOn(dispatcher)

}