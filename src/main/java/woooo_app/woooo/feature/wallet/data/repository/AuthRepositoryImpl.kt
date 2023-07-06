package com.wgroup.woooo_app.woooo.feature.wallet.data.repository
//
//import android.util.Log
//import com.wgroup.woooo_app.woooo.data.source.remote.ApiService
//import com.wgroup.woooo_app.woooo.feature.auth.domain.model.LoginResponse
//import com.wgroup.woooo_app.woooo.feature.auth.domain.model.params.LoginRequestParams
//import com.wgroup.woooo_app.woooo.feature.auth.domain.repository.AuthRepository
//import com.wgroup.woooo_app.woooo.shared.base.APIResult
//import com.wgroup.woooo_app.woooo.shared.base.BaseRepository
//import kotlinx.coroutines.flow.Flow
//import javax.inject.Inject
//
//class AuthRepositoryImpl @Inject constructor(
//    private val apiService: ApiService
//) : BaseRepository(), AuthRepository {
//    override suspend fun login(user: LoginRequestParams): Flow<APIResult<LoginResponse>> = safeApiCall {
//        Log.d("LOGIN API CALL",user.toString())
//        apiService.login(user)
//    }
//
//
//}