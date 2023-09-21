package com.woooapp.meeting.net.util;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author Muneeb Ahmad
 * <p>
 * File Json.java
 * Class [Json]
 * on 09/09/2023 at 12:01 am
 */
public final class Json {

    /**
     *
     * @param k
     * @return
     * @param <K>
     * @throws JsonProcessingException
     */
    public static <K> String serailize(@NonNull K k) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(k);
    }

    /**
     *
     * @param clz
     * @param jsonStr
     * @param enableSingleValueAsArray
     * @return
     * @param <T>
     * @throws IOException
     */
    public static <T> T deserialize(Class<?> clz, @NonNull String jsonStr, boolean enableSingleValueAsArray) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        if (enableSingleValueAsArray) {
            mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        }
        return (T) mapper.readValue(jsonStr, clz);
    }

    /**
     *
     * @param clz
     * @param jsonStr
     * @param enableSingleValueAsArray
     * @return
     */
    public static Object[] deserializeArr(Class<?> clz, @NonNull String jsonStr, boolean enableSingleValueAsArray) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        if (enableSingleValueAsArray)
            mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        return (Object[]) mapper.readValue(jsonStr, clz);
    }

    /**
     * Check whether provided String is a valid JSON String or not.
     *
     * @param jsonString
     * @return
     */
    public static boolean isValidJson(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readTree(jsonString);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

} /**
 * end class [Json]
 */
