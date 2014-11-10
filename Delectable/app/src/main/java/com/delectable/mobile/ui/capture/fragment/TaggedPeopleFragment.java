package com.delectable.mobile.ui.capture.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.CaptureDetailsModel;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.capture.widget.TaggedPeopleAdapter;
import com.delectable.mobile.ui.profile.activity.UserProfileActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TaggedPeopleFragment extends BaseFragment {

    private static final String TAG = TaggedPeopleFragment.class.getSimpleName();

    public static final String PARAMS_CAPTURE_ID = "PARAMS_CAPTURE_ID";

    @InjectView(R.id.list_view)
    protected ListView mListView;

    @Inject
    CaptureDetailsModel mCaptureDetailsModel;

    private String mCaptureId;

    private CaptureDetails mCaptureDetails;

    private TaggedPeopleAdapter mAdapter;

    public static TaggedPeopleFragment newInstance(String captureId) {
        TaggedPeopleFragment fragment = new TaggedPeopleFragment();
        App.injectMembers(fragment);
        Bundle args = new Bundle();
        args.putString(PARAMS_CAPTURE_ID, captureId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        enableBackButton(true);
        int numberOfTaggedPeople = mCaptureDetails.getRegisteredParticipants() != null
                ? mCaptureDetails.getRegisteredParticipants().size()
                : 0;
        String capturerName = mCaptureDetails.getCapturerParticipant().getFname();
        String title = getResources()
                .getQuantityString(R.plurals.cap_feed_tagged_count, numberOfTaggedPeople,
                        numberOfTaggedPeople, capturerName);
        setActionBarSubtitle(title);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
        mCaptureId = getArguments().getString(PARAMS_CAPTURE_ID);
        mCaptureDetails = mCaptureDetailsModel.getCapture(mCaptureId);
        if (mCaptureDetails == null) {
            Log.e(TAG, "could not get capture details from cache");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_people, container, false);
        ButterKnife.inject(this, view);

        mAdapter = new TaggedPeopleAdapter(mCaptureDetails);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AccountMinimal account = mAdapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra(UserProfileActivity.PARAMS_USER_ID, account.getId());
                intent.setClass(getActivity(), UserProfileActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
