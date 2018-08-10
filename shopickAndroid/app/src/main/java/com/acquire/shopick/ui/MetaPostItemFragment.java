package com.acquire.shopick.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.acquire.shopick.R;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.dao.PostCollection;
import com.acquire.shopick.event.LoadBrandFilteredPostCollection;
import com.acquire.shopick.event.LoadUserPostCollectionEvent;
import com.acquire.shopick.event.PostCollectionLoaded;
import com.acquire.shopick.ui.widget.ProgressView;
import com.acquire.shopick.ui.widget.SimpleDividerItemDecoration;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.acquire.shopick.ui.ExploreFragment.REQUEST_BRAND_PICKER;
import static com.acquire.shopick.util.LogUtils.makeLogTag;

/**
 * Created by gaurav on 7/9/16.
 */

public class MetaPostItemFragment extends android.support.v4.app.Fragment {

    private ArrayList<PostCollection> postCollections;
    private Bus mBus;

    @Bind(R.id.recycler_brand_filtered)
    public RecyclerView mMetaPostsRecyclerView;


    @Bind(R.id.progress_feed)
    ProgressView progress_view;

    private Long brand_id;
    private Long category_id;

    public static String TAG = makeLogTag(ShowMetaPostsFragment.class);

    private MetaPostsRecyclerAdapter metaPostsRecyclerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShopickApplication.injectMembers(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRoot = inflater.inflate(R.layout.brand_filtered_recycler_view_toolbar, container, false);
        ButterKnife.bind(this,mRoot);
        ShopickApplication.injectMembers(this);
        mBus = BusProvider.getInstance();
        metaPostsRecyclerAdapter = new MetaPostsRecyclerAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        mMetaPostsRecyclerView.setLayoutManager(linearLayoutManager);
        mMetaPostsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext(), 10, false));
        mMetaPostsRecyclerView.setHasFixedSize(true);
        mMetaPostsRecyclerView.setAdapter(metaPostsRecyclerAdapter);
        Bundle args = getArguments();
        if (args != null) {
            if (args.getLong(FragmentContainerActivity.SHOPICK_BRAND_ID, -1L) != -1L ) {
                brand_id = args.getLong(FragmentContainerActivity.SHOPICK_BRAND_ID, -1L);
                sendEvents();
            } else if (args.getLong(FragmentContainerActivity.SHOPICK_CATEGORY_ID, -1L) != -1L ) {
                category_id = args.getLong(FragmentContainerActivity.SHOPICK_CATEGORY_ID, -1L);
                sendEvents();
            }
        } else {
            sendEvents();
        }

        return mRoot;
    }


    @Subscribe
    public void OnPostCollectionLoaded(PostCollectionLoaded event) {
        if ( event.getPostCollections() != null && event.getPostCollections().size() > 0) {
            postCollections = (ArrayList<PostCollection>) event.getPostCollections();
            metaPostsRecyclerAdapter.mItems.clear();
            metaPostsRecyclerAdapter.mItems.addAll(event.getPostCollections());
            mMetaPostsRecyclerView.invalidate();
            mMetaPostsRecyclerView.setAdapter(metaPostsRecyclerAdapter);
            progress_view.stopAnimation();
        }

    }

    void sendEvents() {
        mBus.post(new LoadBrandFilteredPostCollection(brand_id));

    }


    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);

    }
}
