package com.delectable.mobile.api.jobs.scanwinelabel;

import com.delectable.mobile.api.events.scanwinelabel.AddedCaptureFromPendingCaptureEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.endpointmodels.captures.CaptureDetailsResponse;
import com.delectable.mobile.api.endpointmodels.scanwinelabels.AddCaptureFromPendingCaptureRequest;
import com.delectable.mobile.util.KahunaUtil;
import com.path.android.jobqueue.Params;

import android.util.Log;

import java.util.Calendar;

public class AddCaptureFromPendingCaptureJob extends BaseJob {

    private static final String TAG = AddCaptureFromPendingCaptureJob.class.getSimpleName();

    private AddCaptureFromPendingCaptureRequest mRequest;

    public AddCaptureFromPendingCaptureJob(AddCaptureFromPendingCaptureRequest request) {
        super(new Params(Priority.SYNC).persist());
        // TODO: Possibly Save the request and then load the request from a cache to perform "async" requests?
        mRequest = request;
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();

        String endpoint = "/captures/from_pending_capture";
        mRequest.setContext("details");
        CaptureDetailsResponse response = getNetworkClient().post(endpoint, mRequest,
                CaptureDetailsResponse.class);

        Log.d(TAG, "Added new Capture: " + response.getCapture());
        getEventBus().post(new AddedCaptureFromPendingCaptureEvent(response.getCapture()));
        KahunaUtil.trackCreateCapture(Calendar.getInstance().getTime(),
                response.getCapture().getId());
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        getEventBus()
                .post(new AddedCaptureFromPendingCaptureEvent(getErrorMessage(), getErrorCode()));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return super.shouldReRunOnThrowable(throwable);
    }
}
