package com.delectable.mobile.data;

import com.google.gson.Gson;

import com.delectable.mobile.App;
import com.delectable.mobile.api.models.Motd;

import android.content.Context;
import android.content.SharedPreferences;

public class UserInfo {

    public static final String PREFERENCES = "com.delectable.mobile.data.userinfo";

    private static final String PROPERTY_SESSION_TOKEN = "sessionToken";

    private static final String PROPERTY_SESSION_KEY = "sessionKey";

    private static final String PROPERTY_USER_ID = "userId";

    private static final String PROPERTY_USER_EMAIL = "userEmail";

    private static final String PROPERTY_MOTD = "motd";

    public static void onSignIn(String userId, String sessionKey, String sessionToken,
            String email) {
        SharedPreferences prefs = App.getInstance().getSharedPreferences(PREFERENCES,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_USER_ID, userId);
        editor.putString(PROPERTY_SESSION_KEY, sessionKey);
        editor.putString(PROPERTY_SESSION_TOKEN, sessionToken);
        editor.putString(PROPERTY_USER_EMAIL, email);
        editor.commit();
    }

    public static void onSignOut(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(PROPERTY_SESSION_KEY);
        editor.remove(PROPERTY_SESSION_TOKEN);
        editor.remove(PROPERTY_USER_ID);
        editor.remove(PROPERTY_USER_EMAIL);
        editor.remove(PROPERTY_MOTD);
        editor.commit();
    }

    public static void setMotd(Motd motd) {
        SharedPreferences prefs = App.getInstance().getSharedPreferences(PREFERENCES,
                Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonString = gson.toJson(motd);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_MOTD, jsonString);
        editor.commit();
    }

    public static boolean isSignedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getAll().size() > 0;
    }

    public static String getSessionToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getString(PROPERTY_SESSION_TOKEN, null);
    }

    public static String getSessionKey(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getString(PROPERTY_SESSION_KEY, null);
    }

    public static String getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getString(PROPERTY_USER_ID, null);
    }

    public static String getUserEmail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getString(PROPERTY_USER_EMAIL, null);
    }

    public static Motd getMotd(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        String jsonString = prefs.getString(PROPERTY_MOTD, null);
        if (jsonString != null) {
            Gson gson = new Gson();
            Motd motd = gson.fromJson(jsonString, Motd.class);
            return motd;
        }
        return null;
    }

}
