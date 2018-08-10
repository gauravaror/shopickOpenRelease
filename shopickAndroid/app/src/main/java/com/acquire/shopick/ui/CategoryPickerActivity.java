package com.acquire.shopick.ui;

import android.content.Intent;
import android.database.Cursor;
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
import com.acquire.shopick.dao.Categories;
import com.acquire.shopick.event.CategoriesLoadedEvent;
import com.acquire.shopick.job.GetCategoriesJob;
import com.acquire.shopick.ui.widget.ProgressView;
import com.acquire.shopick.util.AnalyticsManager;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Subscribe;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.acquire.shopick.util.LogUtils.LOGD;
import static com.acquire.shopick.util.LogUtils.makeLogTag;

public class CategoryPickerActivity extends CoreActivity  {

    private static final String TAG = makeLogTag(CategoryPickerActivity.class) ;
    private static final String SCREEN_LABEL = "CategoryPicker";
    private ListView list;
    private Cursor mCursor;
    private CategoryPickerAdapter mCategoryAdapter;
    @Bind(R.id.filter_category)
    EditText mFilterCategory;
    @Bind(R.id.progress_categories)
    ProgressView progressView;

    @Inject
    JobManager jobManager;

    private boolean loadedServer = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.category_picker);
        super.onCreate(savedInstanceState);

        list = (ListView)findViewById(R.id.list_categories);
        mCategoryAdapter =  new CategoryPickerAdapter(getApplicationContext());
        //Set the back button.
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_blue_400_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyticsManager.sendEvent(TAG, "Discarded item", "category", 0L);

                supportFinishAfterTransition();
            }
        });
        ButterKnife.bind(this);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Categories tag = mCategoryAdapter.mItems.get(position);
                intent.putExtra(PostFeedItem.EXTRA_CATEGORY_ID, tag.getId());
                intent.putExtra(PostFeedItem.EXTRA_CATEGORY_NAME, tag.getName());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        mFilterCategory.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String text = mFilterCategory.getText().toString().toLowerCase(Locale.getDefault());
                mCategoryAdapter.filter(text);
                list.invalidate();
                list.setAdapter(mCategoryAdapter);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Set other details.
        toolbar.setTitle("Post to Shopick");
        sendCategoryEvent();
        toolbar.setBackgroundColor(getResources().getColor(R.color.white_pressed));
        toolbar.setTitleTextColor(getResources().getColor(R.color.black_semi_transparent));
        AnalyticsManager.sendScreenView(SCREEN_LABEL);


    }

    private void sendCategoryEvent() {
        jobManager.addJob(new GetCategoriesJob());
    }



    @Subscribe
    public void OnCategoryLoadedEvent(CategoriesLoadedEvent event) {
        if (event.getCategories() != null && event.getCategories().size() > 0) {
            loadedServer = true;
            mCategoryAdapter.mItems.clear();
            mCategoryAdapter.mItemsCopy.clear();
            mCategoryAdapter.mItems.addAll(event.getCategories());
            mCategoryAdapter.mItemsCopy.addAll(event.getCategories());
            progressView.stopAnimation();
            list.invalidate();
            list.setAdapter(mCategoryAdapter);
        }
    }
}
