package com.delectable.mobile.tests;

import com.delectable.mobile.api.models.Registration;
import com.delectable.mobile.data.UserInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.test.AndroidTestCase;

/**
 * Created by abednarek on 5/22/14.
 */
public class UserInfoTest extends AndroidTestCase {

    Registration mTestRegistration;

    @Override
    protected void setUp() throws Exception {
        clearPrefs();
        mTestRegistration = new Registration();
        mTestRegistration.setSessionToken("Session Token");
        mTestRegistration.setSessionKey("Session Key");
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        clearPrefs();
        mTestRegistration = null;
        super.tearDown();
    }

    private void clearPrefs() {
        SharedPreferences prefs = getContext().getSharedPreferences(UserInfo.PREFERENCES,
                Context.MODE_PRIVATE);
        prefs.edit().clear().commit();
    }

    public void testOnSignIn() {
        UserInfo.onSignIn(getContext(), mTestRegistration);
        SharedPreferences prefs = getContext().getSharedPreferences(UserInfo.PREFERENCES,
                Context.MODE_PRIVATE);
        assertEquals(mTestRegistration.getSessionKey(), UserInfo.getSessionKey(getContext()));
        assertEquals(mTestRegistration.getSessionToken(), UserInfo.getSessionToken(getContext()));

        assertEquals(2, prefs.getAll().size());
    }

    public void testOnSignOut() {
        UserInfo.onSignIn(getContext(), mTestRegistration);
        UserInfo.onSignOut(getContext());
        SharedPreferences prefs = getContext().getSharedPreferences(UserInfo.PREFERENCES,
                Context.MODE_PRIVATE);
        assertNull(UserInfo.getSessionKey(getContext()));
        assertNull(UserInfo.getSessionToken(getContext()));

        assertEquals(0, prefs.getAll().size());
    }

    public void testIsSignedIn() {
        assertFalse(UserInfo.isSignedIn(getContext()));

        UserInfo.onSignIn(getContext(), mTestRegistration);
        assertTrue(UserInfo.isSignedIn(getContext()));

        UserInfo.onSignOut(getContext());
        assertFalse(UserInfo.isSignedIn(getContext()));
    }
}
