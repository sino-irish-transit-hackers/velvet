package com.delectable.mobile.tests;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.Registration;
import com.delectable.mobile.data.UserInfo;

import android.content.Context;
import android.content.SharedPreferences;

public class UserInfoTest extends BaseAndroidTestCase {

    Registration mTestRegistration;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mTestRegistration = new Registration();
        mTestRegistration.setSessionToken("Session Token");
        mTestRegistration.setSessionKey("Session Key");
        mTestRegistration.setAccount(new Account());
        mTestRegistration.getAccount().setId("my user id");
    }

    @Override
    protected void tearDown() throws Exception {
        mTestRegistration = null;
        super.tearDown();
    }


    public void testOnSignIn() {
        UserInfo.onSignIn(getContext(), mTestRegistration);
        SharedPreferences prefs = getContext().getSharedPreferences(UserInfo.PREFERENCES,
                Context.MODE_PRIVATE);
        assertEquals(mTestRegistration.getSessionKey(), UserInfo.getSessionKey(getContext()));
        assertEquals(mTestRegistration.getSessionToken(), UserInfo.getSessionToken(getContext()));
        assertEquals(mTestRegistration.getAccount().getId(), UserInfo.getUserId(getContext()));

        assertEquals(3, prefs.getAll().size());
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
