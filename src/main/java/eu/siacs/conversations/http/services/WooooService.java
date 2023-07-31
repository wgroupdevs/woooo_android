package eu.siacs.conversations.http.services;

import eu.siacs.conversations.http.model.LoginAPIResponseJAVA;
import eu.siacs.conversations.http.model.SearchAccountAPIResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import woooo_app.woooo.data.models.auth.requestmodels.LoginRequestParams;

public interface WooooService {

    @POST("/api/Auth/login")
    Call<LoginAPIResponseJAVA> login(
            @Query("isLoginWithEmail") boolean isLoginWithEmail,
            @Body LoginRequestParams user
    );

    @GET("/api/v1/Account/SearchAccount")
    Call<SearchAccountAPIResponse> searchAccount(
            @Query("value") String value,
            @Query("isEmail") boolean isEmail
    );


}
