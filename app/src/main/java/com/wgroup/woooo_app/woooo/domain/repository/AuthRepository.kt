package com.wgroup.woooo_app.woooo.domain.repository

import com.wgroup.woooo_app.woooo.data.models.LoginModel
import com.wgroup.woooo_app.woooo.data.models.LoginRequestParams
import com.wgroup.woooo_app.woooo.data.models.SignUpModel
import com.wgroup.woooo_app.woooo.shared.base.APIResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(user: LoginRequestParams): Flow<APIResult<LoginModel>>
    suspend fun signUp(user: LoginRequestParams): Flow<APIResult<SignUpModel>>
}