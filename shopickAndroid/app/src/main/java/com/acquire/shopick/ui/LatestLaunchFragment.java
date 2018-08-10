package com.acquire.shopick.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.acquire.shopick.R;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.dao.BrandUpdates;
import com.acquire.shopick.dao.Brands;
import com.acquire.shopick.event.BrandUpdatesLoadedEvent;
import com.acquire.shopick.event.FeaturedBrandUpdatesLoadedEvent;
import com.acquire.shopick.event.LoadFeaturedBrandUpdatesEvent;
import com.acquire.shopick.event.LoadTTopBrandUpdatesEvent;
import com.acquire.shopick.event.StoredBrandUpdatesLoadedEvent;
import com.acquire.shopick.event.TopBrandUpdatesLoadedEvent;
import com.acquire.shopick.job.GetBrandUpdatesJob;
import com.acquire.shopick.ui.widget.ProgressView;
import com.acquire.shopick.ui.widget.SimpleDividerItemDecoration;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.acquire.shopick.util.ImageLoader;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.acquire.shopick.ui.ExploreFragment.REQUEST_BRAND_PICKER;
import static com.acquire.shopick.util.LogUtils.LOGD;
import static com.acquire.shopick.util.LogUtils.LOGV;
import static com.acquire.shopick.util.LogUtils.makeLogTag;

/**
 * Created by gaurav on 9/24/15.
 */
public class LatestLaunchFragment extends Fragment  {

    private static String TAG = makeLogTag(ExploreItemFragment.class);
    public Long brand_id = -1L;
    String title;
    String description;
    String photoUrl;
    String filter;
    private ImageLoader  mNoPlaceholderImageLoader;
    private RecyclerView mRecyclerView;
    private ExploreItemsRecyclerAdapter mExploreAdaptersRecycler;
    private Long category_id;
    private boolean loadedServer = false;

    @Bind(R.id.progress_latest_launch)
    public ProgressView progressView;

    @Bind(R.id.list_all_brands)
    public Button openBrands;

    private Bus mBus;



    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBus = BusProvider.getInstance();
        mExploreAdaptersRecycler = new ExploreItemsRecyclerAdapter(getActivity());
        if (mNoPlaceholderImageLoader == null) {
            mNoPlaceholderImageLoader = new ImageLoader(getActivity().getApplicationContext(),R.drawable.placeholder);
        }
        if (mExploreAdaptersRecycler.mItems.size() == 0) {
            sendLatestLaunchEvent();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRoot = inflater.inflate(R.layout.latest_launch_fragment, container, false);
        ButterKnife.bind(this, mRoot);
        mRecyclerView = (RecyclerView) mRoot.findViewById(R.id.recycler_explore);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity().getApplicationContext(), 10, false));
        mRecyclerView.setHasFixedSize(true);
        if (loadedServer ==  false) {
            sendLatestLaunchEvent();
        }
        return mRoot;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    private void sendLatestLaunchEvent() {
        mBus.post(new LoadFeaturedBrandUpdatesEvent());
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
        if (loadedServer ==  false) {
            // BusProvider.getInstance().post(new LoadBrandUpdatesEvent(brand_id,filter));
            sendLatestLaunchEvent();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }


    @OnClick(R.id.list_all_brands)
    public void ListAllBrand(View view) {
        startActivityForResult(DispatchIntentUtils.dispatchOpenBrandList(getActivity().getApplicationContext()), REQUEST_BRAND_PICKER);
        AnalyticsManager.sendEvent("LATESTLAUNCH","CLICKED","LIST_ALL_BRANDS");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivity Result " + requestCode + "  " + resultCode + "  ");
        if (requestCode == REQUEST_BRAND_PICKER && resultCode == getActivity().RESULT_OK && data != null ) {
                brand_id = data.getLongExtra(BrandPickerActivity.EXTRA_BRAND_ID,-1);
                if(brand_id != -1) {
                    String brand_name = data.getStringExtra(BrandPickerActivity.EXTRA_BRAND_NAME);
                    DispatchIntentUtils.startActivityWithCondition(getActivity(), DispatchIntentUtils.openExploreFragmentItem(getActivity().getApplicationContext(), brand_name, brand_id));
                }
        }
    }



    @Subscribe
    public void OnFeaturedBrandUpdatesLoaded(FeaturedBrandUpdatesLoadedEvent event) {
        if ( event.getUpdates() != null && event.getUpdates().size() > 0) {
            loadedServer = true;
            mExploreAdaptersRecycler.mItems.clear();
            mExploreAdaptersRecycler.mItems.addAll(event.getUpdates());
            mRecyclerView.invalidate();
            mRecyclerView.setAdapter(mExploreAdaptersRecycler);
            progressView.stopAnimation();
        }

    }

}
