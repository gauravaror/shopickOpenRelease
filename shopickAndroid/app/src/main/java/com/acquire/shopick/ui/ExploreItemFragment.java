package com.acquire.shopick.ui;

import android.support.v4.app.LoaderManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.acquire.shopick.R;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.dao.Brands;
import com.acquire.shopick.event.BrandUpdatesLoadedEvent;
import com.acquire.shopick.event.StoredBrandUpdatesLoadedEvent;
import com.acquire.shopick.job.GetBrandUpdatesJob;
import com.acquire.shopick.ui.widget.SimpleDividerItemDecoration;
import com.acquire.shopick.util.ImageLoader;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import static com.acquire.shopick.util.LogUtils.LOGD;
import static com.acquire.shopick.util.LogUtils.LOGV;
import static com.acquire.shopick.util.LogUtils.makeLogTag;

/**
 * Created by gaurav on 9/24/15.
 */
public class ExploreItemFragment extends Fragment  {

    private static String TAG = makeLogTag(ExploreItemFragment.class);
    public Long brand_id = -1L;
    private ImageLoader  mNoPlaceholderImageLoader;
    private RecyclerView mRecyclerView;
    private ExploreItemsRecyclerAdapter mExploreAdaptersRecycler;
    private Long category_id;
    private boolean loadedServer = false;


    @Inject
    JobManager jobManager;



    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    static ExploreItemFragment newInstance(int num, Brands item,String filter, Long category_id_) {
        ExploreItemFragment f = new ExploreItemFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putLong("id", item.getId());
        args.putString("title", item.getName());
        args.putString("description", item.getTagline());
        args.putString("photoUrl",item.getLogo_url());
        args.putString("filter",filter);
        args.putLong("category_id", category_id_);
        f.setArguments(args);

        return f;
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShopickApplication.injectMembers(this);
        Bundle args = getArguments();
        brand_id = getArguments() != null ? getArguments().getLong("id") : -1;
        category_id = getArguments() != null ? getArguments().getLong("category_id") : -1;
        mExploreAdaptersRecycler = new ExploreItemsRecyclerAdapter(getActivity());
        mExploreAdaptersRecycler.showBrandLabel = false;
        if (mNoPlaceholderImageLoader == null) {
            mNoPlaceholderImageLoader = new ImageLoader(getActivity().getApplicationContext(),R.drawable.placeholder);
        }
        if (mExploreAdaptersRecycler.mItems.size() == 0) {
                sendBrandUpdatesEvent();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRoot = inflater.inflate(R.layout.explore_fragment_recycler_item, container, false);
        mRecyclerView = (RecyclerView) mRoot.findViewById(R.id.recycler_explore);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity().getApplicationContext(), 10, false));
        mRecyclerView.setHasFixedSize(true);
        if (loadedServer ==  false) {
            sendBrandUpdatesEvent();
        }
        return mRoot;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    private void sendBrandUpdatesEvent() {
        jobManager.addJob(new GetBrandUpdatesJob(category_id, brand_id));
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
        if (loadedServer ==  false) {
            sendBrandUpdatesEvent();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onStoredBrandUpdatesLoaded(StoredBrandUpdatesLoadedEvent event) {
        if (loadedServer != true && event.getUpdates() != null  && event.getBrand_id() == brand_id ) {
            mExploreAdaptersRecycler.mItems.clear();
            mExploreAdaptersRecycler.mItems.addAll(event.getUpdates());
            mRecyclerView.invalidate();
            mRecyclerView.setAdapter(mExploreAdaptersRecycler);
        }
    }

    @Subscribe
    public void onBrandUpdatesLoaded(BrandUpdatesLoadedEvent event) {
        if (event.getUpdates() != null  && event.getBrand_id() == brand_id && loadedServer == false) {
            loadedServer = true;
            mExploreAdaptersRecycler.mItems.clear();
            mExploreAdaptersRecycler.mItems.addAll(event.getUpdates());
            mRecyclerView.invalidate();
            mRecyclerView.setAdapter(mExploreAdaptersRecycler);
        }
    }

}
