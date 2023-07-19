package woooo_app.woooo.shared.base

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import woooo_app.woooo.data.models.BaseModel

abstract class BaseRepository {

    suspend fun <T> safeApiCall(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        apiCall: suspend () -> Response<T>
    ): Flow<APIResult<T>> = flow {
        emit(APIResult.Loading)
        val response = apiCall()
//        Log.d("SafeAPICall StatusCode", response.code().toString())
//        Log.d("SafeAPICall Message", response.message())
//        Log.d("SafeAPICall Successful", response.isSuccessful.toString())
//        Log.d("SafeAPICall headers", response.headers().toString())
//        Log.d("SafeAPICall raw", response.raw().toString())
//        Log.d("SafeAPICall Body", response.body().toString())
//        Log.d("SafeAPICall Error", response.errorBody()?.source().toString())
//        Log.d("SafeAPICall Error ", response.errorBody()?.charStream().toString())
////        Log.d("SafeAPICall Error", response.errorBody().source().readUtf8Line().toString())
//
//
////        Log.d("SafeAPICall fromJson",json.toString())
////        val jsonObj = JSONObject(bufferUTF)
////        val map = jsonObj.toMap()

        if (response.isSuccessful) {
            val data = response.body()
            if (data != null) {
                emit(APIResult.Success(data))
            } else {
                val error = response.errorBody()
                if (error != null) {

                    emit(APIResult.Failure(parseErrorBody(response)))
                } else {
                    emit(APIResult.Failure(BaseModel(Message = "Something went wrong")))
                }
            }
        } else {
            emit(APIResult.Failure(parseErrorBody(response)))
        }
    }.catch { e ->
        e.printStackTrace()
        emit(APIResult.Failure(BaseModel(Message = e.message)))
    }.flowOn(dispatcher)

}


private fun <T> parseErrorBody(response: Response<T>): BaseModel {
    val bufferUTF = response.errorBody()?.source()?.readUtf8Line().toString();
    val parser = JsonParser()
    val json = parser.parse(bufferUTF)
    return Gson().fromJson(json, BaseModel::class.java);
}