package eu.siacs.conversations.http.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import eu.siacs.conversations.Config;
import eu.siacs.conversations.http.HttpConnectionManager;
import eu.siacs.conversations.http.model.GetWooContactsModel;
import eu.siacs.conversations.http.model.LoginAPIResponseJAVA;
import eu.siacs.conversations.http.model.SearchAccountAPIResponse;
import eu.siacs.conversations.http.model.TextTranslateApiResponse;
import eu.siacs.conversations.http.model.TextTranslateModel;
import eu.siacs.conversations.http.model.UpdateUserLanguageModel;
import eu.siacs.conversations.http.model.UserBasicInfo;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import woooo_app.woooo.data.models.auth.requestmodels.GetWooContactsRequestParams;
import woooo_app.woooo.data.models.auth.requestmodels.LoginRequestParams;

public class WooooAPIService {

    private static WooooService wooooService;
    // private field that refers to the object
    private static WooooAPIService wooooAuthService;


    public static WooooAPIService getInstance() {

        // create object if it's not already created

        if (wooooAuthService == null) {
            wooooAuthService = new WooooAPIService();
            final OkHttpClient.Builder builder = HttpConnectionManager.OK_HTTP_CLIENT.newBuilder();
            builder.connectTimeout(50, TimeUnit.SECONDS);
            builder.readTimeout(50, TimeUnit.SECONDS);
            builder.writeTimeout(50, TimeUnit.SECONDS);
            final Retrofit retrofit = new Retrofit.Builder().client(builder.build()).baseUrl(Config.WOOOO_BASE_URL).addConverterFactory(GsonConverterFactory.create()).callbackExecutor(Executors.newSingleThreadExecutor()).build();
            wooooService = retrofit.create(WooooService.class);
        }


        return wooooAuthService;
    }


    public void login(boolean isLoginWithEmail, String email, String phone, String password, OnLoginAPiResult listener) {
        Log.d(phone, "LOGIN STARTED...");
        final LoginRequestParams requestParams = new LoginRequestParams(email, phone, password, "", "", "");
        final Call<LoginAPIResponseJAVA> searchResultCall = wooooService.login(isLoginWithEmail, requestParams);
        searchResultCall.enqueue(new Callback<LoginAPIResponseJAVA>() {
            @Override
            public void onResponse(@NonNull Call<LoginAPIResponseJAVA> call, @NonNull Response<LoginAPIResponseJAVA> response) {
                final LoginAPIResponseJAVA body = response.body();
                if (body == null) {
                    assert response.errorBody() != null;
                    try {
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
//                        listener.);
            }
        });
    }

    public void searchAccount(String value, boolean isEmail, OnSearchAccountAPiResult listener) {
        final Call<SearchAccountAPIResponse> searchResultCall = wooooService.searchAccount(value, isEmail);
        searchResultCall.enqueue(new Callback<SearchAccountAPIResponse>() {
            @Override
            public void onResponse(@NonNull Call<SearchAccountAPIResponse> call, @NonNull Response<SearchAccountAPIResponse> response) {
                final SearchAccountAPIResponse body = response.body();
                if (body == null) {
                    assert response.errorBody() != null;
                    try {
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

    public void getAccountByJidAccount(String jID, OnGetAccountByJidAPiResult listener) {
        final Call<SearchAccountAPIResponse> searchResultCall = wooooService.getAccountByJidAccount(jID);
        searchResultCall.enqueue(new Callback<SearchAccountAPIResponse>() {
            @Override
            public void onResponse(@NonNull Call<SearchAccountAPIResponse> call, @NonNull Response<SearchAccountAPIResponse> response) {
                final SearchAccountAPIResponse body = response.body();
                if (body == null) {
                    assert response.errorBody() != null;
                    try {
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
        final Call<GetWooContactsModel> searchResultCall = wooooService.getWooContacts(params);
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
        final Call<UpdateUserLanguageModel> searchResultCall = wooooService.updateUserLanguage(accountId, language, languageCode);
        searchResultCall.enqueue(new Callback<UpdateUserLanguageModel>() {
            @Override
            public void onResponse(@NonNull Call<UpdateUserLanguageModel> call, @NonNull Response<UpdateUserLanguageModel> response) {
                final UpdateUserLanguageModel body = response.body();
                if (body == null) {
                    assert response.errorBody() != null;
                    try {
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
        final Call<UserBasicInfo> searchResultCall = wooooService.updateProfile(user.accountId, user);
        searchResultCall.enqueue(new Callback<UserBasicInfo>() {
            @Override
            public void onResponse(@NonNull Call<UserBasicInfo> call, @NonNull Response<UserBasicInfo> response) {
                final UserBasicInfo body = response.body();
                if (body == null) {
                    assert response.errorBody() != null;
                    try {
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
        final Call<TextTranslateApiResponse> translateTextResultCall = wooooService.translateText(translateModel);
        translateTextResultCall.enqueue(new Callback<TextTranslateApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<TextTranslateApiResponse> call, @NonNull Response<TextTranslateApiResponse> response) {
                final TextTranslateApiResponse body = response.body();
                if (body == null) {
                    assert response.errorBody() != null;
                    try {
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


    public interface OnLoginAPiResult {
        <T> void onLoginApiResultFound(T result);
    }

    public interface OnSearchAccountAPiResult {
        <T> void onSearchAccountApiResultFound(T result);
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


    private BaseModelAPIResponse parseErrorBody(String response) {
        Log.d("parseErrorBody", response);
        Gson gson = new Gson();
        return gson.fromJson(response, BaseModelAPIResponse.class);
    }

}


