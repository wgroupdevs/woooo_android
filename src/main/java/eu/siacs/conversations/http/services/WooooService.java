package eu.siacs.conversations.http.services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

import woooo_app.woooo.data.models.auth.requestmodels.LoginRequestParams;

public interface WooooService {

    @POST("/api/Auth/login")
    Call<LoginAPIResponseJAVA> login(
            @Query("isLoginWithEmail") Boolean isLoginWithEmail,
            @Body LoginRequestParams user
    );
}
