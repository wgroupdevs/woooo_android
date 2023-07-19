package woooo_app.woooo.domain.repository

import woooo_app.woooo.shared.base.APIResult
import kotlinx.coroutines.flow.Flow
import woooo_app.woooo.data.models.auth.LoginModel
import woooo_app.woooo.data.models.auth.LoginRequestParams
import woooo_app.woooo.data.models.auth.SignUpModel
import woooo_app.woooo.data.models.auth.SignUpRequestModel

interface AuthRepository {
    suspend fun login(user: LoginRequestParams): Flow<APIResult<LoginModel>>
    suspend fun signUp(user: SignUpRequestModel): Flow<APIResult<SignUpModel>>
}