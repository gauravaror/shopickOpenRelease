package com.acquire.shopick.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.acquire.shopick.R;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.dao.Brands;
import com.acquire.shopick.event.BrandsLoadedEvent;
import com.acquire.shopick.event.StoredBrandsLoadedEvent;
import com.acquire.shopick.job.GetBrandsJob;
import com.acquire.shopick.ui.widget.ProgressView;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsManager;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Subscribe;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.acquire.shopick.util.LogUtils.LOGD;
import static com.acquire.shopick.util.LogUtils.LOGV;
import static com.acquire.shopick.util.LogUtils.makeLogTag;

public class BrandPickerActivity extends CoreActivity {

    private static final String TAG = makeLogTag(BrandPickerActivity.class) ;
    private static final String SCREEN_LABEL = "BrandPicker";
    @Bind(R.id.list_categories)
    ListView list;
    private BrandPickerAdapter mBrandAdapter;
    @Bind(R.id.filter_category)
    EditText mFilterCategory;
    @Bind(R.id.progress_brand)
    ProgressView progressView;

    private boolean loadedServer = false;

    @Inject
    JobManager jobManager;


    public static String EXTRA_BRAND_ID = "com.acquire.shopick.ui.PostFeedItem.brand_id";
    public static String EXTRA_BRAND_NAME = "com.acquire.shopick.ui.PostFeedItem.brand_name";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.brand_picker);
        super.onCreate(savedInstanceState);
        mBrandAdapter =  new BrandPickerAdapter(getApplicationContext());
        //Set the back button.
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyticsManager.sendEvent(TAG, "Discarded item", "category", 0L);

                supportFinishAfterTransition();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Brands tag = mBrandAdapter.mItems.get(position);
                intent.putExtra(EXTRA_BRAND_ID, tag.getId());
                intent.putExtra(EXTRA_BRAND_NAME, tag.getName());
                setResult(RESULT_OK, intent);
                supportFinishAfterTransition();
            }
        });
        mFilterCategory.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String text = mFilterCategory.getText().toString().toLowerCase(Locale.getDefault());
                mBrandAdapter.filter(text);
                list.invalidate();
                list.setAdapter(mBrandAdapter);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        addBrandJob();
        AnalyticsManager.sendScreenView(SCREEN_LABEL);


    }


    private void addBrandJob() {
        jobManager.addJob(new GetBrandsJob(AccountUtils.getShopickProfileId(getApplicationContext()), -1L, -1L));

    }



    @Subscribe
    public void OnStoredBrandLoadedEvent(StoredBrandsLoadedEvent event) {
        if ( loadedServer != true && event.getBrands() != null && event.getBrands().size() > 0) {
            mBrandAdapter.mItems.clear();
            mBrandAdapter.mItemsCopy.clear();
            mBrandAdapter.mItems.addAll(event.getBrands());
            mBrandAdapter.mItemsCopy.addAll(event.getBrands());
            progressView.stopAnimation();
            list.invalidate();
            list.setAdapter(mBrandAdapter);
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
            progressView.stopAnimation();
            list.invalidate();
            list.setAdapter(mBrandAdapter);
        }
    }
}
