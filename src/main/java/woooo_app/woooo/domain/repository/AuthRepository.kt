package woooo_app.woooo.domain.repository

import kotlinx.coroutines.flow.Flow
import woooo_app.woooo.data.models.auth.ConfirmAccountModel
import woooo_app.woooo.data.models.auth.ForgotPasswordModel
import woooo_app.woooo.data.models.auth.LoginModel
import woooo_app.woooo.data.models.auth.ResentCodeModel
import woooo_app.woooo.data.models.auth.ResetPasswordModel
import woooo_app.woooo.data.models.auth.SignUpModel
import woooo_app.woooo.data.models.auth.requestmodels.BaseResendCodeReqParam
import woooo_app.woooo.data.models.auth.requestmodels.ForgotPasswordRequestModel
import woooo_app.woooo.data.models.auth.requestmodels.LoginRequestParams
import woooo_app.woooo.data.models.auth.requestmodels.ProfilePicRequestParams
import woooo_app.woooo.data.models.auth.requestmodels.ResetPasswordRequestModel
import woooo_app.woooo.data.models.auth.requestmodels.SignUpRequestModel
import woooo_app.woooo.data.models.profile.UpdateProfileModel
import woooo_app.woooo.data.models.profile.UpdateProfileRequestModel
import woooo_app.woooo.data.models.profile.UploadProfileModel
import woooo_app.woooo.shared.base.APIResult

interface AuthRepository {
    suspend fun login(user: LoginRequestParams): Flow<APIResult<LoginModel>>
    suspend fun signUp(user: SignUpRequestModel): Flow<APIResult<SignUpModel>>
    suspend fun confirmAccount(params: Map<String, String>): Flow<APIResult<ConfirmAccountModel>>
    suspend fun reSendCode(params: BaseResendCodeReqParam): Flow<APIResult<ResentCodeModel>>
    suspend fun forgotPassword(email: ForgotPasswordRequestModel): Flow<APIResult<ForgotPasswordModel>>
    suspend fun resetPassword(params: ResetPasswordRequestModel): Flow<APIResult<ResetPasswordModel>>
    suspend fun updateProfile(params: UpdateProfileRequestModel): Flow<APIResult<UpdateProfileModel>>
    suspend fun uploadProfile(params: ProfilePicRequestParams): Flow<APIResult<UploadProfileModel>>

}