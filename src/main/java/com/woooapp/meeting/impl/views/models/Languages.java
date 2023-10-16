package com.woooapp.meeting.impl.views.models;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.woooapp.meeting.net.util.Json;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 9:52 am 13/10/2023
 * <code>class</code> Languages.java
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Languages implements Serializable {

    private List<Language> languages;

    /**
     *
     * @return
     */
    public List<Language> getLanguages() {
        return languages;
    }

    /**
     *
     * @param languages
     */
    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Language implements Serializable {
        private String code;
        private String name;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    } // end class

    /**
     *
     * @param json
     * @return
     * @throws IOException
     */
    public static Languages fromJson(@NonNull String json) throws IOException {
        return (Languages) Json.deserialize(Languages.class, json, true);
    }

} /** end class */
