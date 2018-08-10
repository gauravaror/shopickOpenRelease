package com.acquire.shopick.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.acquire.shopick.R;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.event.BrandsLoadedEvent;
import com.acquire.shopick.event.ReloadFeedEvent;
import com.acquire.shopick.event.StoredBrandsLoadedEvent;
import com.acquire.shopick.job.GetBrandsJob;
import com.acquire.shopick.job.UpdatePhoneNumberJob;
import com.acquire.shopick.ui.widget.ProgressView;
import com.acquire.shopick.ui.widget.SimpleDividerItemDecoration;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.SnackbarUtil;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.acquire.shopick.util.LogUtils.makeLogTag;

/**
 * Created by gaurav on 3/21/16.
 */
public class LikedBrandActivity extends CoreActivity {

    private static final String TAG = makeLogTag(LikedBrandActivity.class);
    private static final String SCREEN_LABEL = "LikedBrandActivity";
    @Inject
    JobManager jobManager;
    @Bind(R.id.list_categories)
    RecyclerView recyclerView;
    private LikedBrandAdapter mBrandAdapter;
    @Bind(R.id.filter_category)
    EditText mFilterCategory;
    private boolean loadedServer = false;

    @Bind(R.id.button_submit)
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_liked_brand);
        super.onCreate(savedInstanceState);
        Toolbar toolbar = getActionBarToolbar();
        //Set the back button.
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyticsManager.sendEvent(TAG, "Discarded item", "LikedBrandActivity", 0L);

                supportFinishAfterTransition();
            }
        });

        mBrandAdapter =  new LikedBrandAdapter(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext(), 10, true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        mFilterCategory.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String text = mFilterCategory.getText().toString().toLowerCase(Locale.getDefault());
                mBrandAdapter.filter(text);
                recyclerView.invalidate();
                recyclerView.setAdapter(mBrandAdapter);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //Set other details.
        toolbar.setTitle("Select your favourite Brands, We will notify of good deals posted by our community:");
        SnackbarUtil.showMessageLong(recyclerView, "Select your favourite Brands, We will notify you about awesome deals posted by our community!");
        //toolbar.setBackgroundColor(getResources().getColor(R.color.white_pressed));
        addBrandJob();
        AnalyticsManager.sendScreenView(SCREEN_LABEL);
    }


    private void addBrandJob() {
        jobManager.addJob(new GetBrandsJob(-10, -1L, -1L));

    }



    @Subscribe
    public void OnStoredBrandLoadedEvent(StoredBrandsLoadedEvent event) {
        if ( loadedServer != true && event.getBrands() != null && event.getBrands().size() > 0) {
            mBrandAdapter.mItems.clear();
            mBrandAdapter.mItemsCopy.clear();
            mBrandAdapter.mItems.addAll(event.getBrands());
            mBrandAdapter.mItemsCopy.addAll(event.getBrands());
            recyclerView.invalidate();
            recyclerView.setAdapter(mBrandAdapter);
        }
    }

    @Subscribe
    public void OnBrandLoadedEvent(BrandsLoadedEvent event) {
        if (event.getBrands() != null && event.getBrands().size() > 0) {
            loadedServer = true;
            mBrandAdapter.mItems.clear();
            mBrandAdapter.mItemsCopy.clear();
            mBrandAdapter.mItems.addAll(event.getBrands());
            mBrandAdapter.mItemsCopy.addAll(event.getBrands());
            recyclerView.invalidate();
            recyclerView.setAdapter(mBrandAdapter);
        }
    }

    @OnClick(R.id.button_submit)
    void onClick(View view) {
        BusProvider.getInstance().post(new ReloadFeedEvent());
        finish();
    }
}
