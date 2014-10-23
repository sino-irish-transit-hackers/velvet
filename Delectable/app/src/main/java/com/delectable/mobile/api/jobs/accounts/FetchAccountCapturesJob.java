package com.delectable.mobile.api.jobs.accounts;

import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.cache.CaptureListingModel;
import com.delectable.mobile.api.endpointmodels.ListingResponse;
import com.delectable.mobile.api.endpointmodels.captures.CapturesContext;
import com.delectable.mobile.api.jobs.BaseFetchListingJob;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.Listing;

import java.lang.reflect.Type;

import javax.inject.Inject;

public class FetchAccountCapturesJob extends BaseFetchListingJob<CaptureDetails> {

    private static final String TAG = FetchAccountCapturesJob.class.getSimpleName();

    @Inject
    protected CaptureListingModel mListingModel;

    @Override
    public String getEndpoint() {
        return "/accounts/captures";
    }

    @Override
    public Listing<CaptureDetails> getCachedListing(String dataItemId) {
        return mListingModel.getUserCaptures(dataItemId);
    }

    @Override
    public void saveListingToCache(String dataItemId, Listing<CaptureDetails> apiListing) {
        mListingModel.saveUserCaptures(dataItemId, apiListing);
    }

    @Override
    public Type getResponseType() {
        Type type = new TypeToken<ListingResponse<CaptureDetails>>() {
        }.getType();
        return type;
    }

    /**
     * @param accountId       Account that you want to fetch captures for.
     * @param captureListing  The previous ListingResponse if paginating. Pass in {@code null} if
     *                        making a fresh request.
     * @param isPullToRefresh true if user invoke this call via a pull to refresh.
     */
    public FetchAccountCapturesJob(String requestId, CapturesContext context, String accountId,
            Listing<CaptureDetails> captureListing,
            Boolean isPullToRefresh) {
        super(requestId, context.toString(), accountId, captureListing, isPullToRefresh);
    }
}
