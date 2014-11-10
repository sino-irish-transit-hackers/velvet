package com.delectable.mobile.ui.wineprofile.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.BaseWineModel;
import com.delectable.mobile.api.cache.CaptureNoteListingModel;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.controllers.BaseWineController;
import com.delectable.mobile.api.controllers.CaptureController;
import com.delectable.mobile.api.events.UpdatedListingEvent;
import com.delectable.mobile.api.events.captures.MarkedCaptureHelpfulEvent;
import com.delectable.mobile.api.events.wines.UpdatedBaseWineEvent;
import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.api.models.BaseWineMinimal;
import com.delectable.mobile.api.models.CaptureNote;
import com.delectable.mobile.api.models.Listing;
import com.delectable.mobile.api.models.PhotoHash;
import com.delectable.mobile.api.models.Ratingsable;
import com.delectable.mobile.api.models.VarietalsHash;
import com.delectable.mobile.api.models.WineProfileMinimal;
import com.delectable.mobile.api.models.WineProfileSubProfile;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.capture.activity.CaptureDetailsActivity;
import com.delectable.mobile.ui.common.widget.InfiniteScrollAdapter;
import com.delectable.mobile.ui.common.widget.MutableForegroundColorSpan;
import com.delectable.mobile.ui.common.widget.WineBannerView;
import com.delectable.mobile.ui.profile.activity.UserProfileActivity;
import com.delectable.mobile.ui.wineprofile.dialog.ChooseVintageDialog;
import com.delectable.mobile.ui.wineprofile.widget.CaptureNotesAdapter;
import com.delectable.mobile.ui.wineprofile.widget.WineProfileCommentUnitRow;
import com.delectable.mobile.util.HideableActionBarScrollListener;
import com.delectable.mobile.util.KahunaUtil;
import com.delectable.mobile.util.SafeAsyncTask;
import com.melnykov.fab.FloatingActionButton;

import org.apache.commons.lang3.StringUtils;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

//TODO paginate capturenotes listview? go to another screen?
public class WineProfileFragment extends BaseFragment implements
        WineProfileCommentUnitRow.ActionsHandler, InfiniteScrollAdapter.ActionsHandler {

    public static final int ACTIONBAR_HIDE_DELAY = 1000;

    private static final int ACTIONBAR_TRANSITION_ANIM_DURATION = 300;

    public static final String TAG = WineProfileFragment.class.getSimpleName();

    private static final int NO_AVG_RATING = -1;

    private static final String WINE_PROFILE = "wineProfile";

    private static final String PHOTO_HASH = "photoHash";

    private static final String BASE_WINE = "baseWine";

    private static final String BASE_WINE_MINIMAL = "baseWineMinimal";

    private static final String BASE_WINE_ID = "baseWineId";

    private static final String VINTAGE_ID = "vintageId";

    private static final int CHOOSE_VINTAGE_DIALOG = 1;

    private static final String BASE_WINE_NOTES_REQ = "base_wine_notes_req";

    private static final String WINE_PROFILE_NOTES_REQ = "wine_profile_notes_req";

    @Inject
    protected BaseWineController mBaseWineController;

    @Inject
    protected CaptureController mCaptureController;

    @Inject
    protected BaseWineModel mBaseWineModel;

    @InjectView(R.id.wine_banner_view)
    protected WineBannerView mBanner;

    @InjectView(R.id.varietal_container)
    protected View mVarietalContainer;

    @InjectView(R.id.varietal_color_icon)
    protected ImageView mVarietalImageView;

    @InjectView(R.id.varietal_name)
    protected TextView mVarietalTextView;

    @InjectView(R.id.region_path_name)
    protected TextView mRegionPathTextView;

    @InjectView(R.id.all_ratings_average)
    protected TextView mAllRatingsAverageTextView;

    @InjectView(R.id.all_ratings_count)
    protected TextView mAllRatingsCountTextView;

    @InjectView(R.id.pro_ratings_average)
    protected TextView mProRatingsAverageTextView;

    @InjectView(R.id.pro_ratings_count)
    protected TextView mProRatingsCountTextView;

    @InjectView(R.id.all_years_textview)
    protected TextView mAllYearsTextView;

    @InjectView(R.id.empty_view_wine_profile)
    protected View mEmptyView;

    protected FloatingActionButton mCameraButton;

    private MutableForegroundColorSpan mAlphaSpan;

    private SpannableString mTitle;

    private CaptureNotesAdapter mAdapter = new CaptureNotesAdapter(this, this);

    private WineProfileMinimal mWineProfile;

    private PhotoHash mCapturePhotoHash;

    private String mBaseWineId;

    private String mVintageId;

    private BaseWine mBaseWine;

    /**
     * Keep track of whether we're fetching for baseWine or wineProfile capture notes.
     */
    private Type mType = Type.BASE_WINE;

    /**
     * The id that we're fetching captureNotes with. Can be a baseWineId or a wineProfileId.
     */
    private String mFetchingId;

    /**
     * If this is not null, then it means this fragment was spawned from Search Wines.
     */
    private BaseWineMinimal mBaseWineMinimal;


    private Listing<CaptureNote> mCaptureNoteListing;

    /**
     * these maps are used to retain references to {@link CaptureNote} objects expecting updates to
     * their helpful status. This way, when the {@link MarkedCaptureHelpfulEvent} returns, we don't
     * have to iterate through our {@link CaptureNote} list to find the {@link CaptureNote} object
     * to modify.
     */
    private HashMap<String, CaptureNote> mCaptureNotesExpectingUpdate
            = new HashMap<String, CaptureNote>();

    private HashMap<String, Boolean> mCaptureNoteExpectedHelpfulStatus
            = new HashMap<String, Boolean>();

    private boolean mViewWineTracked = false;

    private boolean mFetching;


    /**
     * Returns fragment that uses the provided {@link PhotoHash} as it's wine image. Uses {@code
     * wineProfile} to populate some basic wine metadata and inits request for {@link CaptureNote
     * CaptureNotes} using the {@code baseWineId} in {@link WineProfileMinimal}.
     *
     * @param capturePhotoHash {@link com.delectable.mobile.api.models.CaptureDetails
     *                         CaptureDetails}' PhotoHash. Pass in null to use WineProfile's image.
     */
    //TODO would be cleaner if CaptureDetail was passed in here, but it doesn't implement parcelable yet
    public static WineProfileFragment newInstance(WineProfileMinimal wineProfile,
            PhotoHash capturePhotoHash) {
        WineProfileFragment fragment = new WineProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(WINE_PROFILE, wineProfile);
        args.putParcelable(PHOTO_HASH, capturePhotoHash);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Launching from WineCaptureSubmitFragment.
     */
    public static WineProfileFragment newInstance(BaseWine baseWine) {
        WineProfileFragment fragment = new WineProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(BASE_WINE, baseWine);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Search Wines uses this method to make make it's {@link WineProfileFragment}.
     */
    public static WineProfileFragment newInstance(BaseWineMinimal baseWine) {
        WineProfileFragment fragment = new WineProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(BASE_WINE_MINIMAL, baseWine);
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * {@link WineProfileFragment} accessed by deep links will use this method to start up the
     * fragment. It can contain either just {@code baseWineId}, or both {@code baseWineId} and
     * {@code vintageId} (wine profile id). If the latter, then we must show {@link CaptureNote
     * CaptureNotes} for that {@link WineProfileMinimal}.
     *
     * @param baseWineId required
     * @param vintageId  optional
     */
    public static WineProfileFragment newInstance(String baseWineId, String vintageId) {
        WineProfileFragment fragment = new WineProfileFragment();
        Bundle args = new Bundle();
        args.putString(BASE_WINE_ID, baseWineId);
        args.putString(VINTAGE_ID, vintageId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
        Bundle args = getArguments();
        if (args != null) {
            //spawned from Feed Fragment
            mWineProfile = args.getParcelable(WINE_PROFILE);
            mCapturePhotoHash = args.getParcelable(PHOTO_HASH);

            //spawned from Search
            mBaseWineMinimal = args.getParcelable(BASE_WINE_MINIMAL);

            //spawned from WineCaptureSubmit
            mBaseWine = args.getParcelable(BASE_WINE);

            //spawned from deep links
            mBaseWineId = args.getString(BASE_WINE_ID);
            mVintageId = args.getString(VINTAGE_ID);
        }

        //ensure mBaseWineId is not null, so we can use it to retrieve BaseWine and CaptureNotes
        if (mWineProfile != null) {
            //spawned from Feed Fragment
            mBaseWineId = mWineProfile.getBaseWineId();
        } else if (mBaseWineMinimal != null) {
            //spawned from Search
            mBaseWineId = mBaseWineMinimal.getId();
        } else if (mBaseWine != null) {
            //spawned from WineCaptureSubmitFragment
            mBaseWineId = mBaseWine.getId();
        }
        //if spawned from deeplinks, mBaseWineId wil be already populated

        //ensured that baseWineId is populated, set as our fetchingId
        mFetchingId = mBaseWineId;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // configure ActionBar
        enableBackButton(true);
        getActionBarToolbar()
                .setBackgroundColor(getResources().getColor(R.color.d_off_white_translucent));
        mAlphaSpan = new MutableForegroundColorSpan(0,
                getResources().getColor(R.color.d_big_stone));
        if (mWineProfile != null) {
            mTitle = new SpannableString(
                    mWineProfile.getProducerName() + " " + mWineProfile.getName());
            mTitle.setSpan(mAlphaSpan, 0, mTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            setActionBarSubtitle(mTitle);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wine_profile, container, false);

        final ListView listView = (ListView) view.findViewById(R.id.list_view);

        //prepare header view
        final View header = inflater.inflate(R.layout.wine_profile_header, null, false);
        ButterKnife.inject(this, header);

        updateBannerData();
        updateVarietyRegionRatingView(mBaseWine);

        listView.addHeaderView(header, null, false);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position--; //headerview offsets position of listitems by 1
                launchCaptureDetails(mAdapter.getItem(position));
            }
        });

        // empty state
        mEmptyView.setVisibility(mAdapter.isEmpty() ? View.VISIBLE : View.GONE);

        final HideableActionBarScrollListener hideableActionBarScrollListener
                = new HideableActionBarScrollListener(this);

        // Setup Floating Camera Button
        mCameraButton = (FloatingActionButton) view.findViewById(R.id.camera_button);
        final FloatingActionButton.FabOnScrollListener fabOnScrollListener
                = new FloatingActionButton.FabOnScrollListener() {

            final View wineImageView = header.findViewById(R.id.wine_image);

            boolean isTitleVisible = false;

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                    int totalItemCount) {
                super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

                final float top = -header.getTop();
                float height = header.getHeight();
                // check if header is still visible
                if (top <= height) {
                    wineImageView.setTranslationY(top / 2f);
                }

                // decide if title should be shown / transparent actionbar
                if (listView != null && listView.getChildCount() > 1) {

                    boolean firstItemVisible = listView.getFirstVisiblePosition() > 0;
                    if (isTitleVisible != firstItemVisible) {
                        // title
                        ObjectAnimator titleAnimator = new ObjectAnimator()
                                .ofInt(mAlphaSpan, MutableForegroundColorSpan.ALPHA_PROPERTY,
                                        firstItemVisible ? 0 : 255, firstItemVisible ? 255 : 0);
                        titleAnimator.setDuration(ACTIONBAR_TRANSITION_ANIM_DURATION);
                        titleAnimator.setInterpolator(new DecelerateInterpolator());
                        titleAnimator.setEvaluator(new ArgbEvaluator());
                        titleAnimator
                                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        setActionBarSubtitle(mTitle);
                                    }
                                });
                        titleAnimator.start();
                        // background
                        int solidColor = getResources().getColor(R.color.d_off_white);
                        int translucentColor = getResources()
                                .getColor(R.color.d_off_white_translucent);
                        final ValueAnimator bgAnimator = ValueAnimator.ofObject(
                                new ArgbEvaluator(),
                                firstItemVisible ? translucentColor : solidColor,
                                firstItemVisible ? solidColor : translucentColor);
                        bgAnimator.setDuration(ACTIONBAR_TRANSITION_ANIM_DURATION);
                        bgAnimator.setInterpolator(new DecelerateInterpolator());
                        bgAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                getActionBarToolbar().setBackgroundColor(
                                        (Integer) bgAnimator.getAnimatedValue());
                            }
                        });
                        bgAnimator.start();
                    }
                    isTitleVisible = firstItemVisible;
                }

                hideableActionBarScrollListener
                        .onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        };
        mCameraButton.attachToListView(listView, fabOnScrollListener);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchWineCapture();
            }
        });

        return view;
    }

    /**
     * The WineBannerView can be set with different types of data depending from where this fragment
     * was spawned.
     */
    private void updateBannerData() {
        String wineTitle = null;
        if (mWineProfile != null) {
            //spawned from Feed Fragment
            mBanner.updateData(mWineProfile, mCapturePhotoHash, false);
            wineTitle = mWineProfile.getProducerName() + " " + mWineProfile.getName();
        } else if (mBaseWineMinimal != null) {
            //spawned from Search Wines
            mBanner.updateData(mBaseWineMinimal);
            wineTitle = mBaseWineMinimal.getProducerName() + " " + mBaseWineMinimal.getName();
        } else if (mBaseWine != null) {
            //spawned from WineCaptureSubmit
            //also called after BaseWine is successfully fetched
            mBanner.updateData(mBaseWine);
            wineTitle = mBaseWine.getProducerName() + " " + mBaseWine.getName();
        }

        // update actionbar title
        if (wineTitle != null) {
            mTitle = new SpannableString(wineTitle);
            mTitle.setSpan(mAlphaSpan, 0, mTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            setActionBarSubtitle(mTitle);
        }
    }

    @OnClick(R.id.all_years_textview)
    protected void onAllYearsTextClick() {
        ChooseVintageDialog dialog = ChooseVintageDialog.newInstance(mBaseWineId);
        dialog.setTargetFragment(WineProfileFragment.this,
                CHOOSE_VINTAGE_DIALOG); //callback goes to onActivityResult
        dialog.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onResume() {
        super.onResume();

        //not spawned from WineCaptureSubmit, need to fetch BaseWine
        if (mBaseWine == null) {
            loadLocalBaseWineData(); //load from model to show something first
            mBaseWineController.fetchBaseWine(mBaseWineId);
        }

        if (mAdapter.getItems().isEmpty()) {
            loadLocalData(Type.BASE_WINE, mBaseWineId);
        }

        showOrHideActionBar(false, ACTIONBAR_HIDE_DELAY);
    }

    /**
     * @param wineId baseWineId (for all wines) or wineProfileId (for specific vintage)
     */
    private void loadLocalData(final Type type, final String wineId) {
        mFetching = true;
        new SafeAsyncTask<Listing<CaptureNote>>(this) {
            @Override
            protected Listing<CaptureNote> safeDoInBackground(Void[] params) {
                Log.d(TAG, "loadLocalData:doInBg");
                return CaptureNoteListingModel.getUserCaptures(wineId);
            }

            @Override
            protected void safeOnPostExecute(Listing<CaptureNote> listing) {
                Log.d(TAG, "loadLocalData:returnedFromCache");
                if (listing != null) {
                    Log.d(TAG, "loadLocalData:listingExists size: " + listing.getUpdates().size());
                    mCaptureNoteListing = listing;
                    //items were successfully retrieved from cache, set to view!
                    mAdapter.setItems(listing.getUpdates());
                    mAdapter.notifyDataSetChanged();
                }

                if (mAdapter.getItems().isEmpty()) {
                    //only if there were no cache items do we make the call to fetch entries
                    fetchCaptureNotes(type, wineId, null, false);
                } else {
                    Log.d(TAG, "loadLocalData:success");
                    //simulate a pull to refresh if there are items
                    fetchCaptureNotes(type, wineId, mCaptureNoteListing, true);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case CHOOSE_VINTAGE_DIALOG:
                Object wine = data.getParcelableExtra(ChooseVintageDialog.WINE);
                String mOldFetchingId = mFetchingId;
                //when All Years is selected from dialog
                if (wine instanceof BaseWine) {
                    mType = Type.BASE_WINE;
                    BaseWine baseWine = (BaseWine) wine;
                    updateRatingsView(baseWine);
                    mFetchingId = baseWine.getId();
                }
                //when a vintage year is selected from the dialog
                if (wine instanceof WineProfileSubProfile) {
                    mType = Type.WINE_PROFILE;
                    WineProfileSubProfile wineProfile = (WineProfileSubProfile) wine;
                    updateRatingsView(wineProfile);
                    mFetchingId = wineProfile.getId();
                }

                //only clear list if fetching id has changed
                if (!mOldFetchingId.equals(mFetchingId)) {
                    mAdapter.getItems().clear();

                    //first query cache to see if we have something on hand already
                    loadLocalData(mType, mFetchingId);
                }

        }
    }

    private void loadLocalBaseWineData() {
        //retrieve full base wine information
        mBaseWine = mBaseWineModel.getBaseWine(mBaseWineId);
        if (mBaseWine == null) {
            return;
        }

        //if spawned from a Feed Fragment, mCapturePhotoHash won't be null, we don't want to replace that capture photo with the basewine's photo
        if (mCapturePhotoHash == null) {
            mCapturePhotoHash = mBaseWine.getPhoto();
        }
        updateBannerData();
        updateVarietyRegionRatingView(mBaseWine);
    }

    public void onEventMainThread(UpdatedBaseWineEvent event) {
        if (!mBaseWineId.equals(event.getBaseWineId())) {
            return;
        }

        if (event.isSuccessful()) {
            loadLocalBaseWineData();
        } else {
            showToastError(event.getErrorMessage());
        }
    }

    /**
     * @param idType Whether to load captures notes for a base wine or a wine profile.
     */
    private void fetchCaptureNotes(Type idType, String id, Listing<CaptureNote> listing,
            Boolean isPullToRefresh) {
        mFetching = true;
        if (idType == Type.BASE_WINE) {
            mCaptureController.fetchCaptureNotes(BASE_WINE_NOTES_REQ, id, null, listing, null,
                    isPullToRefresh);
        }
        if (idType == Type.WINE_PROFILE) {
            mCaptureController
                    .fetchCaptureNotes(WINE_PROFILE_NOTES_REQ, null, id, listing, null,
                            isPullToRefresh);
        }
    }

    public void onEventMainThread(UpdatedListingEvent<CaptureNote> event) {
        if (!event.getRequestId().equals(WINE_PROFILE_NOTES_REQ)) {
            if (!event.getRequestId().equals(BASE_WINE_NOTES_REQ)) {
                return;
            }
        }

        mFetching = false;

        if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());
            return;
        }

        if (event.getListing() != null) {
            mCaptureNoteListing = event.getListing();
            mAdapter.setItems(mCaptureNoteListing.getUpdates());
            mAdapter.notifyDataSetChanged();
        }
        //if listing is null, means there are no updates
        //we don't let listing get assigned null

        // empty state
        mEmptyView.setVisibility(mAdapter.isEmpty() ? View.VISIBLE : View.GONE);

    }

    private void markCaptureAsHelpful(CaptureNote captureNote, boolean markHelpful) {
        mCaptureNotesExpectingUpdate.put(captureNote.getId(), captureNote);
        mCaptureNoteExpectedHelpfulStatus.put(captureNote.getId(), markHelpful);

        mCaptureController.markCaptureHelpful(captureNote, markHelpful);
    }

    public void onEventMainThread(MarkedCaptureHelpfulEvent event) {
        String captureId = event.getCaptureId();
        CaptureNote captureNote = mCaptureNotesExpectingUpdate.remove(captureId);
        Boolean expectedHelpful = mCaptureNoteExpectedHelpfulStatus.remove(captureId);
        if (captureNote == null) {
            return; //capturenote didn't exist in the hashmap, means this event wasn't called from this fragment
        }

        //the captureNote object's helpfuling account ids list was modified when the user pressed helpful,
        // if the event errored we need to revert the list back to it's original form
        if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());

            //revert helpfulness, bc event failed
            String userId = UserInfo.getUserId(getActivity());
            if (expectedHelpful) {
                captureNote.unmarkHelpful(userId);
            } else {
                captureNote.markHelpful(userId);
            }
        }
        //will reset following toggle button back to original setting if error
        mAdapter.notifyDataSetChanged();
    }


    private void updateVarietyRegionRatingView(BaseWine baseWine) {
        if (baseWine == null) {
            return;
        }
            //varietal info
        if (baseWine.getVarietalComposition().size() == 0) {
            //beers don't have varietal composition
            mVarietalContainer.setVisibility(View.GONE);
        } else {
            GradientDrawable varietalDrawable = (GradientDrawable) mVarietalImageView.getDrawable();
            String hexColor = baseWine.getVarietalComposition().get(0).getColor();
            varietalDrawable.setColor(Color.parseColor(hexColor));

            //combine varietal names if there's more than one
            ArrayList<String> varietalNames = new ArrayList<String>();
            for (VarietalsHash varietal : baseWine.getVarietalComposition()) {
                varietalNames.add(varietal.getName());
            }
            TextUtils.join(", ", varietalNames);
            String varietalDisplayText = StringUtils.join(varietalNames, ", ");
            mVarietalTextView.setText(varietalDisplayText);
        }

        //regional info
        String regionPath = baseWine.getRegionPathDisplayText(getActivity());
        mRegionPathTextView.setText(regionPath);

        updateRatingsView(baseWine);

        if (!mViewWineTracked) {
            KahunaUtil.trackViewWine(baseWine.getId(), baseWine.getName());
            mViewWineTracked = true;
        }
    }

    private void updateRatingsView(Ratingsable ratingsable) {
        //rating avg
        double allAvg = ratingsable.getRatingsSummary().getAllAvgOfTen();
        double proAvg = ratingsable.getRatingsSummary().getProAvgOfTen();
        DecimalFormat format = new DecimalFormat("0.0");
        if (allAvg == NO_AVG_RATING) { //handling a no rating case, show a dash
            mAllRatingsAverageTextView.setText("-");
            mAllRatingsAverageTextView.setTextColor(getResources().getColor(R.color.d_medium_gray));
        } else {
            String allAvgStr = format.format(allAvg);
            mAllRatingsAverageTextView.setText(makeRatingDisplayText(allAvgStr));
        }
        if (proAvg == NO_AVG_RATING) {
            mProRatingsAverageTextView.setText("-");
            mProRatingsAverageTextView.setTextColor(getResources().getColor(R.color.d_medium_gray));
        } else {
            String proAvgStr = format.format(proAvg);
            mProRatingsAverageTextView.setText(makeRatingDisplayText(proAvgStr));
        }

        //ratings count
        int allCount = ratingsable.getRatingsSummary().getAllCount();
        int proCount = ratingsable.getRatingsSummary().getProCount();
        String allRatingsCount = getResources().getQuantityString(
                R.plurals.wine_profile_ratings_count, allCount, allCount);
        String proRatingsCount = getResources()
                .getQuantityString(R.plurals.wine_profile_pro_ratings_count, proCount, proCount);
        mAllRatingsCountTextView.setText(allRatingsCount);
        mProRatingsCountTextView.setText(proRatingsCount);
    }

    /**
     * Makes the rating display text where the rating is a bit bigger than the 10.
     */
    private CharSequence makeRatingDisplayText(String rating) {
        SpannableString ss = new SpannableString(rating);
        ss.setSpan(new RelativeSizeSpan(1.3f), 0, rating.length(), 0); // set size
        CharSequence displayText = TextUtils.concat(ss, "/10");
        return displayText;
    }


    @Override
    public void toggleHelpful(CaptureNote captureNote, boolean markHelpful) {
        markCaptureAsHelpful(captureNote, markHelpful);
    }

    public void launchCaptureDetails(CaptureNote captureNote) {
        Intent intent = new Intent();
        intent.putExtra(CaptureDetailsActivity.PARAMS_CAPTURE_ID,
                captureNote.getId());
        intent.setClass(getActivity(), CaptureDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void launchUserProfile(String userAccountId) {
        Intent intent = new Intent();
        intent.putExtra(UserProfileActivity.PARAMS_USER_ID, userAccountId);
        intent.setClass(getActivity(), UserProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void shouldLoadNextPage() {
        Log.d(TAG, "shouldLoadNextPage");
        if (mFetching) {
            return;
        }
        Log.d(TAG, "shouldLoadNextPage:notFetching");

        if (mCaptureNoteListing == null) {
            //reached end of list/there are no items, we do nothing.
            //in theory, this should never be null because the getMore check below should stop it from loading
            return;
        }
        Log.d(TAG, "shouldLoadNextPage:captureListingExists");

        if (mCaptureNoteListing.getMore()) {
            mFetching = true;
            //mNoFollowersText.setVisibility(View.GONE);
            fetchCaptureNotes(mType, mFetchingId, mCaptureNoteListing, false);
            Log.d(TAG, "shouldLoadNextPage:moreTrue");
        }
    }

    private static enum Type {
        BASE_WINE, WINE_PROFILE;
    }
}
