package com.acquire.shopick.ui;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.content.Intent;
import android.os.Bundle;


import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.acquire.shopick.Config;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.event.BannersLoadedEvent;
import com.acquire.shopick.event.FeedLoadedEvent;
import com.acquire.shopick.event.LoadBannerEvent;
import com.acquire.shopick.event.LoadPicks;
import com.acquire.shopick.event.LocalFeedLoadedEvent;
import com.acquire.shopick.event.PicksLoadedEvent;
import com.acquire.shopick.event.ReloadFeedEvent;
import com.acquire.shopick.io.model.SearchResult;
import com.acquire.shopick.io.network.ShopickApi;
import com.acquire.shopick.job.GetPostsJob;
import com.acquire.shopick.job.ReadPostJob;
import com.acquire.shopick.ui.widget.FloatingActionMenu;
import com.acquire.shopick.ui.widget.ProgressView;
import com.acquire.shopick.ui.widget.SimpleDividerItemDecoration;
import com.acquire.shopick.util.AnalyticsEvent;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.acquire.shopick.R;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.PrefUtils;
import com.acquire.shopick.util.SnackbarUtil;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.acquire.shopick.util.LogUtils.LOGD;
import static com.acquire.shopick.util.LogUtils.LOGW;
import static com.acquire.shopick.util.LogUtils.makeLogTag;



public class Shopick extends CoreActivity  {
    private static String TAG = makeLogTag(Shopick.class);
    private final static String SCREEN_LABEL = "ShopickMain";

    static com.hookedonplay.decoviewlib.DecoView arcView;

    private static RelativeLayout arcLayout;

    private static Button arcButton;

    static Long mNotifCount = 10L;

    private  int backindex;
    private int seriesItem1;


    public static final String POST_REFERENCE_FLAG = "com.acquire.shopick.post.feed.intent" ;

    Long brand_id = -1L;
    Long category_id = -1L;
    String title;
    private Context mContext;
    private FeedRecyclerAdapter mFeedAdapters;
    // the cursor whose data we are currently displaying


    private int postType = -1;
    private boolean loadedServer = false;
    private boolean reloadedOnResume = false;

    @Bind(R.id.offer_button_feed)
    public Button offer_button;
    @Bind(R.id.collection_button_feed)
    public Button collection_button;
    @Bind(R.id.recycler_feed)
    public RecyclerView recyclerView_feed;
    @Bind(R.id.fab_main_shopick)
    public FloatingActionButton mPostImageButton;
    @Bind(R.id.progress_feed)
    public ProgressView progressView;
    @Bind(R.id.general_search)
    public RecyclerView mRecyclerView;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.intro_screens)
    CirclePageIndicator mPageIndicator;
    @Bind(R.id.search_shopick)
    public SearchView editText_Search;
    @Bind(R.id.banner_layout)
    public LinearLayout bannerLayout;

    @Inject
    JobManager jobManager;

    ShopickApi.SearchService service;

    private SearchRecyclerAdapter mSearchAdaptersRecycler;

    private LinearLayoutManager manager;
    private BannerScreenAdapter bannerScreenAdapter;



    public static int LOGIN_INTENT_RESULT =  1023;

    Timer timer;
    int banners = 0;
    int num_banners;
    private boolean sent = false;

    public void pageSwitcher(int seconds) {
        if(timer != null) {
            timer.cancel();
        }
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay
    }

    // this is an inner class...
    class RemindTask extends TimerTask {

        @Override
        public void run() {

            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            runOnUiThread(new Runnable() {
                public void run() {
                        int current = mViewPager.getCurrentItem();
                        if (current < num_banners -1) {
                            current++;
                        } else {
                            current = 0;
                        }
                        mViewPager.setCurrentItem(current);
                    }
            });

        }
    }

    private Bus mBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Shopick_Sessions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopick);

        //Dependency Injections
        ButterKnife.bind(this);
        ShopickApplication.injectMembers(this);

        //Saving Context
        mContext = getApplicationContext();

        //Getting Bus
        mBus = BusProvider.getInstance();


        //Adapters
        mFeedAdapters = new FeedRecyclerAdapter(this);

        //Setting Banner Adapters and getting Banners
        bannerScreenAdapter =  new BannerScreenAdapter(getApplicationContext(), getFragmentManager());
        mViewPager.setAdapter(bannerScreenAdapter);
        mPageIndicator.setViewPager(mViewPager);
        sendBannerEvent();

        mSearchAdaptersRecycler = new SearchRecyclerAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext(), 10, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mSearchAdaptersRecycler);

        List<SearchResult> list = new ArrayList<SearchResult>();
        //adapter = new SearchResultAdapter(getApplicationContext(), R.layout.activity_shopick, R.id.type);
        attachSearchToRxJava();


        manager = new LinearLayoutManager(getApplicationContext());
        recyclerView_feed.setLayoutManager(manager);
        recyclerView_feed.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext(), 10, false));
        recyclerView_feed.setItemAnimator(new DefaultItemAnimator());
        Bundle args = getIntent().getExtras();
        processArguments(args);

        AnalyticsManager.sendScreenView(SCREEN_LABEL);
    }

    private boolean feedCompletedEventSent = false;
    private boolean feedStartedEventSent = false;
    private boolean feedMidwayEventSent = false;

    RecyclerView.OnScrollListener listenerScroll = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0 && mPostImageButton.isShown())
                mPostImageButton.hide();

            int lastVisiblePosition = manager.findLastCompletelyVisibleItemPosition();
            Post currPost;
            if (lastVisiblePosition >= 0) {
                 currPost = mFeedAdapters.mItems.get(lastVisiblePosition);
                if (currPost.getRead() != null && currPost.getRead() == true) {
                    LOGD(TAG, "Already Seen Post curr earlier :" + currPost.getGlobalID());
                } else {
                    LOGD(TAG, "Last Seen Post curr :" + currPost.getGlobalID());
                    jobManager.addJob(new ReadPostJob(currPost.getGlobalID()));
                    currPost.setRead(true);
                }
            }
            if (!AccountUtils.hasAllOfferShown(getApplicationContext())) {
                LOGD(TAG, "distance in dx " + dy);
                if (dy > 50) {
                    dialogBox();
                    AccountUtils.setAllOfferShownOnce(getApplicationContext());
                }
            }

            if (lastVisiblePosition + 3 > mFeedAdapters.mItems.size() && !feedCompletedEventSent) {
                feedCompletedEventSent = true;
                AnalyticsManager.sendEvent("Feed","FeedScrollCompleted",AccountUtils.getShopickProfileId(getApplicationContext())+"");
                LOGD(TAG, "Feed Completed : " + lastVisiblePosition);

            } else  if (lastVisiblePosition  > mFeedAdapters.mItems.size()/2 && !feedMidwayEventSent)  {
                feedMidwayEventSent = true;
                AnalyticsManager.sendEvent("Feed","FeedScrollMidway",AccountUtils.getShopickProfileId(getApplicationContext())+"");
                LOGD(TAG, "Feed Completed : " + lastVisiblePosition);

            } else if (lastVisiblePosition  > 0 && !feedStartedEventSent) {
                feedStartedEventSent = true;
                AnalyticsManager.sendEvent("Feed","FeedScrollStarted",AccountUtils.getShopickProfileId(getApplicationContext())+"");
                LOGD(TAG, "Feed Completed : " + lastVisiblePosition);

            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            if (newState ==  RecyclerView.SCROLL_STATE_IDLE) {
                mPostImageButton.show();
            }
            super.onScrollStateChanged(recyclerView, newState);
        }

    };

    @OnClick(R.id.fab_main_shopick)
    public void onPostShopick() {
        if (!AccountUtils.getLoginDone(getApplicationContext())) {
            SnackbarUtil.createStartingLogin(recyclerView_feed);
            startActivity(DispatchIntentUtils.dispatchLoginIntent(mContext, true));
            AnalyticsManager.sendEvent("Post", "ButtonClickedMainScreenWithoutLogin", "" + AccountUtils.getShopickTempProfileId(getApplicationContext()));
            return;
        }
        Intent intent = new Intent(getApplicationContext(), PostFeedItem.class);
        AnalyticsManager.sendEvent("Post", "ButtonClickedMainScreen",""+AccountUtils.getShopickTempProfileId(getApplicationContext()));
        startActivity(intent);
    }

    void sendPostEvent() {
        jobManager.addJob(new GetPostsJob(AccountUtils.getShopickProfileId(getApplicationContext()), category_id, postType, brand_id));
    }

    private void processArguments(Bundle args) {
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
                    if (s.size() > 0) {
                        recyclerView_feed.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        Toolbar mActionBarToolbar = getActionBarToolbar();
                        if (mActionBarToolbar != null) {
                            mActionBarToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_18dp);
                            if (arcView != null) {
                                arcView.setVisibility(View.GONE);
                                arcLayout.setVisibility(View.GONE);
                                arcButton.setVisibility(View.GONE);
                            }
                            mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    recyclerView_feed.setVisibility(View.VISIBLE);
                                    mRecyclerView.setVisibility(View.GONE);
                                    if (arcView != null) {
                                        arcView.setVisibility(View.VISIBLE);
                                        arcLayout.setVisibility(View.VISIBLE);
                                        arcButton.setVisibility(View.VISIBLE);

                                    }
                                    editText_Search.setQuery("", false);
                                    mActionBarToolbar.setNavigationIcon(R.drawable.ic_drawer);
                                    mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            reloadedOnResume = true;
                                            openNavDrawer();
                                        }
                                    });
                                }
                            });
                        }

                    }
                    if (!editText_Search.getQuery().toString().isEmpty()) {
                        AnalyticsEvent.searchEventStarted(getApplicationContext(), editText_Search.getQuery().toString());
                    }
                    mSearchAdaptersRecycler.mItems.clear();
                    mSearchAdaptersRecycler.mItems.addAll(s);
                    mSearchAdaptersRecycler.notifyDataSetChanged();
                }, p -> {
                });
    }

    @OnClick(R.id.offer_button_feed)
    public void offerButtonClick(View click) {
        postType = 2;
        loadedServer = false;
        sendPostEvent();
        AnalyticsManager.sendEvent("OfferButton", "ButtonClicked", AccountUtils.getShopickTempProfileId(getApplicationContext()) + "");
        offer_button.setTextColor(getResources().getColor(R.color.material_white));
        collection_button.setTextColor(getResources().getColor(R.color.theme_primary_dark));
        offer_button.setBackgroundResource(R.drawable.buttonshape_selected);
        collection_button.setBackgroundResource(R.drawable.buttonshape);

    }

    @OnClick(R.id.collection_button_feed)
    public void collectionButtonClick(View click) {
        postType = 1;
        loadedServer = false;
        sendPostEvent();
        AnalyticsManager.sendEvent("CollectionButton", "ButtonClicked",  AccountUtils.getShopickTempProfileId(getApplicationContext())+ "");
        offer_button.setTextColor(getResources().getColor(R.color.theme_primary_dark));
        collection_button.setTextColor(getResources().getColor(R.color.material_white));
        offer_button.setBackgroundResource(R.drawable.buttonshape);
        collection_button.setBackgroundResource(R.drawable.buttonshape_selected);
    }


    private void setUIToShopick() {
        Toolbar mActionBarToolbar = getActionBarToolbar();
        if (mActionBarToolbar != null && recyclerView_feed != null) {
            recyclerView_feed.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            if (arcView != null) {
                arcView.setVisibility(View.VISIBLE);
                arcButton.setVisibility(View.VISIBLE);
                arcLayout.setVisibility(View.VISIBLE);
            }
            editText_Search.setQuery("", false);
            mActionBarToolbar.setNavigationIcon(R.drawable.ic_drawer);
            mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reloadedOnResume = true;
                    openNavDrawer();
                    AnalyticsManager.sendEvent("shopickActivity", "HamBurgerMenuOPENED", "OPENED");

                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shopick, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)findViewById(R.id.search_shopick);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchableActivity.class)));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        MenuItem item = menu.findItem(R.id.badge);
        MenuItemCompat.setActionView(item, R.layout.picks_icon_layout);
        View count =  MenuItemCompat.getActionView(item);
        //View count = menu.findItem(R.id.badge).getActionView();
        arcView = (com.hookedonplay.decoviewlib.DecoView) count.findViewById(R.id.dynamicArcView);
        arcLayout = (RelativeLayout)count.findViewById(R.id.arcRelativeLayout);
        arcButton = (Button)count.findViewById(R.id.textPercentage);
        arcButton.setText(AccountUtils.getShopickMyPicks(getApplicationContext()) + " P");
        // Create required data series on the DecoView
        createBackSeries();
        createDataSeries1();
        if (sent) {

        } else {
            sendPicksEvent();
            sent = true;
        }
        // Setup events to be fired on a schedule
        createEvents();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(DispatchIntentUtils.dispatchDisplayPicksIntent(getApplicationContext()));
            }
        };

        //arcView.setText(String.valueOf(mNotifCount)+" P");
        arcView.setOnClickListener(listener);
        arcButton.setOnClickListener(listener);
        arcLayout.setOnClickListener(listener);

        return true;
    }

    private void setNotifCount(Long count){
        mNotifCount = count;
        if (arcButton != null) {
            arcButton.setText(mNotifCount.toString() + " P");
            invalidateOptionsMenu();
        }
    }

    private void createBackSeries() {
        // Create background track
        backindex = arcView.addSeries(new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, 100, 0)
                .setInitialVisibility(false)
                .setLineWidth(15f)
                .build());
    }

    private void createDataSeries1() {
        // Create background track
//Create data series track
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFFF8800"))
                .setRange(0, 100, 0)
                .setLineWidth(15f)
                .build();

        seriesItem1 = arcView.addSeries(seriesItem);

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

    private void createEvents() {
        if (arcView != null ) {
            arcView.executeReset();

            arcView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                    .setIndex(backindex)
                    .setDuration(1500)
                    .setDelay(100)
                    .build());

            arcView.addEvent(new DecoEvent.Builder(99)
                    .setIndex(backindex)
                    .setDuration(800)
                    .setDelay(1700)
                    .build());

            arcView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                    .setIndex(seriesItem1)
                    .setDuration(1500)
                    .setDelay(2600)
                    .build());

            arcView.addEvent(new DecoEvent.Builder((mNotifCount/10) > 100 ? 100 : (mNotifCount/10))
                    .setIndex(seriesItem1)
                    .setDuration(800)
                    .setDelay(4200)
                    .build());

            arcView.addEvent(new DecoEvent.Builder(0)
                    .setIndex(backindex)
                    .setDuration(800)
                    .setDelay(7000)
                    .build());

            arcView.addEvent(new DecoEvent.Builder(0)
                    .setIndex(seriesItem1)
                    .setDuration(800)
                    .setDelay(7000)
                    .build());

            arcView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                    .setIndex(seriesItem1)
                    .setDelay(15000)
                    .setDuration(3000)
                    .setDisplayText(mNotifCount.toString() + "Picks!")
                    .setListener(new DecoEvent.ExecuteEventListener() {
                        @Override
                        public void onEventStart(DecoEvent decoEvent) {

                        }

                        @Override
                        public void onEventEnd(DecoEvent decoEvent) {
                            createEvents();
                        }
                    })
                    .build());
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onStart() {
        super.onStart();
        if(!PrefUtils.isWelcomeDone(getApplicationContext()) && (TextUtils.isEmpty(AccountUtils.getAuthToken(getApplicationContext())) || TextUtils.isEmpty(AccountUtils.getPlusProfileId(getApplicationContext())))) {
            reloadedOnResume =  true;
             startActivityForResult(DispatchIntentUtils.dispatchLoginIntent(getApplicationContext(), true), LOGIN_INTENT_RESULT);
              PrefUtils.markWelcomeDone(getApplicationContext());

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, requestCode + " request code " + resultCode + " result code");
        if (requestCode == LOGIN_INTENT_RESULT && resultCode == Activity.RESULT_OK) {

        }
    }

    @Override
    public boolean canSwipeRefreshChildScrollUp() {
        return true;
    }



    @Override
    public void onResume() {
        super.onResume();
        createEvents();
        if (reloadedOnResume) {
            loadedServer = false;
        }
        if(!loadedServer) {
            sendPostEvent();
        }
        if (!sent) {
            sendPicksEvent();
        }
        sendBannerEvent();
        setUIToShopick();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        createEvents();
        sendBannerEvent();
        setUIToShopick();
    }

    @Override
    public void onBackPressed() {
        //   moveTaskToBack(true);
        if (recyclerView_feed.getVisibility() == View.INVISIBLE) {
            setUIToShopick();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void sendPicksEvent() {
        mBus.post(new LoadPicks());
    }

    private void sendBannerEvent() {
        mBus.post(new LoadBannerEvent());
    }

    @Subscribe
    public void onReloadFeedEventLoaded(ReloadFeedEvent event) {
        loadedServer = false;
        sendPostEvent();
    }

    @Subscribe
    public void onPicksLoaded(PicksLoadedEvent event) {
        if (event.getUser() != null) {
            AccountUtils.setShopickPicks(getApplicationContext(), event.getUser().getPicks());
            if (event.getUser().getPicks() != null) {
                setNotifCount(event.getUser().getPicks());
            }
        } else {
            LOGD(TAG, "User for the picks call is null ???? ");
        }
    }

    @Subscribe
    public void OnBannersLoaded(BannersLoadedEvent event) {
        if (event.getBanners() != null && event.getBanners().size() > 0) {
            Log.d(TAG, event.getBanners().get(0) != null? "" : event.getBanners().get(0).getId() + " ");
            bannerScreenAdapter.mItems.clear();
            bannerScreenAdapter.mItems.addAll(event.getBanners());
            mViewPager.setAdapter(bannerScreenAdapter);
            mViewPager.setOffscreenPageLimit(5);
            bannerLayout.setVisibility(View.VISIBLE);
            num_banners  = event.getBanners().size();
            banners = 0;
            pageSwitcher(8);
        }
    }


    @Subscribe
    public void OnLocalFeedLoaded(LocalFeedLoadedEvent event) {
        if (loadedServer != true && event.getFeed() != null && event.getFeed().size() > 0) {
            Log.d(TAG, event.getFeed().get(0) != null ? "" : event.getFeed().get(0).getId() + " ");
            mFeedAdapters.mItems.clear();
            // mFeedAdapters.mItems.add(new Post());
            mFeedAdapters.mItems.addAll(event.getFeed());
            progressView.stopAnimation();
            recyclerView_feed.invalidate();
            recyclerView_feed.setAdapter(mFeedAdapters);
        }
    }

    @Subscribe
    public void OnFeedLoaded(FeedLoadedEvent event) {
        if ( event.getFeed() != null && event.getFeed().size() > 0) {
            if (loadedServer != true) {
                loadedServer = true;
                Log.d(TAG, event.getFeed().get(0) != null ? "" : event.getFeed().get(0).getId() + " ");
                mFeedAdapters.mItems.clear();
                //  mFeedAdapters.mItems.add(new Post());
                mFeedAdapters.mItems.addAll(event.getFeed());
                progressView.stopAnimation();
                recyclerView_feed.invalidate();
                recyclerView_feed.setAdapter(mFeedAdapters);
                recyclerView_feed.setOnScrollListener(listenerScroll);
            }
        } else {
            mFeedAdapters.mItems.clear();
            //mFeedAdapters.mItems.add(new Post());
            progressView.stopAnimation();
            recyclerView_feed.invalidate();
            recyclerView_feed.setAdapter(mFeedAdapters);
            SnackbarUtil.showMessage(recyclerView_feed, "Couldn't not fetch feed from server at the moment!!");
        }
    }

    // Dialog box

    public void dialogBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Want to know only the offer's running at brands?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        startActivity(DispatchIntentUtils.dispatchAllOfferIntent(getApplicationContext()));
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}




