package com.alphawallet.app.repository;

import android.content.Context;

import com.alphawallet.app.entity.LocaleItem;

import java.util.ArrayList;

public interface LocaleRepositoryType {
    String getUserPreferenceLocale();
    void setUserPreferenceLocale(String locale);

    void setLocale(Context context, String locale);

    ArrayList<LocaleItem> getLocaleList(Context context);

    String getActiveLocale();

    boolean isLocalePresent(String locale);
}
