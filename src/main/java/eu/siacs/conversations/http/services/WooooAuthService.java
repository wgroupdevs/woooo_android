package eu.siacs.conversations.http.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.common.base.Strings;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.Executors;

import eu.siacs.conversations.Config;
import eu.siacs.conversations.http.HttpConnectionManager;
import eu.siacs.conversations.services.XmppConnectionService;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import woooo_app.woooo.data.models.auth.requestmodels.LoginRequestParams;

public class WooooAuthService {

    private final XmppConnectionService service;
    private WooooService wooooService;

    public WooooAuthService(XmppConnectionService service) {
        this.service = service;
    }

    public void initializeWOOOOService() {
        if (Strings.isNullOrEmpty(Config.CHANNEL_DISCOVERY)) {
            this.wooooService = null;
            return;
        }
        final OkHttpClient.Builder builder = HttpConnectionManager.OK_HTTP_CLIENT.newBuilder();
        if (service.useTorToConnect()) {
            builder.proxy(HttpConnectionManager.getProxy());
        }
        final Retrofit retrofit =
                new Retrofit.Builder()
                        .client(builder.build())
                        .baseUrl(Config.WOOOO_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .callbackExecutor(Executors.newSingleThreadExecutor())
                        .build();
        this.wooooService = retrofit.create(WooooService.class);
    }


    public void login(Boolean isLoginWithEmail,String email,String phone, String password, OnLoginAPiResult listener) {
        Log.d("WooooAuthService",
                "LOGIN STARTED...");
        final LoginRequestParams requestParams =
                new LoginRequestParams(email, phone, password, "", "", "");
        final Call<LoginAPIResponseJAVA> searchResultCall =
                wooooService.login(isLoginWithEmail, requestParams);
        searchResultCall.enqueue(
                new Callback<LoginAPIResponseJAVA>() {
                    @Override
                    public void onResponse(
                            @NonNull Call<LoginAPIResponseJAVA> call,
                            @NonNull Response<LoginAPIResponseJAVA> response) {
                        final LoginAPIResponseJAVA body = response.body();


                        Log.d("WooooAuthService",
                                "API RESPONSE " + response.isSuccessful());
                        Log.d("WooooAuthService",
                                "API RESPONSE " + response.code());
                        Log.d("WooooAuthService",
                                "API RESPONSE BODY " + response.body());
                        if (body == null) {

                            Log.d("WooooAuthService",
                                    "API RESPONSE ");
                            assert response.errorBody() != null;
                            try {
                                String errorBodyFound=response.errorBody().byteString().utf8();

                                Log.d("WooooAuthService",
                                        "API RESPONSE " + errorBodyFound);

                                listener.onLoginApiResultFound(parseErrorBody(errorBodyFound));
                            } catch (IOException e) {

                                Log.d("WooooAuthService",
                                        "API Exception " + e.getStackTrace().toString());

                                throw new RuntimeException(e);
                            }

                        } else {

                            listener.onLoginApiResultFound(body);
                        }

                    }

                    @Override
                    public void onFailure(
                            @NonNull Call<LoginAPIResponseJAVA> call,
                            @NonNull Throwable throwable) {
                        Log.d(
                                Config.LOGTAG,
                                "Unable to query WoooService on " + Config.WOOOO_BASE_URL,
                                throwable);
//                        listener.);
                    }
                });
    }


    public interface OnLoginAPiResult {
        <T> void onLoginApiResultFound(T loginModel);
    }


    private BaseModelAPIResponse parseErrorBody(String response) {
        Log.d("parseErrorBody",response);
        Gson gson = new Gson();
        return gson.fromJson(response, BaseModelAPIResponse.class);
    }

}


