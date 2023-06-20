package com.wgroup.woooo_app.woooo.data.source.remote

import com.wgroup.woooo_app.woooo.feature.auth.domain.model.params.LoginRequestParams
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    companion object {
        const val BASE_URL = "https://wooooapi.watchblock.net"
    }

    @POST("/api/Auth/LoginWithEmail")
    suspend fun login(@Body user: LoginRequestParams):Response<String>

}