package com.delectable.mobile.ui.capture.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.CaptureComment;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.controllers.CaptureController;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.events.captures.AddCaptureCommentEvent;
import com.delectable.mobile.events.captures.DeletedCaptureEvent;
import com.delectable.mobile.events.captures.EditedCaptureCommentEvent;
import com.delectable.mobile.events.captures.LikedCaptureEvent;
import com.delectable.mobile.events.captures.RatedCaptureEvent;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.capture.activity.CaptureCommentRateActivity;
import com.delectable.mobile.ui.capture.widget.CaptureDetailsView;
import com.delectable.mobile.ui.profile.activity.UserProfileActivity;
import com.delectable.mobile.ui.wineprofile.activity.WineProfileActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import javax.inject.Inject;

public abstract class BaseCaptureDetailsFragment extends BaseFragment
        implements CaptureDetailsView.CaptureActionsHandler {

    private static final String TAG = BaseCaptureDetailsFragment.class.getSimpleName();

    private static final int REQUEST_DELETE_CONFIRMATION = 100;

    private static final int REQUEST_RATE_COMMENT_CAPTURE = 200;

    private static final int REQUEST_COMMENT_CAPTURE = 300;

    @Inject
    CaptureController mCaptureController;

    /**
     * Capture ready to be deleted or other things, for when the user either clicks OK.
     */
    private CaptureDetails mTempCaptureForAction;

    private CaptureComment mTempUserComment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public abstract void dataSetChanged();

    @Override
    public void writeCommentForCapture(CaptureDetails capture) {
        mTempCaptureForAction = capture;
        Intent intent = new Intent();
        intent.putExtra(CaptureCommentRateActivity.PARAMS_IS_RATING, false);
        intent.setClass(getActivity(), CaptureCommentRateActivity.class);
        startActivityForResult(intent, REQUEST_COMMENT_CAPTURE);
    }

    @Override
    public void rateAndCommentForCapture(CaptureDetails capture) {
        mTempCaptureForAction = capture;
        String userId = UserInfo.getUserId(getActivity());
        boolean isCurrentUserCapture = capture.getCapturerParticipant().getId()
                .equalsIgnoreCase(userId);
        ArrayList<CaptureComment> comments = capture.getCommentsForUserId(userId);
        mTempUserComment = comments.size() > 0 ? comments.get(0) : null;
        String currentUserCommentText = "";

        if (mTempUserComment != null && isCurrentUserCapture) {
            currentUserCommentText = mTempUserComment.getComment();
        }
        int currentUserRating = capture.getRatingForId(userId);

        Intent intent = new Intent();
        intent.putExtra(CaptureCommentRateActivity.PARAMS_IS_RATING, true);
        intent.putExtra(CaptureCommentRateActivity.PARAMS_RATING, currentUserRating);
        intent.putExtra(CaptureCommentRateActivity.PARAMS_COMMENT, currentUserCommentText);

        intent.setClass(getActivity(), CaptureCommentRateActivity.class);
        startActivityForResult(intent, REQUEST_RATE_COMMENT_CAPTURE);
    }

    @Override
    public void toggleLikeForCapture(final CaptureDetails capture) {
        final String userId = UserInfo.getUserId(getActivity());
        boolean userLikesCapture = !capture.doesUserLikeCapture(userId);
        capture.toggleUserLikesCapture(userId);
        dataSetChanged();
        mCaptureController.toggleLikeCapture(capture.getId(), userId, userLikesCapture);
    }

    private void sendComment(final CaptureDetails capture, String comment) {
        // TODO: Loader?
        // Temp comment for instant UI
        final CaptureComment tempComment = new CaptureComment();
        tempComment.setAccountId(UserInfo.getUserId(getActivity()));
        tempComment.setComment(comment);
        if (capture.getComments() == null) {
            capture.setComments(new ArrayList<CaptureComment>());
        }
        capture.getComments().add(tempComment);
        dataSetChanged();

        mCaptureController.addCommentToCapture(capture.getId(), comment);
    }

    private void editComment(CaptureDetails capture, final CaptureComment captureComment) {
        mCaptureController.editCaptureComment(capture.getId(), captureComment.getId(),
                captureComment.getComment());
    }

    @Override
    public void launchWineProfile(CaptureDetails capture) {
        // Don't launch the Wine Capture profile if the Wine is null, such as when the capture hasn't matched a Wine yet
        if (capture.getWineProfile() != null) {
            Intent intent = WineProfileActivity.newIntent(getActivity(), capture.getWineProfile(),
                    capture.getPhoto());
            startActivity(intent);
        }
    }

    @Override
    public void launchUserProfile(String userAccountId) {
        Intent intent = new Intent();
        intent.putExtra(UserProfileActivity.PARAMS_USER_ID, userAccountId);
        intent.setClass(getActivity(), UserProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void launchTaggedUserListing(CaptureDetails capture) {
        // TODO: Tagged User Listing if design exists
        Log.d(TAG, "Launch Extra Tagged User Listing Screen.");
    }

    @Override
    public void discardCaptureClicked(CaptureDetails capture) {
        mTempCaptureForAction = capture;
        showConfirmationNoTitle(getString(R.string.capture_remove), getString(R.string.remove),
                null, REQUEST_DELETE_CONFIRMATION);
    }

    public void deleteCapture(CaptureDetails capture) {
        mCaptureController.deleteCapture(capture.getId());
    }

    @Override
    public void editCapture(CaptureDetails capture) {
        // Not sure if this is what the edit icon does?
        rateAndCommentForCapture(capture);
    }

    private void sendRating(final CaptureDetails capture, final int rating) {
        String userId = UserInfo.getUserId(getActivity());
        // Instant UI update
        capture.updateRatingForUser(UserInfo.getUserId(getActivity()), rating);
        dataSetChanged();
        // update rated capture
        mCaptureController.rateCapture(capture.getId(), userId, rating);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DELETE_CONFIRMATION) {
            if (resultCode == Activity.RESULT_OK) {
                deleteCapture(mTempCaptureForAction);
            }
            mTempCaptureForAction = null;
        }

        if (requestCode == REQUEST_COMMENT_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                String commentText = data.getStringExtra(CaptureCommentRateFragment.DATA_COMMENT);
                Log.i(TAG, "Request Data Comment Text: " + commentText);
                sendComment(mTempCaptureForAction, commentText);
            }
            mTempCaptureForAction = null;
        }

        if (requestCode == REQUEST_RATE_COMMENT_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                String commentText = data.getStringExtra(CaptureCommentRateFragment.DATA_COMMENT);
                int rating = data.getIntExtra(CaptureCommentRateFragment.DATA_RATING, -1);
                Log.i(TAG, "Request Data Comment Text: " + commentText);
                Log.i(TAG, "Request Data Rating: " + rating);
                sendRating(mTempCaptureForAction, rating);
                if (mTempUserComment != null && mTempUserComment.getId() != null) {
                    mTempUserComment.setComment(commentText);
                    editComment(mTempCaptureForAction, mTempUserComment);
                    dataSetChanged();
                } else {
                    sendComment(mTempCaptureForAction, commentText);
                }
            }
            mTempUserComment = null;
            mTempCaptureForAction = null;
        }
    }

    //endregion

    //region EventBus Events
    public void onEventMainThread(AddCaptureCommentEvent event) {
        dataSetChanged();
    }

    public void onEventMainThread(EditedCaptureCommentEvent event) {
        dataSetChanged();
    }

    public void onEventMainThread(LikedCaptureEvent event) {
        dataSetChanged();
    }

    public void onEventMainThread(RatedCaptureEvent event) {
        dataSetChanged();
    }

    public void onEventMainThread(DeletedCaptureEvent event) {
        dataSetChanged();
    }
    //endregion
}
