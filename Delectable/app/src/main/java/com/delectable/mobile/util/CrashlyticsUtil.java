package com.delectable.mobile.util;

import com.crashlytics.android.Crashlytics;

public class CrashlyticsUtil {

    private static final String SESSION_KEY = "SESSION_KEY";

    public static void onSignIn(String name, String email, String userId, String sessionKey) {
        Crashlytics.setUserName(name);
        Crashlytics.setUserEmail(email);
        Crashlytics.setUserIdentifier(userId);
        Crashlytics.setString(SESSION_KEY, sessionKey);
    }

    public static void onSignOut() {
        Crashlytics.setUserName(null);
        Crashlytics.setUserEmail(null);
        Crashlytics.setUserIdentifier(null);
        Crashlytics.setString(SESSION_KEY, null);
    }

}
