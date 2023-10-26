package eu.siacs.conversations.http.services;

import java.util.Map;

import eu.siacs.conversations.http.model.GetWooContactsModel;
import eu.siacs.conversations.http.model.LoginAPIResponseJAVA;
import eu.siacs.conversations.http.model.SearchAccountAPIResponse;
import eu.siacs.conversations.http.model.SignUpModel;
import eu.siacs.conversations.http.model.SignUpRequestModel;
import eu.siacs.conversations.http.model.TextTranslateApiResponse;
import eu.siacs.conversations.http.model.TextTranslateModel;
import eu.siacs.conversations.http.model.UpdateUserLanguageModel;
import eu.siacs.conversations.http.model.UserBasicInfo;
import eu.siacs.conversations.http.model.requestmodels.GetWooContactsRequestParams;
import eu.siacs.conversations.http.model.requestmodels.EmailRequestModel;
import eu.siacs.conversations.http.model.requestmodels.ResetPasswordRequestModel;
import eu.siacs.conversations.http.model.wallet.BlockChainAPIModel;
import eu.siacs.conversations.http.model.wallet.Payment;
import eu.siacs.conversations.http.model.wallet.PaymentReqModel;
import eu.siacs.conversations.http.model.wallet.WalletOverviewApiModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import eu.siacs.conversations.http.model.requestmodels.LoginRequestParams;

public interface WooService {

    @POST("/api/Auth/login")
    Call<LoginAPIResponseJAVA> login(@Query("isLoginWithEmail") boolean isLoginWithEmail, @Body LoginRequestParams user);

    @GET("/api/v1/Account/SearchAccount")
    Call<SearchAccountAPIResponse> searchAccount(@Query("value") String value, @Query("isEmail") boolean isEmail);

    @GET("/api/v1/Wallet/GetOverviewData")
    Call<WalletOverviewApiModel> getWalletOverviewData(@Query("accountId") String value);

    @POST("/api/v1/Contact/ContactsFromPhone")
    Call<GetWooContactsModel> getWooContacts(@Body GetWooContactsRequestParams params);

    @POST("/api/v1/Chat/TranslateText")
    Call<TextTranslateApiResponse> translateText(@Body TextTranslateModel params);

    @PUT("/api/v1/Account/UpdateUserLanguage")
    Call<UpdateUserLanguageModel> updateUserLanguage(@Query("accountId") String accountId, @Query("language") String language, @Query("languageCode") String languageCode);

    @PUT("/api/v1/Account/UpdateAccount")
    Call<UserBasicInfo> updateProfile(@Query("id") String id, @Body UserBasicInfo params);

    @GET("/api/v1/Account/GetByJID")
    Call<SearchAccountAPIResponse> getAccountByJidAccount(@Query("jid") String jid);

    @POST("/api/Auth/SignUp")
    Call<SignUpModel> signUp(@Body SignUpRequestModel user);

    @GET("/api/Auth/ConfirmAccount")
    Call<BaseModelAPIResponse> confirmAccount(
            @QueryMap Map<String, String> params);

    @POST("/api/Auth/resend-code")
    Call<BaseModelAPIResponse> resendOTP(@Query("IsOtpForAccount") boolean IsOtpForAccount, @Body EmailRequestModel email);

    @POST("/api/Auth/forgot-password")
    Call<BaseModelAPIResponse> forgotPassword(@Body EmailRequestModel email);

    @POST("/api/Auth/reset-password")
    Call<BaseModelAPIResponse> resetPassword(@Body ResetPasswordRequestModel params);

    @GET("api/v1/Blockchain")
    Call<BlockChainAPIModel> getBlockChain();

    @POST("/api/v1/Wallet/UpdateWalletAddress")
    Call<BaseModelAPIResponse> updateWalletAddress(@Query("accountId") String accountId, @Query("walletAddress") String walletAddress);

    @POST("/api/v1/Payment/CreatePayment")
    Call<BaseModelAPIResponse> createPayment(@Body PaymentReqModel payment);

}
