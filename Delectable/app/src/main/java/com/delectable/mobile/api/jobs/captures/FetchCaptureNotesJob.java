package com.delectable.mobile.api.jobs.captures;

import com.delectable.mobile.api.endpointmodels.ListingResponse;
import com.delectable.mobile.api.endpointmodels.captures.CapturesNotesRequest;
import com.delectable.mobile.api.events.captures.FetchedCaptureNotesEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.CaptureNote;
import com.google.gson.reflect.TypeToken;
import com.path.android.jobqueue.Params;

import java.lang.reflect.Type;

public class FetchCaptureNotesJob extends BaseJob {

    private static final String TAG = FetchCaptureNotesJob.class.getSimpleName();

    private String mBaseWineId;

    private String mWineProfileId;

    private String mBefore;

    private String mAfter;

    private String mIncludeCaptureNote;

    public FetchCaptureNotesJob(String baseWineId, String wineProfileId, String before, String after,
                                String includeCaptureNote) {
        super(new Params(Priority.SYNC).requireNetwork().persist());

        mBaseWineId = baseWineId;
        mWineProfileId = wineProfileId;
        mBefore = before;
        mAfter = after;
        mIncludeCaptureNote = includeCaptureNote;
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/captures/notes";
        CapturesNotesRequest request = new CapturesNotesRequest(mBaseWineId, mWineProfileId,
                mBefore, mAfter, mIncludeCaptureNote);
        Type type = new TypeToken<ListingResponse<CaptureNote>>() {
        }.getType();
        ListingResponse<CaptureNote> response = getNetworkClient()
                .post(endpoint, request, type);
        getEventBus().post(new FetchedCaptureNotesEvent(response.getPayload()));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new FetchedCaptureNotesEvent(TAG + " " + getErrorMessage()));
    }


}
