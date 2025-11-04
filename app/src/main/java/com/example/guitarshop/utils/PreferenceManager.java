package com.example.guitarshop.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static final String PREF_NAME = "GuitarShopPrefs";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_USER_ID = "userId";

    public static void saveToken(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(KEY_TOKEN, token).apply();
    }

    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public static void clearToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(KEY_TOKEN).apply();
    }
    public static void saveUserId(Context context, int userId) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit().putInt(KEY_USER_ID, userId).apply();
    }

    public static int getUserId(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getInt(KEY_USER_ID, 0);
    }
}
