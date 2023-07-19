package woooo_app.woooo.data.datasource.remote.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap
import woooo_app.woooo.data.models.auth.ConfirmAccountModel
import woooo_app.woooo.data.models.auth.LoginModel
import woooo_app.woooo.data.models.auth.ResendCodeModel
import woooo_app.woooo.data.models.auth.SignUpModel
import woooo_app.woooo.data.models.auth.requestmodels.LoginRequestParams
import woooo_app.woooo.data.models.auth.requestmodels.SignUpRequestModel

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

    @GET("/api/Auth/ConfirmAccount")
    suspend fun confirmAccount(@QueryMap params: SignUpRequestModel): Response<ConfirmAccountModel>

    @POST("/api/Auth/resend-code")
    suspend fun reSendCode(@QueryMap params: String): Response<ResendCodeModel>

    @POST("/api/Auth/forgot-password")
    suspend fun forgotPassword(@QueryMap params: String): Response<ResendCodeModel>

}