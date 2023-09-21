package eu.siacs.conversations.http.services;

import eu.siacs.conversations.http.model.GetWooContactsModel;
import eu.siacs.conversations.http.model.LoginAPIResponseJAVA;
import eu.siacs.conversations.http.model.SearchAccountAPIResponse;
import eu.siacs.conversations.http.model.TextTranslateApiResponse;
import eu.siacs.conversations.http.model.TextTranslateModel;
import eu.siacs.conversations.http.model.UpdateUserLanguageModel;
import eu.siacs.conversations.http.model.UserBasicInfo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import woooo_app.woooo.data.models.auth.requestmodels.GetWooContactsRequestParams;
import woooo_app.woooo.data.models.auth.requestmodels.LoginRequestParams;

public interface WooooService {

    @POST("/api/Auth/login")
    Call<LoginAPIResponseJAVA> login(@Query("isLoginWithEmail") boolean isLoginWithEmail, @Body LoginRequestParams user);

    @GET("/api/v1/Account/SearchAccount")
    Call<SearchAccountAPIResponse> searchAccount(@Query("value") String value, @Query("isEmail") boolean isEmail);

    @POST("/api/v1/Contact/ContactsFromPhone")
    Call<GetWooContactsModel> getWooContacts(@Body GetWooContactsRequestParams params);
  @POST("/api/v1/Chat/TranslateText")
    Call<TextTranslateApiResponse> translateText(@Body TextTranslateModel params);

    @PUT("/api/v1/Account/UpdateUserLanguage")
    Call<UpdateUserLanguageModel> updateUserLanguage(@Query("accountId") String accountId, @Query("language") String language, @Query("languageCode") String languageCode);
    @PUT("/api/v1/Account/UpdateAccount")
    Call<UserBasicInfo> updateProfile(@Query("id") String id,@Body UserBasicInfo params);

//    @PUT("/api/v1/Account/UpdateAccount")
//    suspend fun updateProfile(
//            @Query("id") id: String,@Body params:UpdateProfileRequestModel
//    ): Response<UpdateProfileModel>
}
