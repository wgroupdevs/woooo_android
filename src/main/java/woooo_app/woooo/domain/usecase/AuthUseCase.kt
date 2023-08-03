package woooo_app.woooo.domain.usecase

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
import woooo_app.woooo.domain.repository.AuthRepository
import woooo_app.woooo.shared.base.APIResult
import woooo_app.woooo.shared.base.BaseUseCase
import javax.inject.Inject

typealias LoginBaseUseCase = BaseUseCase<LoginRequestParams, Flow<APIResult<LoginModel>>>
typealias SignUpBaseUseCase = BaseUseCase<SignUpRequestModel, Flow<APIResult<SignUpModel>>>
typealias ConfirmAccountBaseUseCase = BaseUseCase<Map<String, String>, Flow<APIResult<ConfirmAccountModel>>>
typealias ReSendCodeBaseUseCase = BaseUseCase<BaseResendCodeReqParam, Flow<APIResult<ResentCodeModel>>>
typealias ForgotPasswordBaseUseCase = BaseUseCase<ForgotPasswordRequestModel, Flow<APIResult<ForgotPasswordModel>>>
typealias ResetPasswordBaseUseCase = BaseUseCase<ResetPasswordRequestModel, Flow<APIResult<ResetPasswordModel>>>
typealias UpdateProfileBaseUseCase = BaseUseCase<UpdateProfileRequestModel, Flow<APIResult<UpdateProfileModel>>>
typealias UploadProfileBaseUseCase = BaseUseCase<ProfilePicRequestParams, Flow<APIResult<UploadProfileModel>>>

/**
 * use case to login user
 *
 * @property authRepository repository instance
 */
class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) :
    LoginBaseUseCase {

    /**
     * Function returns a login Api response
     *
     * @param params user login request object
     * @return login response
     */
    override suspend fun invoke(params: LoginRequestParams): Flow<APIResult<LoginModel>> =
        authRepository.login(params)

}

class SignUpUseCase @Inject constructor(private val authRepository: AuthRepository) :
    SignUpBaseUseCase {
    override suspend fun invoke(params: SignUpRequestModel): Flow<APIResult<SignUpModel>> =
        authRepository.signUp(params)
}

class ConfirmAccountUseCase @Inject constructor(private val authRepository: AuthRepository) :
    ConfirmAccountBaseUseCase {
    override suspend fun invoke(params: Map<String, String>): Flow<APIResult<ConfirmAccountModel>> =
        authRepository.confirmAccount(params)
}

class ReSendCodeUseCase @Inject constructor(private val authRepository: AuthRepository) :
    ReSendCodeBaseUseCase {
    override suspend fun invoke(params: BaseResendCodeReqParam): Flow<APIResult<ResentCodeModel>> =
        authRepository.reSendCode(params)
}

class ForgotPasswordUseCase @Inject constructor(private val authRepository: AuthRepository) :
    ForgotPasswordBaseUseCase {
    override suspend fun invoke(params: ForgotPasswordRequestModel): Flow<APIResult<ForgotPasswordModel>> =
        authRepository.forgotPassword(params)
}

class ResetPasswordUseCase @Inject constructor(private val authRepository: AuthRepository) :
    ResetPasswordBaseUseCase {
    override suspend fun invoke(params: ResetPasswordRequestModel): Flow<APIResult<ResetPasswordModel>> =
        authRepository.resetPassword(params)
}

class UpdateProfileUseCase @Inject constructor(private val profileRepository: AuthRepository) :
    UpdateProfileBaseUseCase {
    override suspend fun invoke(params: UpdateProfileRequestModel): Flow<APIResult<UpdateProfileModel>> =
        profileRepository.updateProfile(params)
}

class UploadProfileUseCase @Inject constructor(private val profileRepository: AuthRepository) :
    UploadProfileBaseUseCase {

    override suspend fun invoke(params: ProfilePicRequestParams): Flow<APIResult<UploadProfileModel>> =
        profileRepository.uploadProfile(params)
}