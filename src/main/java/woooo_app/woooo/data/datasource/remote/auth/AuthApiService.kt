package woooo_app.woooo.data.datasource.remote.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import woooo_app.woooo.data.models.auth.LoginModel
import woooo_app.woooo.data.models.auth.LoginRequestParams
import woooo_app.woooo.data.models.auth.SignUpModel
import woooo_app.woooo.data.models.auth.SignUpRequestModel

interface AuthApiService {
    companion object {
        const val BASE_URL = "https://wooooapi.watchblock.net"
//        const val BASE_URL = "http://192.168.1.18"
    }

    @POST("/api/Auth/login")
    suspend fun login(
        @Query("isLoginWithEmail") isLoginWithEmail: Boolean,
        @Body user: LoginRequestParams
    ): Response<LoginModel>

    @POST("/api/Auth/SignUp")
    suspend fun signUp(@Body user: SignUpRequestModel): Response<SignUpModel>


}