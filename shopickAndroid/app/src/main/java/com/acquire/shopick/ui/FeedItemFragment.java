package com.acquire.shopick.ui;

import android.support.v4.app.Fragment;

import android.content.Context;

import android.content.Intent;

import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.acquire.shopick.R;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.bus.BusProvider;

import com.acquire.shopick.event.FeedLoadedEvent;
import com.acquire.shopick.event.LoadFeedEvent;
import com.acquire.shopick.event.LocalFeedLoadedEvent;
import com.acquire.shopick.job.GetPostsJob;
import com.acquire.shopick.ui.widget.FloatingActionMenu;
import com.acquire.shopick.ui.widget.ProgressView;
import com.acquire.shopick.ui.widget.SimpleDividerItemDecoration;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsEvent;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.acquire.shopick.util.ImageLoader;
import com.acquire.shopick.util.SnackbarUtil;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;


///
///

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.acquire.shopick.ui.ExploreFragment.REQUEST_BRAND_PICKER;
import static com.acquire.shopick.util.LogUtils.LOGD;
import static com.acquire.shopick.util.LogUtils.makeLogTag;

/**
 * Created by gaurav on 9/24/15.
 */
public class FeedItemFragment extends Fragment   {

    public static final String POST_REFERENCE_FLAG = "com.acquire.shopick.post.feed.intent" ;
    private static String TAG = makeLogTag(MainFeedFragment.class);
    Long brand_id = -1L;
    Long category_id = -1L;
    String title;
    private Context mContext;
    private ImageLoader  mNoPlaceholderImageLoader;
    private FeedRecyclerAdapter mFeedAdapters;
    // the cursor whose data we are currently displaying

    @Bind(R.id.progress_feed)
    public ProgressView progressView;

    @Bind(R.id.recycler_brand_filtered)
    public RecyclerView recyclerView_main;

    private int postType = -1;
    private boolean loadedServer = false;

    @Inject
    JobManager jobManager;


    private Bus mBus;



    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        ShopickApplication.injectMembers(this);
        mFeedAdapters = new FeedRecyclerAdapter(getActivity());
        if (mNoPlaceholderImageLoader == null) {
            mNoPlaceholderImageLoader = new ImageLoader(getActivity().getApplicationContext(),R.drawable.placeholder);
        }
        mBus = BusProvider.getInstance();
    }


    void sendPostEvent() {
        mBus.post(new LoadFeedEvent(AccountUtils.getShopickProfileId(mContext),"",-1, brand_id));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRoot = inflater.inflate(R.layout.brand_filtered_recycler_view_toolbar, container, false);
        ButterKnife.bind(this,mRoot);

        progressView = (ProgressView) mRoot.findViewById(R.id.progress_feed);

        recyclerView_main.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView_main.addItemDecoration(new SimpleDividerItemDecoration(getActivity().getApplicationContext(), 10, false));
        recyclerView_main.setItemAnimator(new DefaultItemAnimator());
        Bundle args = getArguments();
        if (args != null) {
            if (args.getLong(FragmentContainerActivity.SHOPICK_BRAND_ID, -1L) != -1L ) {
                brand_id = args.getLong(FragmentContainerActivity.SHOPICK_BRAND_ID, -1L);
                sendPostEvent();
            } else if (args.getLong(FragmentContainerActivity.SHOPICK_CATEGORY_ID, -1L) != -1L ) {
                category_id = args.getLong(FragmentContainerActivity.SHOPICK_CATEGORY_ID, -1L);
                sendPostEvent();
            }
        } else {
            sendPostEvent();
        }
        return mRoot;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
        if(!loadedServer) {
            sendPostEvent();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }


    @Subscribe
    public void OnFeedLoaded(FeedLoadedEvent event) {
        if (event.getFeed() != null && event.getFeed().size() > 0 && loadedServer == false) {
            loadedServer = true;
            Log.d(TAG, event.getFeed().get(0) != null? "" : event.getFeed().get(0).getId() + " ");
            mFeedAdapters.mItems.clear();
            mFeedAdapters.mItems.addAll(event.getFeed());
            progressView.stopAnimation();
            recyclerView_main.invalidate();
            recyclerView_main.setAdapter(mFeedAdapters);
        }
    }
}
