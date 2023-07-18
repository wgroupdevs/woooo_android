package com.wgroup.woooo_app.woooo.data.repositoryImp

import android.util.Log
import com.wgroup.woooo_app.woooo.data.datasource.remote.auth.AuthApiService
import com.wgroup.woooo_app.woooo.data.models.LoginRequestParams
import com.wgroup.woooo_app.woooo.data.models.LoginModel
import com.wgroup.woooo_app.woooo.data.models.SignUpModel
import com.wgroup.woooo_app.woooo.domain.repository.AuthRepository
import com.wgroup.woooo_app.woooo.shared.base.APIResult
import com.wgroup.woooo_app.woooo.shared.base.BaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: AuthApiService
) : BaseRepository(),AuthRepository {
    override suspend fun login(user: LoginRequestParams): Flow<APIResult<LoginModel>> =
        safeApiCall {
            Log.d("LOGIN API CALL",user.toString())
            apiService.login(user)
        }

    override suspend fun signUp(user: LoginRequestParams): Flow<APIResult<SignUpModel>> = safeApiCall {
        Log.d("SignUp API CALL",user.toString())
        apiService.signUp(user)
    }
}