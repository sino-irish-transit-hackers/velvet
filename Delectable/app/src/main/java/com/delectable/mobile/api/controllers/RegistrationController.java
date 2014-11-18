package com.delectable.mobile.api.controllers;

import com.delectable.mobile.api.jobs.registrations.LoginFacebookJob;
import com.delectable.mobile.api.jobs.registrations.LoginJob;
import com.delectable.mobile.api.jobs.registrations.RegisterJob;
import com.delectable.mobile.api.jobs.registrations.ResetPasswordJob;
import com.delectable.mobile.util.AnalyticsUtil;
import com.path.android.jobqueue.JobManager;

import javax.inject.Inject;


public class RegistrationController {

    private static final String TAG = RegistrationController.class.getSimpleName();

    @Inject
    JobManager mJobManager;

    @Inject
    AnalyticsUtil mAnalytics;

    public void register(String email, String password, String fname, String lname) {
        mJobManager.addJobInBackground(new RegisterJob(email, password, fname, lname));
    }

    public void login(String email, String password) {
        mJobManager.addJobInBackground(new LoginJob(email, password));
    }

    public void facebookLogin(String facebookToken, double facebookTokenExpiration) {
        mJobManager
                .addJobInBackground(new LoginFacebookJob(facebookToken, facebookTokenExpiration));
    }

    public void resetPassword(String email) {
        mJobManager.addJobInBackground(new ResetPasswordJob(email));
    }

}
