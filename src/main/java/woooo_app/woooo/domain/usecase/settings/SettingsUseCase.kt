package woooo_app.woooo.domain.usecase.settings

import kotlinx.coroutines.flow.Flow
import woooo_app.woooo.data.models.settings.ChangeNumberModel
import woooo_app.woooo.data.models.settings.ChangePasswordModel
import woooo_app.woooo.data.models.settings.DeleteAccountModel
import woooo_app.woooo.data.models.settings.requestModels.ChangeNumberReqModel
import woooo_app.woooo.data.models.settings.requestModels.ChangePasswordReqModel
import woooo_app.woooo.domain.repository.AuthRepository
import woooo_app.woooo.shared.base.APIResult
import woooo_app.woooo.shared.base.BaseUseCase
import javax.inject.Inject

typealias ChangeNumberBaseUseCase = BaseUseCase<ChangeNumberReqModel,Flow<APIResult<ChangeNumberModel>>>
typealias ChangePasswordBaseUseCase = BaseUseCase<ChangePasswordReqModel,Flow<APIResult<ChangePasswordModel>>>
typealias DeleteAccountBaseUseCase = BaseUseCase<String,Flow<APIResult<DeleteAccountModel>>>

class ChangeNumberUseCase @Inject constructor(private val profileRepository: AuthRepository) :
    ChangeNumberBaseUseCase {

    override suspend fun invoke(params: ChangeNumberReqModel): Flow<APIResult<ChangeNumberModel>> =
        profileRepository.changeNumber(params)
}

class ChangePasswordUseCase @Inject constructor(private val profileRepository: AuthRepository) :
    ChangePasswordBaseUseCase {

    override suspend fun invoke(params: ChangePasswordReqModel): Flow<APIResult<ChangePasswordModel>> =
        profileRepository.changePassword(params)
}

class DeleteAccountUseCase @Inject constructor(private val profileRepository: AuthRepository) :
    DeleteAccountBaseUseCase {

    override suspend fun invoke(accountId: String): Flow<APIResult<DeleteAccountModel>> =
        profileRepository.deleteAccount(accountId)
}