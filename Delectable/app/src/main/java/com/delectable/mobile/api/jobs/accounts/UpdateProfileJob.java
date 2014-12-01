package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.App;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.endpointmodels.accounts.AccountsUpdateProfileRequest;
import com.delectable.mobile.api.events.accounts.UpdatedProfileEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.Account;
import com.path.android.jobqueue.Params;

public class UpdateProfileJob extends BaseJob {

    private static final String TAG = UpdateProfileJob.class.getSimpleName();

    private String mFname;

    private String mLname;

    private String mUrl;

    private String mBio;

    public UpdateProfileJob(String fname, String lname, String url, String bio) {
        super(new Params(Priority.SYNC.value()).requireNetwork().persist());
        mFname = fname;
        mLname = lname;
        mUrl = url;
        mBio = bio;
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/update_profile";
        AccountsUpdateProfileRequest request = new AccountsUpdateProfileRequest(
                mFname, mLname, mUrl, mBio);
        BaseResponse response = getNetworkClient().post(endpoint, request,
                BaseResponse.class);

        Account currentUser = UserInfo.getAccountPrivate(App.getInstance());
        currentUser.setFname(mFname);
        currentUser.setLname(mLname);
        currentUser.setUrl(mUrl);
        currentUser.setBio(mBio);
        UserInfo.setAccountPrivate(currentUser);
        getEventBus().post(new UpdatedProfileEvent(mFname, mLname, mUrl, mBio));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new UpdatedProfileEvent(TAG + " " + getErrorMessage()));
    }
}
