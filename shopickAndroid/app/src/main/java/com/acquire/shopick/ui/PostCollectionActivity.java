package com.acquire.shopick.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.acquire.shopick.R;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.event.LoadPostCollectionEvent;
import com.acquire.shopick.event.PostCollectionLoadedEvent;
import com.acquire.shopick.ui.widget.ProgressView;
import com.acquire.shopick.ui.widget.SimpleDividerItemDecoration;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.SnackbarUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.acquire.shopick.util.LogUtils.LOGD;
import static com.acquire.shopick.util.LogUtils.makeLogTag;


public class PostCollectionActivity extends CoreActivity   {

    private static final String SCREEN_NAME = "OPENED_POST_COLLECTION_SCREEN";
    private static String TAG = makeLogTag(PostCollectionActivity.class);

    private FeedRecyclerAdapter mFeedAdapters;

    @Bind(R.id.recycler_feed)
    public RecyclerView recyclerView_feed;

    private Context mContext;


    @Bind(R.id.progress_bar)
    public ProgressView progressView;

    private Bus mBus;

    String collection_id;

    String collection_title;

    public static String SHOPICK_POST_COLLECTION_ID = "com.acquire.shopick.android.post_collection_id_intent";
    public static String SHOPICK_POST_COLLECTION_TITLE = "com.acquire.shopick.android.post_collection_title_intent";


    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_post_collection_banner);
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        Uri data = getIntent().getData();
        if (b == null || ( b != null && b.getString(SHOPICK_POST_COLLECTION_ID, null) == null ))  {
            collection_id  = data.getLastPathSegment();
            collection_title = getIntent().getStringExtra("collection_title");

        } else {
            collection_id = b.getString(SHOPICK_POST_COLLECTION_ID, "");
            collection_title = b.getString(SHOPICK_POST_COLLECTION_TITLE, "Offers");
        }

        mContext = getApplicationContext();
        mFeedAdapters = new FeedRecyclerAdapter(this);

        //Set Navigational Items
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_grey_300_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyticsManager.sendEvent(TAG, "Discarded item", "category", 0L);
                supportFinishAfterTransition();
            }
        });

        if (!TextUtils.isEmpty(collection_title)) {
            setTitle(collection_title);
        }


        recyclerView_feed.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView_feed.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext(), 10, false));
        recyclerView_feed.setItemAnimator(new DefaultItemAnimator());

        sendPostCollectionEvent();

        AnalyticsManager.sendScreenView(SCREEN_NAME);

    }

    private void sendPostCollectionEvent() {
        mBus = BusProvider.getInstance();
        mBus.post(new LoadPostCollectionEvent(collection_id));
    }


    @Override
    public   void onResume() {
        super.onResume();
        mBus = BusProvider.getInstance();
    }

    @Subscribe
    public void OnPostCollectionLoaded(PostCollectionLoadedEvent event) {
        if (event.getPostArrayList() != null && event.getPostArrayList().size() > 0) {
            Log.d(TAG, event.getPostArrayList().get(0) != null ? "" : event.getPostArrayList().get(0).getId() + " ");
            mFeedAdapters.mItems.clear();
            //  mFeedAdapters.mItems.add(new Post());
            mFeedAdapters.mItems.addAll(event.getPostArrayList());
            recyclerView_feed.invalidate();
            recyclerView_feed.setAdapter(mFeedAdapters);
            progressView.stopAnimation();
        } else {
            mFeedAdapters.mItems.clear();
            //mFeedAdapters.mItems.add(new Post());
            recyclerView_feed.invalidate();
            recyclerView_feed.setAdapter(mFeedAdapters);
            SnackbarUtil.showMessage(recyclerView_feed, "Snap!! Some Error, Please try again later");
        }
    }

}
