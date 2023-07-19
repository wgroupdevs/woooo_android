package woooo_app.woooo.shared.base
import android.util.Log
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.BufferedSource
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import woooo_app.woooo.data.models.BaseModel

abstract class BaseRepository {

    suspend fun <T> safeApiCall(
        dispatcher:CoroutineDispatcher = Dispatchers.IO,
        apiCall:suspend ()-> Response<T>
    ):Flow<APIResult<T>> = flow {
        emit(APIResult.Loading)
        val response = apiCall()

        Log.d("SafeAPICall StatusCode",response.code().toString())
        Log.d("SafeAPICall Message",response.message())
        Log.d("SafeAPICall Successful",response.isSuccessful.toString())
        Log.d("SafeAPICall headers",response.headers().toString())
        Log.d("SafeAPICall raw",response.raw().toString())
        Log.d("SafeAPICall Body",response.body().toString())
        Log.d("SafeAPICall Error", response.errorBody()?.source().toString())
        Log.d("SafeAPICall Error ", response.errorBody()?.charStream().toString())
//        Log.d("SafeAPICall Error", response.errorBody().source().readUtf8Line().toString())


        val bufferUTF= response.errorBody()!!.source().readUtf8Line().toString();
        val parser = JsonParser()
        val json = parser
//        val jsonObj = JSONObject(bufferUTF)
//        val map = jsonObj.toMap()


//        val data= Gson().fromJson(JSONObject(), Map::class.java)
//        val data= gson.fromJson(errorBody, Map::class.java)
//        Log.d("SafeAPICall fromJson",map.toString())


        if (response.isSuccessful){
            val data = response.body()
            if(data != null){
                emit(APIResult.Success(data))
            }else{
                val error = response.errorBody()
                if(error != null){
//                    emit(APIResult.Failure(IOException(error.toString())))
                }else{
//                    emit(APIResult.Failure(IOException("something went wrong")))
                }
            }
        }else{
            emit(APIResult.Failure(BaseModel()))
        }
    }.catch { e->
        e.printStackTrace()
//        emit(APIResult.Failure(Exception(e)))
    }.flowOn(dispatcher)

}

suspend fun parseErrorBody(source: BufferedSource): String {
    val stringBuilder = StringBuilder()
    while (true) {
        val data = source.readUtf8Line()
        if (data!=null && data.isEmpty()) {
            break
        }
        stringBuilder.append(data)
    }
    return stringBuilder.toString()
}

fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {
    when (val value = this[it])
    {
        is JSONArray ->
        {
            val map = (0 until value.length()).associate { Pair(it.toString(), value[it]) }
            JSONObject(map).toMap().values.toList()
        }
        is JSONObject -> value.toMap()
        JSONObject.NULL -> null
        else            -> value
    }
}