package com.acquire.shopick.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acquire.shopick.R;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.dao.PostCollection;
import com.acquire.shopick.event.LoadPostCollectionEvent;
import com.acquire.shopick.event.LoadTTopBrandUpdatesEvent;
import com.acquire.shopick.event.LoadTopPostCollectionEvent;
import com.acquire.shopick.event.LoadUserPostCollectionEvent;
import com.acquire.shopick.event.PostCollectionLoaded;
import com.acquire.shopick.event.TopPostCollectionLoaded;
import com.acquire.shopick.job.GetBrandsJob;
import com.acquire.shopick.job.GetPostsJob;
import com.acquire.shopick.ui.widget.ProgressView;
import com.acquire.shopick.ui.widget.SimpleDividerItemDecoration;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.acquire.shopick.ui.ExploreFragment.REQUEST_BRAND_PICKER;
import static com.acquire.shopick.util.LogUtils.makeLogTag;
import static com.facebook.GraphRequest.TAG;

/**
 * Created by gaurav on 6/20/16.
 */

public class ShowMetaPostsFragment extends Fragment {

    private ArrayList<PostCollection> postCollections;
    private Bus mBus;

    @Bind(R.id.recycler_meta_post)
    public RecyclerView mMetaPostsRecyclerView;


    @Bind(R.id.progress_meta_post)
    ProgressView progress_view;

    private Long brand_id;

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
        View mRoot = inflater.inflate(R.layout.show_meta_post_fragment, container, false);
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
        sendEvents();
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
        mBus.post(new LoadUserPostCollectionEvent());

    }

    @OnClick(R.id.list_all_brands)
    public void ListAllBrand(View view) {
        startActivityForResult(DispatchIntentUtils.dispatchOpenBrandList(getActivity().getApplicationContext()), REQUEST_BRAND_PICKER);
        AnalyticsManager.sendEvent("META_OFFER_FEED","CLICKED","LIST_ALL_BRANDS");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivity Result " + requestCode + "  " + resultCode + "  ");
        if (requestCode == REQUEST_BRAND_PICKER && resultCode == getActivity().RESULT_OK && data != null ) {
            brand_id = data.getLongExtra(BrandPickerActivity.EXTRA_BRAND_ID,-1);
            if(brand_id != -1) {
                String brand_name = data.getStringExtra(BrandPickerActivity.EXTRA_BRAND_NAME);
                DispatchIntentUtils.startActivityWithCondition(getActivity(), DispatchIntentUtils.openMetaPostFragmentItem(getActivity().getApplicationContext(), brand_name, brand_id));
            }
        }
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
