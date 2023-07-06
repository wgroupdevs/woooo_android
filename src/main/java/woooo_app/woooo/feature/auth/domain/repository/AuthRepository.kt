package com.wgroup.woooo_app.woooo.feature.auth.domain.repository

import com.wgroup.woooo_app.woooo.feature.auth.domain.model.LoginResponse
import com.wgroup.woooo_app.woooo.feature.auth.domain.model.params.LoginRequestParams
import com.wgroup.woooo_app.woooo.shared.base.APIResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(user: LoginRequestParams): Flow<APIResult<LoginResponse>>
}