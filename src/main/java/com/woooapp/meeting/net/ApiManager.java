package com.woooapp.meeting.net;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fasterxml.jackson.databind.util.JSONPObject;

import org.bouncycastle.cert.ocsp.Req;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import eu.siacs.conversations.BuildConfig;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Muneeb Ahmad
 * <p>
 * File ApiManager.java
 * Class [ApiManager]
 * on 08/09/2023 at 11:16 pm
 */
public final class ApiManager {

    private static final String TAG = ApiManager.class.getSimpleName() + ".java";
    private Context mContext;

    /**
     * @param context
     */
    private ApiManager(Context context) {
        this.mContext = context;
    }

    public static final String BASE_URL = "https://wmediasoup.watchblock.net";

    /**
     * Should be called prior to creating a meeting
     */
    private static final String EP_CREATE = "/meeting/create";
    private static final String EP_GET_ROOM_DATA = "/meeting/";
    public static final String URL_MIC_MUTED = "https://wmediasoup.watchblock.net/meeting/mic/push";
    public static final String URL_MIC_UN_MUTED = "https://wmediasoup.watchblock.net/meeting/mic/pull";
    public static final String URL_HAND_RAISED = "https://wmediasoup.watchblock.net/meeting/hand/push";
    public static final String URL_HAND_LOWERED = "https://wmediasoup.watchblock.net/meeting/mic/pull";
    public static final String URL_CHAT_TRANSLATION = "https://wooooapi.watchblock.net/api/v1/Chat/TranslateText";

    private static final String USER_AGENT = String.format("%s[Android]-%s v%s",
            BuildConfig.APP_NAME, BuildConfig.BUILD_TYPE, BuildConfig.VERSION_NAME);

    /**
     * @param jsonBody
     * @param apiResultCallback
     */
    public void fetchCreatePreMeeting(@NonNull String jsonBody, @NonNull ApiResult2 apiResultCallback) {
        String url = BASE_URL + EP_CREATE;
        OkHttpClient client = new OkHttpClient.Builder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(jsonBody, mediaType);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", USER_AGENT)
                .build();

        Log.v(TAG, "[POST] : " + url);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                apiResultCallback.onFailure(call, e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                apiResultCallback.onResult(call, response);
            }
        });
    }

    /**
     * @param meetingId
     * @param apiResultCallback
     */
    public void fetchRoomData2(@NonNull String meetingId, @NonNull ApiResult2 apiResultCallback) {
        final String url = BASE_URL + EP_GET_ROOM_DATA + meetingId;
        OkHttpClient client = new OkHttpClient.Builder().build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", USER_AGENT)
                .build();

        Log.v(TAG, "[GET] : " + url);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                apiResultCallback.onFailure(call, e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                apiResultCallback.onResult(call, response);
            }
        });
    }

    /**
     * @param jsonBody
     * @param meetingId
     * @param apiResultCallback
     */
    public void putMembersData(@NonNull String jsonBody, @NonNull String meetingId, @NonNull ApiResult2 apiResultCallback) {
        final String url = String.format("%s/meeting/%s", BASE_URL, meetingId);
        OkHttpClient client = new OkHttpClient.Builder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(jsonBody, mediaType);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .method("PUT", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", USER_AGENT)
                .build();

        Log.v(TAG, "[PUT] : " + url);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                apiResultCallback.onFailure(call, e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                apiResultCallback.onResult(call, response);
            }
        });
    }

    /**
     * @param url
     * @param meetingId
     * @param socketId
     * @param callback
     */
    public void putStates(@NonNull String url, @NonNull String meetingId, @NonNull String socketId, @Nullable ApiResult2 callback) {
        final String url2 = String.format("%s/%s", url, meetingId);
        OkHttpClient client = new OkHttpClient.Builder().build();
        MediaType mediaType = MediaType.parse("application/json");
        try {
            JSONObject obj = new JSONObject();
            obj.put("user", socketId);

            RequestBody body = RequestBody.create(String.valueOf(obj), mediaType);
            Request request = new Request.Builder()
                    .url(url2)
                    .method("PUT", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-Agent", USER_AGENT)
                    .build();

            Log.v(TAG, "[PUT] : " + url2);

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    if (callback != null) callback.onFailure(call, e.getMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (callback != null) callback.onResult(call, response);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param text
     * @param languageCode
     */
    public void getTranslation(@NonNull String text, @NonNull String languageCode, @Nullable ApiResult2 callback) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        MediaType mediaType = MediaType.parse("application/json");
        try {
            JSONObject obj = new JSONObject();
            obj.put("text", text);
            obj.put("languageCode", languageCode);

            RequestBody reqBody = RequestBody.create(String.valueOf(obj), mediaType);
            Request request = new Request.Builder()
                    .url(URL_CHAT_TRANSLATION)
                    .method("POST", reqBody)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-Agent", USER_AGENT)
                    .build();

            Log.v(TAG, "[POST] : " + URL_CHAT_TRANSLATION);

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    if (callback != null) callback.onFailure(call, e.getMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (callback != null) callback.onResult(call, response);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param meetingId
     * @param password
     * @param callback
     */
    public void applyPassword(@NonNull String meetingId,
                              @NonNull String password,
                              @Nullable ApiResult2 callback) {
        final String url = String.format("%s/meeting/password/%s", BASE_URL, meetingId);
        OkHttpClient client = new OkHttpClient.Builder().build();
        MediaType mediaType = MediaType.parse("application/json");
        try {
            JSONObject obj = new JSONObject();
            obj.put("password", password);

            RequestBody body = RequestBody.create(String.valueOf(obj), mediaType);

            Request request = new Request.Builder()
                    .url(url)
                    .method("PUT", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-Agent", USER_AGENT)
                    .build();

            Log.v(TAG, "[PUT] : " + url);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    if (callback != null) callback.onFailure(call, e.getMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (callback != null) callback.onResult(call, response);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param meetingId
     * @param callback
     */
    public void checkForPassword(@NonNull String meetingId, @Nullable ApiResult2 callback) {
        final String url = String.format("%s/meeting/checkPass/%s", BASE_URL, meetingId);
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", USER_AGENT)
                .build();

        Log.v(TAG, "[GET] : " + url);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (callback != null) callback.onFailure(call, e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (callback != null) callback.onResult(call, response);
            }
        });
    }

    /**
     *
     * @param meetingId
     * @param password
     * @param callback
     */
    public void confirmPassword(@NonNull String meetingId,
                                @NonNull String password,
                                @Nullable ApiResult2 callback) {
       try {
           final String url = String.format("%s/meeting/confirmPass/%s", BASE_URL, meetingId);
           JSONObject obj = new JSONObject();
           obj.put("password", password);

           OkHttpClient client = new OkHttpClient.Builder().build();
           MediaType mediaType = MediaType.parse("application/json");

           RequestBody body = RequestBody.create(String.valueOf(obj), mediaType);

           Request request = new Request.Builder()
                   .url(url)
                   .method("POST", body)
                   .addHeader("Content-Type", "application/json")
                   .addHeader("User-Agent", USER_AGENT)
                   .build();

           Log.v(TAG, "[POST] : " + url);

           client.newCall(request).enqueue(new Callback() {
               @Override
               public void onFailure(@NonNull Call call, @NonNull IOException e) {
                   if (callback != null) callback.onFailure(call, e.getMessage());
               }

               @Override
               public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (callback != null) callback.onResult(call, response);
               }
           });
       } catch (JSONException ex) {
           ex.printStackTrace();
       }
    }

    public interface ApiResult2 {
        void onResult(Call call, Response response);

        void onFailure(Call call, Object e);
    }

    /**
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
