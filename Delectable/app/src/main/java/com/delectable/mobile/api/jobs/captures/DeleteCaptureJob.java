package com.delectable.mobile.api.jobs.captures;

import com.delectable.mobile.api.cache.CaptureDetailsModel;
import com.delectable.mobile.api.endpointmodels.ActionRequest;
import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.events.captures.DeletedCaptureEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class DeleteCaptureJob extends BaseJob {

    private static final String TAG = DeleteCaptureJob.class.getSimpleName();

    @Inject
    CaptureDetailsModel mCapturesModel;

    private String mCaptureId;

    public DeleteCaptureJob(String captureId) {
        super(new Params(Priority.SYNC.value()));
        mCaptureId = captureId;
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();
        String endpoint = "/captures/delete";

        ActionRequest request = new ActionRequest(mCaptureId);

        // Response has no payload, just "success"
        BaseResponse response = getNetworkClient().post(endpoint, request, BaseResponse.class);

        mCapturesModel.deleteCaptureDetails(mCaptureId);

        getEventBus().post(new DeletedCaptureEvent(true, mCaptureId));
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        if (getErrorMessage() != null) {
            getEventBus()
                    .post(new DeletedCaptureEvent(getErrorMessage(), mCaptureId, getErrorCode()));
        } else {
            getEventBus().post(new DeletedCaptureEvent(false, mCaptureId));
        }
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return super.shouldReRunOnThrowable(throwable);
    }
}
