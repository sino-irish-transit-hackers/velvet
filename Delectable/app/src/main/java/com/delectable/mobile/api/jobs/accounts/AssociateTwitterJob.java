package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.endpointmodels.accounts.AccountPrivateResponse;
import com.delectable.mobile.api.endpointmodels.accounts.AccountsAssociateTwitterRequest;
import com.delectable.mobile.api.events.accounts.AssociateTwitterEvent;
import com.delectable.mobile.api.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.Account;
import com.path.android.jobqueue.Params;

public class AssociateTwitterJob extends BaseJob {

    private static final String TAG = AssociateTwitterJob.class.getSimpleName();

    private final long mTwitterId;

    private final String mToken;

    private final String mTokenSecret;

    private final String mScreenName;


    public AssociateTwitterJob(long twitterId, String token, String tokenSecret,
            String screenName) {
        super(new Params(Priority.UX.value()).requireNetwork());

        mTwitterId = twitterId;
        mToken = token;
        mTokenSecret = tokenSecret;
        mScreenName = screenName;
    }

    @Override
    public void onRun() throws Throwable {

        String endpoint = "/accounts/associate_twitter";
        AccountsAssociateTwitterRequest request = new AccountsAssociateTwitterRequest(mTwitterId,
                mToken, mTokenSecret, mScreenName);
        AccountPrivateResponse response = mNetworkClient
                .post(endpoint, request, AccountPrivateResponse.class);

        Account account = response.getPayload().getAccount();
        UserInfo.setAccountPrivate(account);
        mEventBus.post(new AssociateTwitterEvent(account));
        mEventBus.post(new UpdatedAccountEvent(account));
    }

    @Override
    protected void onCancel() {
        mEventBus.post(new AssociateTwitterEvent(TAG + " " + getErrorMessage()));
    }

}
