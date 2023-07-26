package woooo_app.woooo.domain.usecase
//
//import kotlinx.coroutines.flow.Flow
//import woooo_app.woooo.data.models.profile.UpdateProfileModel
//import woooo_app.woooo.domain.repository.ProfileRepository
//import woooo_app.woooo.shared.base.APIResult
//import woooo_app.woooo.shared.base.BaseUseCase
//import javax.inject.Inject
//
//typealias UpdateProfileBaseUseCase = BaseUseCase<Map<String,String>,Flow<APIResult<UpdateProfileModel>>>
//
//class UpdateProfileUseCase @Inject constructor(private val profileRepository: ProfileRepository) :
//    UpdateProfileBaseUseCase {
//    override suspend fun invoke(params: Map<String,String>): Flow<APIResult<UpdateProfileModel>> =
//        profileRepository.updateProfile(params)
//}