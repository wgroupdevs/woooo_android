package com.wgroup.woooo_app.woooo.domain.usecase

import com.wgroup.woooo_app.woooo.data.models.LoginRequestParams
import com.wgroup.woooo_app.woooo.data.models.LoginModel
import com.wgroup.woooo_app.woooo.data.models.SignUpModel
import com.wgroup.woooo_app.woooo.domain.repository.AuthRepository
import com.wgroup.woooo_app.woooo.shared.base.APIResult
import com.wgroup.woooo_app.woooo.shared.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias LoginBaseUseCase = BaseUseCase<LoginRequestParams,Flow<APIResult<LoginModel>>>
typealias SignUpBaseUseCase = BaseUseCase<LoginRequestParams,Flow<APIResult<SignUpModel>>>

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
    override suspend fun invoke(params: LoginRequestParams): Flow<APIResult<SignUpModel>> =
        authRepository.signUp(params)

}
