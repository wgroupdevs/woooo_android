package woooo_app.woooo.domain.repository

import com.wgroup.woooo_app.woooo.shared.base.APIResult
import kotlinx.coroutines.flow.Flow
import woooo_app.woooo.data.models.auth.ConfirmAccountModel
import woooo_app.woooo.data.models.auth.LoginModel
import woooo_app.woooo.data.models.auth.ResendCodeModel
import woooo_app.woooo.data.models.auth.SignUpModel
import woooo_app.woooo.data.models.auth.requestmodels.LoginRequestParams
import woooo_app.woooo.data.models.auth.requestmodels.SignUpRequestModel

interface AuthRepository {
    suspend fun login(user: LoginRequestParams): Flow<APIResult<LoginModel>>
    suspend fun signUp(user: SignUpRequestModel): Flow<APIResult<SignUpModel>>
    suspend fun confirmAccount(params: SignUpRequestModel): Flow<APIResult<ConfirmAccountModel>>
    suspend fun reSendCode(params: String): Flow<APIResult<ResendCodeModel>>
}