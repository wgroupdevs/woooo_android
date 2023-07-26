package woooo_app.woooo.data.repositoryImp

import kotlinx.coroutines.flow.Flow
import woooo_app.woooo.data.models.profile.UpdateProfileModel
import woooo_app.woooo.shared.base.APIResult
import woooo_app.woooo.shared.base.BaseRepository
import javax.inject.Inject

//class ProfileRepositoryImpl @Inject constructor(
//    private val apiService: ProfileApiService
//) : BaseRepository(),ProfileRepository {
//    override suspend fun updateProfile(params: Map<String,String>): Flow<APIResult<UpdateProfileModel>> =
//        safeApiCall {
//            apiService.updateProfile(params)
//        }
//}