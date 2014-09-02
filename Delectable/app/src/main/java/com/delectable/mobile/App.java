package com.delectable.mobile;

import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.di.AppModule;
import com.kahuna.sdk.KahunaAnalytics;

import android.app.Application;
import android.util.Log;

import dagger.ObjectGraph;

public class App extends Application {

    private static final String TAG = App.class.getSimpleName();

    private static App sInstance;

    private ObjectGraph mObjectGraph;

    public App() {
        sInstance = this;
    }

    public static App getInstance() {
        return sInstance;
    }

    public static void injectMembers(Object object) {
        getInstance().mObjectGraph.inject(object);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            KahunaAnalytics
                    .onAppCreate(this, BuildConfig.KAHUNA_SECRET, BuildConfig.KAHUNA_PUSH_ID);
            KahunaAnalytics.setPushReceiver(PushReceiver.class);
            updateKahunaAttributes();
        } catch (Exception ex) {
            Log.wtf(TAG, "Kahuna Failed", ex);
        }

        mObjectGraph = ObjectGraph.create(new AppModule());
    }

    public void updateKahunaAttributes() {
        if (UserInfo.isSignedIn(this)) {
            String username = UserInfo.getUserId(this);
            String email = UserInfo.getUserEmail(this);

            KahunaAnalytics.setUsernameAndEmail(username, email);
            // TODO: Enable / Disable dialog in Settings?
            KahunaAnalytics.enablePush();
        }
    }
}
