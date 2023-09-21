package com.woooapp.meeting.lib;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Muneeb Ahmad
 * <p>
 * File JsonUtils.java
 * Class [JsonUtils]
 * on 08/09/2023 at 5:12 pm
 */
public class JsonUtils {

    public static void jsonPut(JSONObject json, String key, Object value) {
        try {
            json.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public static JSONObject toJsonObject(String data) {
        try {
            return new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    @NonNull
    public static JSONArray toJsonArray(String data) {
        try {
            return new JSONArray(data);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

} /**
 * end class [JsonUtils]
 */
