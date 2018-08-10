package com.acquire.shopick.ui;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acquire.shopick.R;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.event.PresentationsLoadedEvent;
import com.acquire.shopick.event.StoredPresentationsLoadedEvent;
import com.acquire.shopick.job.GetTipsJob;
import com.acquire.shopick.ui.widget.ProgressView;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.ImageLoader;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.OverFlipMode;

import static com.acquire.shopick.util.LogUtils.LOGD;
import static com.acquire.shopick.util.LogUtils.LOGE;
import static com.acquire.shopick.util.LogUtils.makeLogTag;

/**
 * Created by gaurav on 9/24/15.
 */
public class PresentationFragment extends Fragment implements
        PresentationFlipAdapter.Callback , FlipView.OnFlipListener, FlipView.OnOverFlipListener {

    private static final String TAG = makeLogTag(PresentationFragment.class);

    private View mRoot = null;
    private PresentationFlipAdapter mPresentationAdapter = null;
    private FlipView mViewPager = null;

    // the cursor whose data we are currently displaying
    private int mPresentationQueryToken;
    private Cursor mCursor;
    private boolean mIsSearchCursor;

    private TextView mEmptyView;
    private View mLoadingView;
    private Bundle mArguments;
    private ProgressView progressView;
    private String filter = "";
    private boolean loadedServer = false;
    private Long category_id =  -1L;
    private String tab_id = "presentation";

    @Inject
    JobManager jobManager;


    // this variable is relevant when we start the sessions loader, and indicates the desired
    // behavior when load finishes: if true, this is a full reload (for example, because filters
    // have been changed); if not, it's just a refresh because data has changed.
    private boolean mSessionDataIsFullReload = false;

    private ImageLoader mImageLoader;
    private int mDefaultSessionColor;
    private GestureDetectorCompat mDetector;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShopickApplication.injectMembers(this);

        //   mDetector = new GestureDetectorCompat(getActivity().getApplicationContext(), new MyGestureListener());
        mPresentationAdapter = new PresentationFlipAdapter(getActivity());
        mPresentationAdapter.setCallback(this);

        if (mPresentationAdapter.mItems.size() == 0) {
               // BusProvider.getInstance().post(new LoadPresentationsEvent(1, filter));
            sendPresentationEvent();
        }

    }

    private void sendPresentationEvent() {
        jobManager.addJob(new GetTipsJob(AccountUtils.getShopickProfileId(getActivity().getApplicationContext()), category_id));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //reloadFromArguments(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.presentation_fragment_pager, container, false);
        mViewPager = (FlipView)mRoot.findViewById(R.id.pager_presentation);
        mViewPager.setEmptyView(mRoot.findViewById(R.id.empty_text));
        mViewPager.setOverFlipMode(OverFlipMode.RUBBER_BAND);
        mViewPager.setAdapter(mPresentationAdapter);
       // mViewPager.peakNext(false);
        progressView = (ProgressView) mRoot.findViewById(R.id.progress_presentation);
        mEmptyView = (TextView) mRoot.findViewById(R.id.empty_text);
        mViewPager.setOnFlipListener(this);
        mViewPager.setOnOverFlipListener(this);
        mLoadingView = mRoot.findViewById(R.id.loading);
        return mRoot;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mViewPager != null )
                mViewPager.peakNext(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
        if (loadedServer == false) {
           // BusProvider.getInstance().post(new LoadPresentationsEvent(1, filter));
            sendPresentationEvent();
        }
       // mViewPager.peakNext(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void OnStoredPresentationsLoaded(StoredPresentationsLoadedEvent event) {
        if (event.getPresentations() != null ) {
            loadedServer = true;
            mPresentationAdapter.mItems.clear();
            mPresentationAdapter.mItems.addAll(event.getPresentations());
            progressView.stopAnimation();
            mViewPager.invalidate();
            mViewPager.setAdapter(mPresentationAdapter);
            mViewPager.peakNext(true);
        } else {
            //     BusProvider.getInstance().post(new LoadFeedEvent(1, filter));
        }
    }


    @Subscribe
    public void OnPresentationsLoaded(PresentationsLoadedEvent event) {
        if (event.getPresentations() != null ) {
            loadedServer = true;
            mPresentationAdapter.mItems.clear();
            mPresentationAdapter.mItems.addAll(event.getPresentations());
            progressView.stopAnimation();
            mViewPager.invalidate();
            mViewPager.setAdapter(mPresentationAdapter);
            mViewPager.peakNext(true);
        } else {
       //     BusProvider.getInstance().post(new LoadFeedEvent(1, filter));
        }
    }



    @Override
    public void onPageRequested(int page) {
        try {
            mViewPager.smoothFlipTo(page);
        } catch (Exception ex) {
            LOGE(TAG, "Seems pgae doesn't exist but we try to set to it. "+ page);
        }
    }

    @Override
    public void onFlippedToPage(FlipView flipView, int i, long l) {
        LOGD(TAG,String.valueOf(i));
    }

    @Override
    public void onOverFlip(FlipView flipView, OverFlipMode overFlipMode, boolean b, float v, float v1) {
        LOGD(TAG,String.valueOf(b));
    }


}
