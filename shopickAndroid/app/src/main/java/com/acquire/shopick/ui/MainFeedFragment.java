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
import com.acquire.shopick.event.LocalFeedLoadedEvent;
import com.acquire.shopick.job.GetPostsJob;
import com.acquire.shopick.ui.widget.FloatingActionMenu;
import com.acquire.shopick.ui.widget.ProgressView;
import com.acquire.shopick.ui.widget.SimpleDividerItemDecoration;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsEvent;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.acquire.shopick.util.ImageLoader;
import com.acquire.shopick.util.SnackbarUtil;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Subscribe;


///
///

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.acquire.shopick.ui.ExploreFragment.REQUEST_BRAND_PICKER;
import static com.acquire.shopick.util.LogUtils.LOGD;
import static com.acquire.shopick.util.LogUtils.makeLogTag;

/**
 * Created by gaurav on 9/24/15.
 */
public class MainFeedFragment extends Fragment   {

    public static final String POST_REFERENCE_FLAG = "com.acquire.shopick.post.feed.intent" ;
    private static String TAG = makeLogTag(MainFeedFragment.class);
    Long brand_id = -1L;
    Long category_id = -1L;
    String title;
    private Context mContext;
    private ImageLoader  mNoPlaceholderImageLoader;
    private FeedRecyclerAdapter mFeedAdapters;
    // the cursor whose data we are currently displaying
    private FloatingActionMenu mFloatingMenu;
    private ProgressView progressView;

    private RecyclerView recyclerView_feed;
    private int postType = -1;
    private boolean loadedServer = false;

    @Inject
    JobManager jobManager;





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
    }

    void sendPostEvent() {
        jobManager.addJob(new GetPostsJob(AccountUtils.getShopickProfileId(getActivity().getApplicationContext()), category_id, postType, brand_id));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRoot = inflater.inflate(R.layout.feed_fragment, container, false);
        ButterKnife.bind(this,mRoot);

        recyclerView_feed = (RecyclerView) mRoot.findViewById(R.id.recycler_feed);
       // tabLayout = (TabLayout) mRoot.findViewById(R.id.feed_tab_layout);
        mFloatingMenu = (FloatingActionMenu)mRoot.findViewById(R.id.fab_menu_feed);
        progressView = (ProgressView) mRoot.findViewById(R.id.progress_feed);

        recyclerView_feed.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView_feed.addItemDecoration(new SimpleDividerItemDecoration(getActivity().getApplicationContext(), 10, false));
        recyclerView_feed.setItemAnimator(new DefaultItemAnimator());
        mFloatingMenu.setOnMenuItemClickListener(new FloatingActionMenu.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(FloatingActionMenu fam, int index, FloatingActionButton item) {

                if (!AccountUtils.getLoginDone(getContext())) {
                    SnackbarUtil.createStartingLogin(recyclerView_feed);
                    startActivity(DispatchIntentUtils.dispatchLoginIntent(mContext, true));
                    return;
                }
                Intent intent = new Intent(getActivity(), PostFeedItem.class);
                intent.putExtra(POST_REFERENCE_FLAG, index);
                AnalyticsEvent.postEventStarted(mContext, AccountUtils.getShopickTempProfileId(getActivity().getApplicationContext()), index);
                startActivity(intent);
            }
        });


        Bundle args = getArguments();
        if (args != null) {
            if (args.getLong(FragmentContainerActivity.SHOPICK_BRAND_ID, -1L) != -1L ) {
                brand_id = args.getLong(FragmentContainerActivity.SHOPICK_BRAND_ID, -1L);
                if (args.getBoolean(FragmentContainerActivity.SHOPICK_BRAND_OFFER, false)) {
                    postType = 2;
                } else {
                    postType = 1;
                }
                sendPostEvent();
            } else if (args.getLong(FragmentContainerActivity.SHOPICK_CATEGORY_ID, -1L) != -1L ) {
                category_id = args.getLong(FragmentContainerActivity.SHOPICK_CATEGORY_ID, -1L);
                if (args.getBoolean(FragmentContainerActivity.SHOPICK_CATEGORY_OFFER, false)) {
                    postType = 2;
                } else {
                    postType = 1;
                }
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

    @OnClick(R.id.list_all_brands)
    public void ListAllBrand(View view) {
        startActivityForResult(DispatchIntentUtils.dispatchOpenBrandList(getActivity().getApplicationContext()), REQUEST_BRAND_PICKER);
        AnalyticsManager.sendEvent("MAIN_FEED","CLICKED","LIST_ALL_BRANDS");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivity Result " + requestCode + "  " + resultCode + "  ");
        if (requestCode == REQUEST_BRAND_PICKER && resultCode == getActivity().RESULT_OK && data != null ) {
            brand_id = data.getLongExtra(BrandPickerActivity.EXTRA_BRAND_ID,-1);
            if(brand_id != -1) {
                String brand_name = data.getStringExtra(BrandPickerActivity.EXTRA_BRAND_NAME);
                DispatchIntentUtils.startActivityWithCondition(getActivity(), DispatchIntentUtils.openFeedFragmentItem(getActivity().getApplicationContext(), brand_name, brand_id));
            }
        }
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
          //  mFeedAdapters.mItems.add(new Post());
            mFeedAdapters.mItems.addAll(event.getFeed());
            progressView.stopAnimation();
            recyclerView_feed.invalidate();
            recyclerView_feed.setAdapter(mFeedAdapters);
        }
    }
}
