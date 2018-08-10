package com.acquire.shopick.ui;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.acquire.shopick.Config;
import com.acquire.shopick.R;
import com.acquire.shopick.io.network.ShopickApi;
import com.acquire.shopick.ui.widget.SimpleDividerItemDecoration;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsEvent;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.acquire.shopick.util.SnackbarUtil;
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.acquire.shopick.util.LogUtils.LOGD;
import static com.acquire.shopick.util.LogUtils.makeLogTag;

/**
 * Created by gaurav on 3/9/16.
 */
public class SearchableActivity extends CoreActivity {
    private static final String TAG = makeLogTag(SearchableActivity.class);
    private static final String SCREEN_NAME = "SEARCHABLE_SCREEN_FINAL";
    ShopickApi.SearchService service;

    private SearchRecyclerAdapter mSearchAdaptersRecycler;

    @Bind(R.id.search_shopick)
    public SearchView editText_Search ;

    @Bind(R.id.general_search)
    public RecyclerView mRecyclerView;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.search);
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());

        //Set the back button.
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyticsManager.sendEvent(TAG, "Discarded item", "Searchable", 0L);
                supportFinishAfterTransition();
            }
        });

        if (editText_Search == null) {
            ButterKnife.bind(this);
        }
        editText_Search.setIconified(false);
        mSearchAdaptersRecycler = new SearchRecyclerAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext(), 10, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mSearchAdaptersRecycler);

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ShopickApi.SearchService.class);
        attachSearchToRxJava();
        AnalyticsManager.sendScreenView(SCREEN_NAME);

    }

    private void attachSearchToRxJava() {

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ShopickApi.SearchService.class);


        RxSearchView.queryTextChanges(editText_Search)
                .debounce(150, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .switchMap(s -> service.searchShopick(s.toString(), AccountUtils.getRequestMap(getApplicationContext())))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> {
                    SnackbarUtil.showMessage(editText_Search, "Error while searching!! Do you have internet connection!!");
                })

                .subscribe(s -> {
                    if (!editText_Search.getQuery().toString().isEmpty()) {
                        AnalyticsEvent.searchEventStarted(getApplicationContext(), editText_Search.getQuery().toString());
                    }
                    mSearchAdaptersRecycler.mItems.clear();
                    mSearchAdaptersRecycler.mItems.addAll(s);
                    mSearchAdaptersRecycler.notifyDataSetChanged();
                }, p -> {
                });


    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (editText_Search == null) {
                ButterKnife.bind(this);
            }
            editText_Search.setQuery(query, false);
        }
    }

    private void doMySearch(String query) {
        LOGD(TAG, "Searching for query"+ query);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)findViewById(R.id.search_shopick);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchableActivity.class)));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }
}
