package eu.siacs.conversations.http.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import eu.siacs.conversations.Config;
import eu.siacs.conversations.http.HttpConnectionManager;
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
import eu.siacs.conversations.persistance.WOOPrefManager;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import eu.siacs.conversations.http.model.requestmodels.LoginRequestParams;

public class WooAPIService {

    private static WooService wooService;
    // private field that refers to the object
    private static WooAPIService wooooAuthService;

    private static String TAG = "WooooAPIService_TAG";

    public static String userToken;

    public static WooAPIService getInstance() {



        if (wooooAuthService == null) {
            Log.d(TAG, "USER TOKEN SET IN HEADER :" + userToken);
            wooooAuthService = new WooAPIService();
            final OkHttpClient.Builder builder = HttpConnectionManager.OK_HTTP_CLIENT.newBuilder();
            builder.connectTimeout(50, TimeUnit.SECONDS);
            builder.readTimeout(120, TimeUnit.SECONDS);
            builder.writeTimeout(50, TimeUnit.SECONDS);
            builder.addInterceptor(chain -> {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + userToken)
                        .build();
                return chain.proceed(newRequest);
            });
            final Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .baseUrl(Config.WOOOO_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .callbackExecutor(Executors.newSingleThreadExecutor())
                    .build();
            wooService = retrofit.create(WooService.class);
        }
        return wooooAuthService;
    }

    public static void resetWooAPIService() {
        wooooAuthService = null;
        getInstance();
    }


    public void login(boolean isLoginWithEmail, String email, String phone, String password, OnLoginAPiResult listener) {
        Log.d(phone, "LOGIN STARTED...");
        final LoginRequestParams requestParams = new LoginRequestParams(email, phone, password, "", "", "");
        final Call<LoginAPIResponseJAVA> searchResultCall = wooService.login(isLoginWithEmail, requestParams);
        searchResultCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<LoginAPIResponseJAVA> call, @NonNull Response<LoginAPIResponseJAVA> response) {
                final LoginAPIResponseJAVA body = response.body();
                if (body == null) {
                    try {
                        assert response.errorBody() != null;
                        String errorBodyFound = response.errorBody().byteString().utf8();
                        listener.onLoginApiResultFound(parseErrorBody(errorBodyFound));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    listener.onLoginApiResultFound(body);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginAPIResponseJAVA> call, @NonNull Throwable throwable) {
                Log.d(Config.LOGTAG, "Unable to query WoooService on " + Config.WOOOO_BASE_URL, throwable);
            }
        });
    }

    public void signUp(SignUpRequestModel user, OnSignUpAPiResult listener) {
        Log.d(TAG, "SIGNUP STARTED...");
        final Call<SignUpModel> signUpResultCall = wooService.signUp(user);
        signUpResultCall.enqueue(new Callback<SignUpModel>() {
            @Override
            public void onResponse(@NonNull Call<SignUpModel> call, @NonNull Response<SignUpModel> response) {
                final SignUpModel body = response.body();
                if (body == null) {
                    try {
                        assert response.errorBody() != null;
                        String errorBodyFound = response.errorBody().byteString().utf8();
                        listener.onSignUpResultFound(parseErrorBody(errorBodyFound));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    listener.onSignUpResultFound(body);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SignUpModel> call, @NonNull Throwable throwable) {
                Log.d(Config.LOGTAG, "Unable to query WoooService on " + Config.WOOOO_BASE_URL, throwable);
            }
        });
    }

    public void confirmAccount(Map<String, String> params, OnConfirmAccountAPiResult listener) {
        Log.d(TAG, "confirmAccount STARTED...");
        final Call<BaseModelAPIResponse> confirmAccount = wooService.confirmAccount(params);
        confirmAccount.enqueue(new Callback<BaseModelAPIResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseModelAPIResponse> call, @NonNull Response<BaseModelAPIResponse> response) {
                final BaseModelAPIResponse body = response.body();
                if (body == null) {
                    try {
                        assert response.errorBody() != null;
                        String errorBodyFound = response.errorBody().byteString().utf8();
                        listener.onConfirmAccountResultFound(parseErrorBody(errorBodyFound));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    listener.onConfirmAccountResultFound(body);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseModelAPIResponse> call, @NonNull Throwable throwable) {
                Log.d(Config.LOGTAG, "Unable to query WoooService on " + Config.WOOOO_BASE_URL, throwable);
            }
        });
    }

    public void resendOTP(boolean IsOtpForAccount, EmailRequestModel bodyParams, OnResendOTPAPiResult listener) {
        Log.d(TAG, "resendOTP STARTED...");
        final Call<BaseModelAPIResponse> confirmAccount = wooService.resendOTP(IsOtpForAccount, bodyParams);
        confirmAccount.enqueue(new Callback<BaseModelAPIResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseModelAPIResponse> call, @NonNull Response<BaseModelAPIResponse> response) {
                final BaseModelAPIResponse body = response.body();
                if (body == null) {
                    try {
                        assert response.errorBody() != null;
                        String errorBodyFound = response.errorBody().byteString().utf8();
                        listener.onResendOTPResultFound(parseErrorBody(errorBodyFound));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    listener.onResendOTPResultFound(body);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseModelAPIResponse> call, @NonNull Throwable throwable) {
                Log.d(Config.LOGTAG, "Unable to query WoooService on " + Config.WOOOO_BASE_URL, throwable);
            }
        });
    }

    public void forgotPassword(EmailRequestModel bodyParams, OnForgotPasswordAPiResult listener) {
        Log.d(TAG, "forgotPassword STARTED...");
        final Call<BaseModelAPIResponse> forgotPassword = wooService.forgotPassword(bodyParams);
        forgotPassword.enqueue(new Callback<BaseModelAPIResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseModelAPIResponse> call, @NonNull Response<BaseModelAPIResponse> response) {
                final BaseModelAPIResponse body = response.body();
                if (body == null) {
                    try {
                        assert response.errorBody() != null;
                        String errorBodyFound = response.errorBody().byteString().utf8();
                        listener.onForgotPasswordResultFound(parseErrorBody(errorBodyFound));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    listener.onForgotPasswordResultFound(body);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseModelAPIResponse> call, @NonNull Throwable throwable) {
                Log.d(Config.LOGTAG, "Unable to query WoooService on " + Config.WOOOO_BASE_URL, throwable);
            }
        });
    }

    public void updateWalletAddress(String accountId, String walletAddress, OnUpdateWalletAddressResult listener) {
        Log.d(TAG, "updateWalletAddress STARTED...");
        final Call<BaseModelAPIResponse> updateWallet = wooService.updateWalletAddress(accountId, walletAddress);
        updateWallet.enqueue(new Callback<BaseModelAPIResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseModelAPIResponse> call, @NonNull Response<BaseModelAPIResponse> response) {
                final BaseModelAPIResponse body = response.body();
                if (body == null) {
                    try {
                        assert response.errorBody() != null;
                        String errorBodyFound = response.errorBody().byteString().utf8();
                        listener.onUpdateWalletAddressFound(parseErrorBody(errorBodyFound));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    listener.onUpdateWalletAddressFound(body);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseModelAPIResponse> call, @NonNull Throwable throwable) {
                Log.d(Config.LOGTAG, "Unable to query WoooService on " + Config.WOOOO_BASE_URL, throwable);
            }
        });
    }

    public void createPayment(PaymentReqModel payment, OnCreatePaymentResult listener) {
        Log.d(TAG, "createPayment STARTED...");
        final Call<BaseModelAPIResponse> updateWallet = wooService.createPayment(payment);
        updateWallet.enqueue(new Callback<BaseModelAPIResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseModelAPIResponse> call, @NonNull Response<BaseModelAPIResponse> response) {
                final BaseModelAPIResponse body = response.body();
                if (body == null) {
                    try {
                        assert response.errorBody() != null;
                        String errorBodyFound = response.errorBody().byteString().utf8();
                        listener.onCreatePaymentFound(parseErrorBody(errorBodyFound));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    listener.onCreatePaymentFound(body);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseModelAPIResponse> call, @NonNull Throwable throwable) {
                Log.d(Config.LOGTAG, "Unable to query WoooService on " + Config.WOOOO_BASE_URL, throwable);
            }
        });
    }

    public void resetPassword(ResetPasswordRequestModel bodyParams, OnResetPasswordAPiResult listener) {
        Log.d(TAG, "resendOTP STARTED...");
        final Call<BaseModelAPIResponse> resetPassword = wooService.resetPassword(bodyParams);
        resetPassword.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<BaseModelAPIResponse> call, @NonNull Response<BaseModelAPIResponse> response) {
                final BaseModelAPIResponse body = response.body();
                if (body == null) {
                    try {
                        assert response.errorBody() != null;
                        String errorBodyFound = response.errorBody().byteString().utf8();
                        listener.onResetPasswordResultFound(parseErrorBody(errorBodyFound));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    listener.onResetPasswordResultFound(body);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseModelAPIResponse> call, @NonNull Throwable throwable) {
                Log.d(Config.LOGTAG, "Unable to query WoooService on " + Config.WOOOO_BASE_URL, throwable);
            }
        });
    }

    public void searchAccount(String value, boolean isEmail, OnSearchAccountAPiResult listener) {
        final Call<SearchAccountAPIResponse> searchResultCall = wooService.searchAccount(value, isEmail);
        searchResultCall.enqueue(new Callback<SearchAccountAPIResponse>() {
            @Override
            public void onResponse(@NonNull Call<SearchAccountAPIResponse> call, @NonNull Response<SearchAccountAPIResponse> response) {
                final SearchAccountAPIResponse body = response.body();
                if (body == null) {
                    try {
                        assert response.errorBody() != null;
                        String errorBodyFound = response.errorBody().byteString().utf8();
                        listener.onSearchAccountApiResultFound(parseErrorBody(errorBodyFound));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    listener.onSearchAccountApiResultFound(body);
                }

            }

            @Override
            public void onFailure(@NonNull Call<SearchAccountAPIResponse> call, @NonNull Throwable throwable) {
                Log.d(Config.LOGTAG, "Unable to query WoooService on " + Config.WOOOO_BASE_URL, throwable);
//                        listener.);
            }
        });
    }

    public void getWalletOverviewData(String value, OnWalletOverviewAPiResult listener) {

        Log.d(TAG, "getWalletOverviewData VALUE :" + value);

        final Call<WalletOverviewApiModel> searchResultCall = wooService.getWalletOverviewData(value);
        searchResultCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<WalletOverviewApiModel> call, @NonNull Response<WalletOverviewApiModel> response) {
                Log.d(TAG, "getWalletOverviewData VALUE :" + response);
                final WalletOverviewApiModel body = response.body();
                if (body == null) {
                    try {
                        assert response.errorBody() != null;
                        String errorBodyFound = response.errorBody().byteString().utf8();
                        listener.onWalletOverviewResultFound(parseErrorBody(errorBodyFound));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    listener.onWalletOverviewResultFound(body);
                }

            }

            @Override
            public void onFailure(@NonNull Call<WalletOverviewApiModel> call, @NonNull Throwable throwable) {
                Log.d(Config.LOGTAG, "getWalletOverviewData ERROR " + call.isCanceled(), throwable);
                Log.d(Config.LOGTAG, "getWalletOverviewData ERROR " + call.timeout(), throwable.getCause());
            }
        });
    }

    public void getAccountByJidAccount(String jID, OnGetAccountByJidAPiResult listener) {
        final Call<SearchAccountAPIResponse> searchResultCall = wooService.getAccountByJidAccount(jID);
        searchResultCall.enqueue(new Callback<SearchAccountAPIResponse>() {
            @Override
            public void onResponse(@NonNull Call<SearchAccountAPIResponse> call, @NonNull Response<SearchAccountAPIResponse> response) {
                final SearchAccountAPIResponse body = response.body();
                if (body == null) {
                    try {
                        assert response.errorBody() != null;
                        String errorBodyFound = response.errorBody().byteString().utf8();
                        listener.onGetAccountByJidResultFound(parseErrorBody(errorBodyFound));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    listener.onGetAccountByJidResultFound(body);
                }

            }

            @Override
            public void onFailure(@NonNull Call<SearchAccountAPIResponse> call, @NonNull Throwable throwable) {
                Log.d(Config.LOGTAG, "Unable to query WoooService on " + Config.WOOOO_BASE_URL, throwable);
//                        listener.);
            }
        });
    }

    public void getWooContact(GetWooContactsRequestParams params, OnGetWooContactAPiResult listener) {
        final Call<GetWooContactsModel> searchResultCall = wooService.getWooContacts(params);
        searchResultCall.enqueue(new Callback<GetWooContactsModel>() {
            @Override
            public void onResponse(@NonNull Call<GetWooContactsModel> call, @NonNull Response<GetWooContactsModel> response) {
                final GetWooContactsModel body = response.body();
                if (body == null) {
                    assert response.errorBody() != null;
                    try {
                        String errorBodyFound = response.errorBody().byteString().utf8();
                        listener.OnGetWooContactAPiResultFound(parseErrorBody(errorBodyFound));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    listener.OnGetWooContactAPiResultFound(body);
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetWooContactsModel> call, @NonNull Throwable throwable) {
                Log.d(Config.LOGTAG, "Unable to query WoooService on " + Config.WOOOO_BASE_URL, throwable);
//                        listener.);
            }
        });
    }

    public void updateUserLanguage(String accountId, String language, String languageCode, OnUpdateUserLanguageApiResult listener) {
        final Call<UpdateUserLanguageModel> searchResultCall = wooService.updateUserLanguage(accountId, language, languageCode);
        searchResultCall.enqueue(new Callback<UpdateUserLanguageModel>() {
            @Override
            public void onResponse(@NonNull Call<UpdateUserLanguageModel> call, @NonNull Response<UpdateUserLanguageModel> response) {
                final UpdateUserLanguageModel body = response.body();
                if (body == null) {
                    try {
                        assert response.errorBody() != null;
                        String errorBodyFound = response.errorBody().byteString().utf8();
                        listener.OnUpdateUserLanguageAPiResultFound(parseErrorBody(errorBodyFound));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    listener.OnUpdateUserLanguageAPiResultFound(body);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpdateUserLanguageModel> call, @NonNull Throwable throwable) {
                Log.d(Config.LOGTAG, "Unable to query WoooService on " + Config.WOOOO_BASE_URL, throwable);
//                        listener.);
            }
        });
    }

    public void updateProfile(UserBasicInfo user, OnUpdateAccountApiResult listener) {
        final Call<UserBasicInfo> searchResultCall = wooService.updateProfile(user.accountId, user);
        searchResultCall.enqueue(new Callback<UserBasicInfo>() {
            @Override
            public void onResponse(@NonNull Call<UserBasicInfo> call, @NonNull Response<UserBasicInfo> response) {
                final UserBasicInfo body = response.body();
                if (body == null) {
                    try {
                        assert response.errorBody() != null;
                        String errorBodyFound = response.errorBody().byteString().utf8();
                        listener.OnUpdateAccountAPiResultFound(parseErrorBody(errorBodyFound));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    listener.OnUpdateAccountAPiResultFound(body);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserBasicInfo> call, @NonNull Throwable throwable) {
                Log.d(Config.LOGTAG, "Unable to query WoooService on " + Config.WOOOO_BASE_URL, throwable);
//                        listener.);
            }
        });
    }

    public void translateText(TextTranslateModel translateModel, OnTextTranslateAPiResult listener) {
        final Call<TextTranslateApiResponse> translateTextResultCall = wooService.translateText(translateModel);
        translateTextResultCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TextTranslateApiResponse> call, @NonNull Response<TextTranslateApiResponse> response) {
                final TextTranslateApiResponse body = response.body();
                if (body == null) {
                    try {
                        assert response.errorBody() != null;
                        String errorBodyFound = response.errorBody().byteString().utf8();
                        listener.OnTextTranslateResultFound(parseErrorBody(errorBodyFound));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    listener.OnTextTranslateResultFound(body);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TextTranslateApiResponse> call, @NonNull Throwable throwable) {
                Log.d(Config.LOGTAG, "Unable to query WoooService on " + Config.WOOOO_BASE_URL, throwable);
//                        listener.);
            }
        });
    }

    public void getBlockChain(OnGetBlockChainApiResult listener) {
        final Call<BlockChainAPIModel> blockChainResultCall = wooService.getBlockChain();
        blockChainResultCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<BlockChainAPIModel> call, @NonNull Response<BlockChainAPIModel> response) {
                final BlockChainAPIModel body = response.body();
                if (body == null) {
                    try {
                        assert response.errorBody() != null;
                        String errorBodyFound = response.errorBody().byteString().utf8();
                        listener.OnGetBlockChainResultFound(parseErrorBody(errorBodyFound));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    listener.OnGetBlockChainResultFound(body);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BlockChainAPIModel> call, @NonNull Throwable throwable) {
                Log.d(Config.LOGTAG, "Unable to query WoooService on " + Config.WOOOO_BASE_URL, throwable);
//                        listener.);
            }
        });
    }


    public interface OnLoginAPiResult {
        <T> void onLoginApiResultFound(T result);
    }

    public interface OnSignUpAPiResult {
        <T> void onSignUpResultFound(T result);
    }

    public interface OnConfirmAccountAPiResult {
        <T> void onConfirmAccountResultFound(T result);
    }

    public interface OnResendOTPAPiResult {
        <T> void onResendOTPResultFound(T result);
    }

    public interface OnForgotPasswordAPiResult {
        <T> void onForgotPasswordResultFound(T result);
    }

    public interface OnUpdateWalletAddressResult {
        <T> void onUpdateWalletAddressFound(T result);
    }

    public interface OnCreatePaymentResult {
        <T> void onCreatePaymentFound(T result);
    }

    public interface OnResetPasswordAPiResult {
        <T> void onResetPasswordResultFound(T result);
    }

    public interface OnSearchAccountAPiResult {
        <T> void onSearchAccountApiResultFound(T result);
    }

    public interface OnWalletOverviewAPiResult {
        <T> void onWalletOverviewResultFound(T result);
    }

    public interface OnGetAccountByJidAPiResult {
        <T> void onGetAccountByJidResultFound(T result);
    }

    public interface OnGetWooContactAPiResult {
        <T> void OnGetWooContactAPiResultFound(T result);
    }

    public interface OnTextTranslateAPiResult {
        <T> void OnTextTranslateResultFound(T result);
    }

    public interface OnUpdateUserLanguageApiResult {
        <T> void OnUpdateUserLanguageAPiResultFound(T result);
    }

    public interface OnUpdateAccountApiResult {
        <T> void OnUpdateAccountAPiResultFound(T result);
    }

    public interface OnGetBlockChainApiResult {
        <T> void OnGetBlockChainResultFound(T result);
    }


    private BaseModelAPIResponse parseErrorBody(String response) {
        Log.d("parseErrorBody", response);
        Gson gson = new Gson();
        return gson.fromJson(response, BaseModelAPIResponse.class);
    }

}


