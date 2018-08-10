package com.acquire.shopick.ui;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.acquire.shopick.Config;
import com.acquire.shopick.R;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.event.LoadPicks;
import com.acquire.shopick.event.PicksLoadedEvent;
import com.acquire.shopick.io.network.ShopickApi;
import com.acquire.shopick.ui.widget.SimpleDividerItemDecoration;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsEvent;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.acquire.shopick.util.PrefUtils;
import com.acquire.shopick.util.SnackbarUtil;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

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
import static com.acquire.shopick.util.LogUtils.makeLogTag;

/**
 * Created by gaurav on 6/7/16.
 */

public class ChoiceBrowserActivity  extends CoreActivity {

    private static final String TAG = makeLogTag(ChoiceBrowserActivity.class);
    private static final String SCREEN_LABEL = "ChoiceBrowser";


    static com.hookedonplay.decoviewlib.DecoView arcView;

    private static RelativeLayout arcLayout;

    private static Button arcButton;

    static Long mNotifCount = 10L;

    private  int backindex;
    private int seriesItem1;


    @Inject
    JobManager jobManager;

    ShopickApi.SearchService service;

    private Bus mBus;
    private Context mContext;


    @Bind(R.id.shopick_main_layout)
    public LinearLayout mainShopickLayout;

    @Bind(R.id.general_search)
    public RecyclerView mRecyclerView;

    @Bind(R.id.toolbar_actionbar)
    public Toolbar toolbar;

    @Bind(R.id.search_shopick_button)
    Button searchShopick;

    private SearchRecyclerAdapter mSearchAdaptersRecycler;
    public static int LOGIN_INTENT_RESULT =  1023;
    private boolean sent = false;

    // Primary toolbar and drawer toggle
    private Toolbar mActionBarToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_SideBarShopick);
        setContentView(R.layout.activity_choice_browser);
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        //Getting Bus
        mBus = BusProvider.getInstance();
        mSearchAdaptersRecycler = new SearchRecyclerAdapter(getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext(), 10, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mSearchAdaptersRecycler);
        // Initializing Toolbar and setting it as the actionbar
        setSupportActionBar(toolbar);

        ThreeCircleMainHomeFragment threeCircleMainHomeFragment_ = new ThreeCircleMainHomeFragment();
        threeCircleMainHomeFragment_.setArguments(getIntent().getExtras());
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, threeCircleMainHomeFragment_);
        fragmentTransaction.commit();
        AnalyticsManager.sendScreenView(SCREEN_LABEL);
    }


    @OnClick(R.id.search_shopick_button)
    public void searchClick(View mView) {
        DispatchIntentUtils.startActivityWithCondition(this,DispatchIntentUtils.dispatchSearchIntent(getApplicationContext(),true));
        AnalyticsManager.sendEvent("THREECIRCLEBSCREEN","CLICKED","SEARCH");
        new NetworkOperation().execute();
    }


    private class NetworkOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            try {

                AbstractXMPPConnection connection = new XMPPTCPConnection("mh", "gaurav", "controller.shopick.co.in");
                connection.connect().login();

                Chat chat = ChatManager.getInstanceFor(connection)
                        .createChat("admin1@controller.shopick.co.in", new ChatMessageListener() {
                            @Override
                            public void processMessage(Chat chat, org.jivesoftware.smack.packet.Message message) {
                                System.out.println("Received message: " + message);
                            }
                        });
                chat.sendMessage("Howdy!");
            } catch (Exception e) {
                System.out.println("Received message: " + e.getMessage() + e.getStackTrace());
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            DispatchIntentUtils.startActivityWithCondition(this, DispatchIntentUtils.dispatchSettingsIntent(getApplicationContext()));
            return true;
        }

        if (id == R.id.action_picks) {
            DispatchIntentUtils.startActivityWithCondition(this, DispatchIntentUtils.dispatchDisplayPicksIntent(getApplicationContext()));
            return true;
        }

        if (id == R.id.action_search) {
            DispatchIntentUtils.startActivityWithCondition(this, DispatchIntentUtils.dispatchSearchIntent(getApplicationContext(),true));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setUIToShopick() {
        Toolbar mActionBarToolbar = getActionBarToolbar();
        if (mActionBarToolbar != null && mainShopickLayout != null) {
            mainShopickLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            if (arcView != null) {
                arcView.setVisibility(View.VISIBLE);
                arcButton.setVisibility(View.VISIBLE);
                arcLayout.setVisibility(View.VISIBLE);
            }
            mActionBarToolbar.setNavigationIcon(R.drawable.ic_drawer);
            mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openNavDrawer();
                    AnalyticsManager.sendEvent("shopickActivity", "HamBurgerMenuOPENED", "OPENED");

                }
            });
        }
    }

    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shopick, menu);

    /*    // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)findViewById(R.id.search_shopick);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchableActivity.class)));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
*/
        MenuItem item = menu.findItem(R.id.badge);
        MenuItemCompat.setActionView(item, R.layout.picks_icon_layout);
        View count =  MenuItemCompat.getActionView(item);
        //View count = menu.findItem(R.id.badge).getActionView();
        arcView = (com.hookedonplay.decoviewlib.DecoView) count.findViewById(R.id.dynamicArcView);
        arcLayout = (RelativeLayout)count.findViewById(R.id.arcRelativeLayout);
        arcButton = (Button)count.findViewById(R.id.textPercentage);
        arcButton.setText(String.format("%d P", AccountUtils.getShopickMyPicks(getApplicationContext())));
        // Create required data series on the DecoView
        createBackSeries();
        createDataSeries1();
        if (!sent) {
            sendPicksEvent();
            sent = true;
        }
        // Setup events to be fired on a schedule
        createEvents();
        View.OnClickListener listener = v -> startActivity(DispatchIntentUtils.dispatchDisplayPicksIntent(getApplicationContext()));         AnalyticsManager.sendEvent("THREECIRCLEBSCREEN","CLICKED","NAV_PICKS");



        //arcView.setText(String.valueOf(mNotifCount)+" P");
        arcView.setOnClickListener(listener);
        arcButton.setOnClickListener(listener);
        arcLayout.setOnClickListener(listener);

        return true;
    }

    private void setNotifCount(Long count){
        mNotifCount = count;
        if (arcButton != null) {
            arcButton.setText(String.format("%s P", mNotifCount.toString()));
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
    public void onStart() {
        super.onStart();
        if(!PrefUtils.isWelcomeDone(getApplicationContext()) && (TextUtils.isEmpty(AccountUtils.getAuthToken(getApplicationContext())) || TextUtils.isEmpty(AccountUtils.getPlusProfileId(getApplicationContext())))) {
            startActivityForResult(DispatchIntentUtils.dispatchLoginIntent(getApplicationContext(), true), LOGIN_INTENT_RESULT);
            PrefUtils.markWelcomeDone(getApplicationContext());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, requestCode + " request code " + resultCode + " result code");
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!sent) {
            sendPicksEvent();
            sent = true;
        }
        createEvents();
        setUIToShopick();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        createEvents();
    }

    @Override
    public void onBackPressed() {
        //   moveTaskToBack(true);
        if (mainShopickLayout.getVisibility() == View.INVISIBLE) {
            mainShopickLayout.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
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


    private void sendPicksEvent() {
        mBus.post(new LoadPicks());
    }


}

