package com.woooapp.meeting.net;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import eu.siacs.conversations.BuildConfig;

/**
 * @author Muneeb Ahmad
 * <p>
 * File ApiManager.java
 * Class [ApiManager]
 * on 08/09/2023 at 11:16 pm
 */
public final class ApiManager {

    private Context mContext;
    private final RequestQueue requestQueue;

    /**
     *
     * @param context
     */
    private ApiManager(Context context) {
        this.mContext = context;
        this.requestQueue = Volley.newRequestQueue(mContext);
    }

    public static final String BASE_URL = "https://wmediasoup.watchblock.net";

    /**
     * Should be called prior to creating a meeting
     */
    private static final String EP_CREATE = "/meeting/create";
    private static final String EP_GET_ROOM_DATA = "/meeting/";

    private static final String USER_AGENT = String.format("%s[Android]-%s v%s",
            BuildConfig.APP_NAME, BuildConfig.BUILD_TYPE, BuildConfig.VERSION_NAME);

    /**
     *
     * @param jsonBody
     * @param apiResultCallback
     */
    public void fetchCreatePreMeeting(@NonNull String jsonBody, @NonNull ApiResult apiResultCallback) {
        StringRequest strRequest = new StringRequest(
                Request.Method.POST,
                BASE_URL + EP_CREATE,
                apiResultCallback::onResult,
                apiResultCallback::onFailure) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return jsonBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-Agent", USER_AGENT);
                return headers;
            }
        };

        requestQueue.add(strRequest);
    }

    /**
     *
     * @param meetingId
     * @param apiResultCallback
     */
    public void fetchRoomData(@NonNull String meetingId, @NonNull ApiResult apiResultCallback) {
        StringRequest strRequest = new StringRequest(
                Request.Method.GET,
                BASE_URL + EP_GET_ROOM_DATA + meetingId,
                apiResultCallback::onResult,
                apiResultCallback::onFailure
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", USER_AGENT);
                return headers;
            }
        };

        requestQueue.add(strRequest);
    }

    /**
     *  Also Make Put Request To Server
     * {@link ApiManager#BASE_URL}/meeting/[meetingID] To Add Our Own Member Data. Sent Data
     * Should Look Like:
     * <p>
     * {
     *      accountUniqueId:data?.accountUniqueId,
     *      email:data?.email,
     *      username:data?.userName,
     *      picture:data?.imageURL,
     *      socketId:socket.id
     * }
     * </p>
     *
     * @param jsonBody
     * @param meetingId
     * @param apiResultCallback
     */
    public void putMembersData(@NonNull String jsonBody, @NonNull String meetingId, @NonNull ApiResult apiResultCallback) {
        StringRequest strRequest = new StringRequest(
                Request.Method.PUT,
                String.format("%s/meeting/%s", BASE_URL, meetingId),
                apiResultCallback::onResult,
                apiResultCallback::onFailure) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return jsonBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-Agent", USER_AGENT);
                return headers;
            }
        };
        requestQueue.add(strRequest);
    }

    public interface ApiResult {
        void onResult(Object response);
        void onFailure(VolleyError error);
    }

    /**
     *
     * @param context
     * @return
     */
    public static ApiManager build(final Context context) {
        synchronized (ApiManager.class) {
            return new ApiManager(context);
        }
    }

} /**
 * end class [ApiManager]
 */
