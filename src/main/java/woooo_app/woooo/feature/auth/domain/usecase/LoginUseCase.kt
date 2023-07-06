package com.wgroup.woooo_app.woooo.feature.auth.domain.usecase

import com.wgroup.woooo_app.woooo.feature.auth.domain.model.LoginResponse
import com.wgroup.woooo_app.woooo.feature.auth.domain.model.params.LoginRequestParams
import com.wgroup.woooo_app.woooo.feature.auth.domain.repository.AuthRepository
import com.wgroup.woooo_app.woooo.shared.base.APIResult
import com.wgroup.woooo_app.woooo.shared.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


typealias LoginBaseUseCase = BaseUseCase<LoginRequestParams, Flow<APIResult<LoginResponse>>>


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
    override suspend fun invoke(params: LoginRequestParams): Flow<APIResult<LoginResponse>> = authRepository.login(params)

}