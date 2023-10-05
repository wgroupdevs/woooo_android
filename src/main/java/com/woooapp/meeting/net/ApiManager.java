package com.woooapp.meeting.net;

import android.content.Context;
import androidx.annotation.NonNull;

import java.io.IOException;

import eu.siacs.conversations.BuildConfig;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
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

    private static final String USER_AGENT = String.format("%s[Android]-%s v%s",
            BuildConfig.APP_NAME, BuildConfig.BUILD_TYPE, BuildConfig.VERSION_NAME);

    /**
     * @param jsonBody
     * @param apiResultCallback
     */
    public void fetchCreatePreMeeting(@NonNull String jsonBody, @NonNull ApiResult2 apiResultCallback) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(jsonBody, mediaType);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(BASE_URL + EP_CREATE)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", USER_AGENT)
                .build();
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
     *
     * @param meetingId
     * @param apiResultCallback
     */
    public void fetchRoomData2(@NonNull String meetingId, @NonNull ApiResult2 apiResultCallback) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(BASE_URL + EP_GET_ROOM_DATA + meetingId)
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", USER_AGENT)
                .build();
        try {
            Response response = client.newCall(request).execute();
            apiResultCallback.onResult(null, response);
        } catch (IOException e) {
            e.printStackTrace();
            apiResultCallback.onFailure(null, e.getMessage());
        }
    }

    /**
     * @param jsonBody
     * @param meetingId
     * @param apiResultCallback
     */
    public void putMembersData(@NonNull String jsonBody, @NonNull String meetingId, @NonNull ApiResult2 apiResultCallback) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(jsonBody, mediaType);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(String.format("%s/meeting/%s", BASE_URL, meetingId))
                .method("PUT", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", USER_AGENT)
                .build();
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
