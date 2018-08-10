package com.acquire.shopick.ui;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acquire.shopick.R;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.event.BrandsLoadedEvent;
import com.acquire.shopick.event.StoredBrandsLoadedEvent;
import com.acquire.shopick.job.GetBrandsJob;
import com.acquire.shopick.ui.widget.FloatingActionMenu;
import com.acquire.shopick.ui.widget.ProgressView;
import com.acquire.shopick.util.AccountUtils;
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
public class ExploreFragment extends Fragment {

    private static final String TAG = makeLogTag(ExploreFragment.class);

    private View mRoot = null;
    private ExploreAdapter mExploreAdapter = null;
    private ViewPager mViewPager = null;
    private int itemCount;
    // the cursor whose data we are currently displaying
    private String filter = "";
    private TextView mEmptyView;
    private View mLoadingView;
    private Bundle mArguments;
    private TabLayout tab;
    private CoordinatorLayout ll_explore;
    private ProgressView progressView;
    private ImageLoader mImageLoader;
    private GestureDetectorCompat mDetector;
    private boolean loadedServer = false;
    private FloatingActionMenu mFloatingMenu;
    private String tab_id = "explore";

    private Long category_id = -1L;
    private Long brand_id = -1L;
    private String category_name = "";
    private String brand_name = "";
    private boolean busRegistered = false;


    @Inject
    JobManager jobManager;


    static final int REQUEST_CATEGORY_PICKER = 3008;
    static final int REQUEST_BRAND_PICKER = 5008;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShopickApplication.injectMembers(this);
        mExploreAdapter = new ExploreAdapter(getActivity().getApplicationContext(),getChildFragmentManager());
        if (mExploreAdapter.mItems.size() == 0) {
            sendBrandEvent();
        }


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.explore_fragment_pager, container, false);
        mViewPager = (ViewPager)mRoot.findViewById(R.id.pager_explore);
        mViewPager.setAdapter(mExploreAdapter);
        ll_explore = (CoordinatorLayout) mRoot.findViewById(R.id.ll_explore_frag);
      //  progressView = (ProgressView) mRoot.findViewById(R.id.progress_updates);

        tab  = (TabLayout)mRoot.findViewById(R.id.pager_tab_layout);
        mEmptyView = (TextView) mRoot.findViewById(R.id.empty_text);
        mLoadingView = mRoot.findViewById(R.id.loading);
        if (loadedServer == false ) {
           sendBrandEvent();
        }
        mFloatingMenu = (FloatingActionMenu)mRoot.findViewById(R.id.fab_menu_explore);

        mFloatingMenu.setOnMenuItemClickListener(new FloatingActionMenu.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(FloatingActionMenu fam, int index, FloatingActionButton item) {
                dispatchFilter(index);
            }
        });
        return mRoot;
    }

    void dispatchFilter(int index) {
        if (index == 0) {
            Intent intent = new Intent(getActivity(), CategoryPickerActivity.class);
            startActivityForResult(intent, REQUEST_CATEGORY_PICKER);
        } else if (index == 1) {
            Intent intent = new Intent(getActivity(), BrandPickerActivity.class);
            startActivityForResult(intent, REQUEST_BRAND_PICKER);

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivity Result " + requestCode + "  " + resultCode + "  ");
        if (requestCode == REQUEST_CATEGORY_PICKER && resultCode == getActivity().RESULT_OK && data != null ) {
            category_id = data.getLongExtra(PostFeedItem.EXTRA_CATEGORY_ID, -1);
            mExploreAdapter.category_id = category_id;
            if(category_id != -1) {
                category_name = data.getStringExtra(PostFeedItem.EXTRA_CATEGORY_NAME);
                filter = String.valueOf(category_id);
                brand_id = -1L;
                brand_name = "";
                sendBrandEvent();

            }
        }  else if (requestCode == REQUEST_BRAND_PICKER && resultCode == getActivity().RESULT_OK && data != null ) {
            brand_id = data.getLongExtra(BrandPickerActivity.EXTRA_BRAND_ID,-1);
            if(brand_id != -1) {
                brand_name = data.getStringExtra(BrandPickerActivity.EXTRA_BRAND_NAME);
                category_id = -1L;
                mExploreAdapter.category_id = category_id;
                category_name = "";
                sendBrandEvent();

            }
        }

    }

    void sendBrandEvent() {
       // BusProvider.getInstance().post(new  LoadBrandEvent(AccountUtils.getShopickProfileId(getActivity().getApplicationContext()), filter, brand_id));
        jobManager.addJob(new GetBrandsJob(AccountUtils.getShopickProfileId(getActivity().getApplicationContext()), category_id, brand_id));
    }



    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
        if (loadedServer == false ) {
             sendBrandEvent();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }


    @Subscribe
    public void OnStoredBrandsLoaded(StoredBrandsLoadedEvent event) {
        if (event.getBrands() != null ) {
            mExploreAdapter.mItems.clear();
            mExploreAdapter.mItems.addAll(event.getBrands());
            mViewPager.invalidate();
            mViewPager.setAdapter(mExploreAdapter);
            itemCount = event.getBrands().size();
            if ( itemCount > 0 ) {
                tab.setupWithViewPager(mViewPager);
                tab.setTabTextColors(R.color.theme_accent_1_light, R.color.theme_accent_1);
                //setupTabs();
            } else {
                tab.removeAllTabs();
            }
        }
    }

    @Subscribe
    public void OnBrandsLoaded(BrandsLoadedEvent event) {
        if (event.getBrands() != null ) {
            loadedServer = true;
            mExploreAdapter.mItems.clear();
            mExploreAdapter.mItems.addAll(event.getBrands());
      //      progressView.stopAnimation();
            mViewPager.invalidate();
            mViewPager.setAdapter(mExploreAdapter);
            itemCount = event.getBrands().size();
            if ( itemCount > 0 ) {
                tab.setupWithViewPager(mViewPager);
                tab.setTabTextColors(R.color.theme_accent_1_light, R.color.theme_accent_1);
                //setupTabs();
            } else {
                tab.removeAllTabs();
            }
        } else {
           sendBrandEvent();
        }
    }

}
