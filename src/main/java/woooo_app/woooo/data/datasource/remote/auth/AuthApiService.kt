package woooo_app.woooo.data.datasource.remote.auth

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.QueryMap
import woooo_app.woooo.data.models.auth.ConfirmAccountModel
import woooo_app.woooo.data.models.auth.ForgotPasswordModel
import woooo_app.woooo.data.models.auth.LoginModel
import woooo_app.woooo.data.models.auth.ResentCodeModel
import woooo_app.woooo.data.models.auth.ResetPasswordModel
import woooo_app.woooo.data.models.auth.SignUpModel
import woooo_app.woooo.data.models.auth.requestmodels.ForgotPasswordRequestModel
import woooo_app.woooo.data.models.auth.requestmodels.LoginRequestParams
import woooo_app.woooo.data.models.auth.requestmodels.ReSentOTPRequestModel
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

interface AuthApiService {
    companion object {
        const val BASE_URL = "https://wooooapi.watchblock.net"
//        const val BASE_URL = "http://192.168.100.83"
//        const val BASE_URL = "http://192.168.1.18"
    }

    @POST("/api/Auth/login")
    suspend fun login(
        @Query("isLoginWithEmail") isLoginWithEmail: Boolean,@Body user: LoginRequestParams
    ): Response<LoginModel>

    @POST("/api/Auth/SignUp")
    suspend fun signUp(@Body user: SignUpRequestModel): Response<SignUpModel>

    @GET("/api/Auth/ConfirmAccount")
    suspend fun confirmAccount(
        @QueryMap params: Map<String,String>
    ): Response<ConfirmAccountModel>

    @POST("/api/Auth/resend-code")
    suspend fun reSendCode(
        @Query("IsOtpForAccount") IsOtpForAccount: Boolean,@Body email: ReSentOTPRequestModel
    ): Response<ResentCodeModel>

    @POST("/api/Auth/forgot-password")
    suspend fun forgotPassword(@Body params: ForgotPasswordRequestModel): Response<ForgotPasswordModel>

    @POST("/api/Auth/reset-password")
    suspend fun resetPassword(@Body params: ResetPasswordRequestModel): Response<ResetPasswordModel>

    @PUT("/api/v1/Account/UpdateAccount")
    suspend fun updateProfile(
        @Query("id") id: String,@Body params: UpdateProfileRequestModel
    ): Response<UpdateProfileModel>

    @Multipart
    @POST("/api/v1/Account/ProfilePic") // Replace with your API endpoint
    suspend fun uploadFile(
        @Part("AccountUniqueId") id: RequestBody,@Part image: MultipartBody.Part
    ): Response<UploadProfileModel>

    @PUT("/api/v1/Settings/ChangePhoneNumber")
    suspend fun changeNumber(
        @Body params: ChangeNumberReqModel
    ): Response<ChangeNumberModel>

    @PUT("/api/v1/Settings/ChangePassword")
    suspend fun changePassword(@Body params: ChangePasswordReqModel): Response<ChangePasswordModel>

    @POST("/api/v1/Settings/DeleteAccount")
    suspend fun deleteAccount(@Query("accountId") accountID: String): Response<DeleteAccountModel>
}