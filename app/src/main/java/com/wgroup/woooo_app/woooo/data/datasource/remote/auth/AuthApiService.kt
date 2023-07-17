package com.wgroup.woooo_app.woooo.data.datasource.remote.auth

import com.wgroup.woooo_app.woooo.data.models.LoginModel
import com.wgroup.woooo_app.woooo.data.models.LoginRequestParams
import com.wgroup.woooo_app.woooo.data.models.SignUpModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    companion object {
//        const val BASE_URL = "https://wooooapi.watchblock.net"
        const val BASE_URL = "http://192.168.1.18"
    }

    @POST("/api/Auth/LoginWithEmail")
    suspend fun login(@Body user: LoginRequestParams):Response<LoginModel>
    @POST("/api/Auth/SignUp")
    suspend fun signUp(@Body user: LoginRequestParams):Response<SignUpModel>

}