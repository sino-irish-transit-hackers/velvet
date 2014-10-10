package com.delectable.mobile.controllers;

import com.delectable.mobile.api.models.AccountConfig;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.Identifier;
import com.delectable.mobile.jobs.accounts.AddIdentifierJob;
import com.delectable.mobile.jobs.accounts.AssociateFacebookJob;
import com.delectable.mobile.jobs.accounts.AssociateTwitterJob;
import com.delectable.mobile.jobs.accounts.FacebookifyProfilePhotoJob;
import com.delectable.mobile.jobs.accounts.FetchAccountCapturesJob;
import com.delectable.mobile.jobs.accounts.FetchAccountPrivateJob;
import com.delectable.mobile.jobs.accounts.FetchAccountProfileJob;
import com.delectable.mobile.jobs.accounts.FetchAccountsFromContactsJob;
import com.delectable.mobile.jobs.accounts.FetchActivityFeedJob;
import com.delectable.mobile.jobs.accounts.FetchDelectafriendsJob;
import com.delectable.mobile.jobs.accounts.FetchFacebookSuggestionsJob;
import com.delectable.mobile.jobs.accounts.FetchFollowersJob;
import com.delectable.mobile.jobs.accounts.FetchFollowingsJob;
import com.delectable.mobile.jobs.accounts.FetchInfluencerSuggestionsJob;
import com.delectable.mobile.jobs.accounts.FetchTwitterSuggestionsJob;
import com.delectable.mobile.jobs.accounts.FollowAccountJob;
import com.delectable.mobile.jobs.accounts.RemoveIdentifierJob;
import com.delectable.mobile.jobs.accounts.SearchAccountsJob;
import com.delectable.mobile.jobs.accounts.UpdateIdentifierJob;
import com.delectable.mobile.jobs.accounts.UpdateProfileJob;
import com.delectable.mobile.jobs.accounts.UpdateProfilePhotoJob;
import com.delectable.mobile.jobs.accounts.UpdateSettingJob;
import com.delectable.mobile.model.api.accounts.AccountsCapturesRequest;
import com.path.android.jobqueue.JobManager;

import javax.inject.Inject;


public class AccountController {

    @Inject
    JobManager mJobManager;

    public void fetchProfile(String id) {
        mJobManager.addJobInBackground(new FetchAccountProfileJob(id));
    }

    public void fetchAccountPrivate(String id) {
        mJobManager.addJobInBackground(new FetchAccountPrivateJob(id));
    }

    public void fetchActivityFeed(String before, String after) {
        mJobManager.addJobInBackground(new FetchActivityFeedJob(before, after));
    }

    public void fetchFollowers(String accountId, BaseListingResponse<AccountMinimal> listing) {
        mJobManager.addJobInBackground(new FetchFollowersJob(accountId, listing));
    }

    public void fetchFollowings(String accountId, BaseListingResponse<AccountMinimal> listing) {
        mJobManager.addJobInBackground(new FetchFollowingsJob(accountId, listing));
    }

    /**
     * @param context         Context type for capture
     * @param accountId       Account that you want to fetch captures for.
     * @param listing         The previous ListingResponse if paginating. Pass in {@code null} if
     *                        making a fresh request.
     * @param isPullToRefresh true if user invoke this call via a pull to refresh.
     */
    public void fetchAccountCaptures(AccountsCapturesRequest.Context context, String accountId,
            BaseListingResponse<CaptureDetails> listing, Boolean isPullToRefresh) {
        mJobManager.addJobInBackground(
                new FetchAccountCapturesJob(context, accountId, listing, isPullToRefresh));
    }

    public void followAccount(String id, boolean follow) {
        mJobManager.addJobInBackground(new FollowAccountJob(id, follow));
    }

    public void fetchInfluencerSuggestions(String id) {
        mJobManager.addJobInBackground(new FetchInfluencerSuggestionsJob(id));
    }

    public void fetchFacebookSuggestions(String id) {
        mJobManager.addJobInBackground(new FetchFacebookSuggestionsJob(id));
    }

    public void fetchTwitterSuggestions(String id) {
        mJobManager.addJobInBackground(new FetchTwitterSuggestionsJob(id));
    }

    public void fetchDelectafriends() {
        mJobManager.addJobInBackground(new FetchDelectafriendsJob());
    }

    public void fetchAccountsFromContacts() {
        mJobManager.addJobInBackground(new FetchAccountsFromContactsJob());
    }

    public void searchAccounts(String query, int offset, int limit) {
        mJobManager.addJobInBackground(new SearchAccountsJob(query, offset, limit));
    }

    //region Settings Screen
    public void facebookifyProfilePhoto() {
        mJobManager.addJobInBackground(new FacebookifyProfilePhotoJob());
    }

    public void updateProfilePhoto(byte[] imageData) {
        mJobManager.addJobInBackground(new UpdateProfilePhotoJob(imageData));
    }

    public void updateProfile(String fname, String lname, String url, String bio) {
        mJobManager.addJobInBackground(new UpdateProfileJob(fname, lname, url, bio));
    }

    public void updateSetting(AccountConfig.Key key, boolean setting) {
        mJobManager.addJobInBackground(new UpdateSettingJob(key, setting));
    }

    public void addIdentifier(String string, Identifier.Type type) {
        mJobManager.addJobInBackground(new AddIdentifierJob(string, type, null));
    }

    public void updateIdentifier(Identifier identifier, String string) {
        mJobManager.addJobInBackground(new UpdateIdentifierJob(identifier, string, null));
    }

    public void removeIdentifier(Identifier identifier) {
        mJobManager.addJobInBackground(new RemoveIdentifierJob(identifier));
    }

    public void associateFacebook(String facebookToken, double facebookTokenExpiration) {
        mJobManager.addJobInBackground(
                new AssociateFacebookJob(facebookToken, facebookTokenExpiration));
    }

    public void associateTwitter(long twitterId, String token, String tokenSecret,
            String screenName) {
        mJobManager.addJobInBackground(
                new AssociateTwitterJob(twitterId, token, tokenSecret, screenName));
    }
    //endregion

}
