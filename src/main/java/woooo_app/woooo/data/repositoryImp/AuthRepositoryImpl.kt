package woooo_app.woooo.data.repositoryImp

import android.util.Log
import kotlinx.coroutines.flow.Flow
import woooo_app.woooo.data.datasource.remote.auth.AuthApiService
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
import woooo_app.woooo.data.models.settings.ChangeNumberModel
import woooo_app.woooo.data.models.settings.ChangePasswordModel
import woooo_app.woooo.data.models.settings.DeleteAccountModel
import woooo_app.woooo.data.models.settings.requestModels.ChangeNumberReqModel
import woooo_app.woooo.data.models.settings.requestModels.ChangePasswordReqModel
import woooo_app.woooo.domain.repository.AuthRepository
import woooo_app.woooo.shared.base.APIResult
import woooo_app.woooo.shared.base.BaseRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: AuthApiService
) : BaseRepository(),AuthRepository {

    override suspend fun login(user: LoginRequestParams): Flow<APIResult<LoginModel>> =
        safeApiCall {
            Log.d("LOGIN API CALL",user.toString())
            apiService.login(true,user)
        }

    override suspend fun signUp(user: SignUpRequestModel): Flow<APIResult<SignUpModel>> =
        safeApiCall {
            Log.d("SignUp API CALL",user.toString())
            apiService.signUp(user)
        }

    override suspend fun confirmAccount(params: Map<String,String>): Flow<APIResult<ConfirmAccountModel>> =
        safeApiCall {
            Log.d("Confirm Account API",params.toString())
            apiService.confirmAccount(params)
        }

    override suspend fun reSendCode(params: BaseResendCodeReqParam): Flow<APIResult<ResentCodeModel>> =
        safeApiCall {
            Log.d("reSend API ",params.toString())
            apiService.reSendCode(
                IsOtpForAccount = params.IsOtpForAccount,email = params.email
            )
        }

    override suspend fun forgotPassword(email: ForgotPasswordRequestModel): Flow<APIResult<ForgotPasswordModel>> =
        safeApiCall {
            Log.d("reSend API ",email.toString())
            apiService.forgotPassword(email)
        }

    override suspend fun resetPassword(params: ResetPasswordRequestModel): Flow<APIResult<ResetPasswordModel>> =
        safeApiCall {
            Log.d("reSend API ",params.toString())
            apiService.resetPassword(params)
        }

    override suspend fun updateProfile(params: UpdateProfileRequestModel): Flow<APIResult<UpdateProfileModel>> =
        safeApiCall {
            Log.d("update Profile params ","${params.toMap()}")
            apiService.updateProfile("0102600C-AB5F-4385-A7AC-8D6C6754FABD",params)
        }

    override suspend fun uploadProfile(params: ProfilePicRequestParams): Flow<APIResult<UploadProfileModel>> =
        safeApiCall {
            Log.d("${params.imageFile} <image  ${params.accountUniqueId}","Upload Profile Pic ")
//            apiService.uploadFile("0D163635-2ED2-4C36-8EBB-77EF90C74023", params)
            apiService.uploadFile(params.accountUniqueId,params.imageFile)
        }

    override suspend fun changeNumber(params: ChangeNumberReqModel): Flow<APIResult<ChangeNumberModel>> =
        safeApiCall {
            Log.d(params.phoneNumber,"Sed=n===ascjnac")
            apiService.changeNumber(params)
        }

    override suspend fun changePassword(params: ChangePasswordReqModel): Flow<APIResult<ChangePasswordModel>> =
        safeApiCall {
            apiService.changePassword(params)
        }

    override suspend fun deleteAccount(accountId: String): Flow<APIResult<DeleteAccountModel>> =
        safeApiCall {
            apiService.deleteAccount(accountId)
        }


}