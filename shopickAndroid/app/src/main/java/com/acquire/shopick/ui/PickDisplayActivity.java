package com.acquire.shopick.ui;

/**
 * Created by gaurav on 3/29/16.
 */

        import android.content.Context;
        import android.graphics.Color;
        import android.graphics.PointF;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.text.TextUtils;
        import android.view.View;
        import android.view.animation.AnticipateInterpolator;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import com.acquire.shopick.Config;
        import com.acquire.shopick.R;
        import com.acquire.shopick.bus.BusProvider;
        import com.acquire.shopick.dao.User;
        import com.acquire.shopick.event.LeaderboardLoadedEvent;
        import com.acquire.shopick.event.LoadLeaderboard;
        import com.acquire.shopick.event.LoadMonthlyCampign;
        import com.acquire.shopick.event.LoadPicks;
        import com.acquire.shopick.event.MonthlyCampignLoadedEvent;
        import com.acquire.shopick.event.PicksLoadedEvent;
        import com.acquire.shopick.io.model.Referral;
        import com.acquire.shopick.ui.widget.SimpleDividerItemDecoration;
        import com.acquire.shopick.util.AccountUtils;
        import com.acquire.shopick.util.AnalyticsManager;
        import com.acquire.shopick.util.DispatchIntentUtils;
        import com.acquire.shopick.util.ImageLoader;
        import com.acquire.shopick.util.SnackbarUtil;
        import com.facebook.FacebookSdk;
        import com.facebook.appevents.AppEventsLogger;
        import com.hookedonplay.decoviewlib.DecoView;
        import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
        import com.hookedonplay.decoviewlib.charts.SeriesItem;
        import com.hookedonplay.decoviewlib.events.DecoEvent;
        import com.squareup.otto.Bus;
        import com.squareup.otto.Subscribe;

        import butterknife.Bind;
        import butterknife.ButterKnife;

        import static com.acquire.shopick.util.LogUtils.LOGD;
        import static com.acquire.shopick.util.LogUtils.makeLogTag;

public class PickDisplayActivity extends AppCompatActivity {

    private static final String TAG = makeLogTag(PickDisplayActivity.class);
    private static final String SCREEN_NAME = "OPENED_PICK_SCREEN";
    /**
     * DecoView animated arc based chart
     */
    @Bind(R.id.dynamicArcView)
    public DecoView mDecoView;

    @Bind(R.id.leaderboard)
    public RecyclerView mRecyclerView;

    @Bind(R.id.linear_leaderboard)
    public LinearLayout linearLayout;

    @Bind(R.id.earnPicks)
    public Button earnPicks;

    @Bind(R.id.reedemPicks)
    public Button redeemPicks;


    @Bind(R.id.textPercentage)
    TextView textPercentage;

    @Bind(R.id.textRemaining)
    TextView textToGo;

    @Bind(R.id.leaderboard_award_image)
    ImageView leaderboard_price_image;

    @Bind(R.id.leaderboard_description)
    TextView leaderboard_desc;


    private ImageLoader mNoPlaceHolderImageLoader;

    /**
     * Data series index used for controlling animation of {@link DecoView}. These are set when
     * the data series is created then used in {@link #createEvents} to specify what series to
     * apply a given event to
     */
    private int mBackIndex;
    private int mBackIndex2;
    private int mSeries1Index;
    private int mSeries2Index;

    private Bus mBus;

    private Context mContext;

    private LeaderboardRecyclerAdapter leaderboardRecyclerAdapter;
    /**
     * Maximum value for each data series in the {@link DecoView}. This can be different for each
     * data series, in this example we are applying the same all data series
     */
    private final float mSeriesMax = 1000f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_display);
        AnalyticsManager.initializeAnalyticsTracker(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        ButterKnife.bind(this);
        mContext = this;
        mBus = BusProvider.getInstance();
        leaderboardRecyclerAdapter =  new LeaderboardRecyclerAdapter(getApplicationContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext(), 3, true));
        mRecyclerView.setAdapter(leaderboardRecyclerAdapter);

        textPercentage.setText(AccountUtils.getShopickMyPicks(getApplicationContext()).toString() + "Picks");
        textToGo.setText(AccountUtils.getShopickMyPicksMonthly(getApplicationContext()).toString() + " Monthly Picks");

        createBackSeries(mSeriesMax);
        createBackSeries2(mSeriesMax);
        createDataSeries1(mSeriesMax);
        createDataSeries2(mSeriesMax);
        sendPicksEvent();
        sendleaderboardEvent();
        sendlMonthlyCampignEvent();
        earnPicks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DispatchIntentUtils.startActivityWithCondition(mContext, DispatchIntentUtils.dispatchEarnPicksIntent(getApplicationContext()));
                //startActivity(DispatchIntentUtils.dispatchEarnPicksIntent(getApplicationContext()));
            }
        });

        redeemPicks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DispatchIntentUtils.startActivityWithCondition(mContext, DispatchIntentUtils.dispatchRedeemPicksIntent(getApplicationContext()));
            }
        });
        AnalyticsManager.sendScreenView(SCREEN_NAME);
    }

    private void createBackSeries(float max) {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, max, 0)
                .setInitialVisibility(true)
                .build();

        mBackIndex = mDecoView.addSeries(seriesItem);
    }

    private void createBackSeries2(float max) {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, max, 0)
                .setLineWidth(32f)
                .setInset(new PointF(32f, 32f))
                .setInitialVisibility(true)
                .build();

        mBackIndex2 = mDecoView.addSeries(seriesItem);
    }

    private void createDataSeries1(float max) {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFFF8800"))
                .setRange(0, max, 0)
                .setInitialVisibility(false)
                .build();

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                float picks = seriesItem.getInitialValue();
                textPercentage.setText(((int) currentPosition)+" Picks" );
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });




        mSeries1Index = mDecoView.addSeries(seriesItem);
    }

    private void createDataSeries2(float max) {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFFF4444"))
                .setRange(0, max, 0)
                .setLineWidth(32f)
                .setInset(new PointF(32f, 32f))
                .setInitialVisibility(false)
                .build();

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {

                textToGo.setText(((int) currentPosition)+" Monthly Picks");

            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries2Index = mDecoView.addSeries(seriesItem);
    }


    private void createEvents(User user) {
        mDecoView.executeReset();

        mDecoView.addEvent(new DecoEvent.Builder(mSeriesMax)
                .setIndex(mBackIndex)
                .setDuration(3000)
                .setDelay(80)
                .build());
        mDecoView.addEvent(new DecoEvent.Builder(mSeriesMax)
                .setIndex(mBackIndex2)
                .setDuration(3000)
                .setDelay(100)
                .build());


        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries1Index)
                .setDuration(2000)
                .setDelay(1250)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(user.getPicks())
                .setIndex(mSeries1Index)
                .setDelay(3250)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries2Index)
                .setDuration(1000)
                .setEffectRotations(1)
                .setDelay(7000)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(user.getMonthlyPicks())
                .setIndex(mSeries2Index)
                .setDelay(8500)
                .build());


        mDecoView.addEvent(new DecoEvent.Builder(0).setIndex(mSeries2Index).setDelay(18000).build());

        mDecoView.addEvent(new DecoEvent.Builder(0)
                .setIndex(mSeries1Index)
                .setDelay(20000)
                .setDuration(1000)
                .setInterpolator(new AnticipateInterpolator())
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent decoEvent) {

                    }

                    @Override
                    public void onEventEnd(DecoEvent decoEvent) {
                        resetText();
                    }
                })
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                .setIndex(mSeries1Index)
                .setDelay(21000)
                .setDuration(3000)
                .setDisplayText("Picks!")
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent decoEvent) {

                    }

                    @Override
                    public void onEventEnd(DecoEvent decoEvent) {
                        sendPicksEvent();
                    }
                })
                .build());

        resetText();
    }


    @Override
    public void onResume() {
        super.onResume();
        mBus.register(this);
        sendPicksEvent();
        sendleaderboardEvent();
        sendlMonthlyCampignEvent();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
        AppEventsLogger.deactivateApp(this);

    }


    private void resetText() {
        textPercentage.setText("");
        textToGo.setText("");
    }



    private void sendPicksEvent() {
        mBus = BusProvider.getInstance();
        mBus.post(new LoadPicks());
    }

    private void sendleaderboardEvent() {
        mBus = BusProvider.getInstance();
        mBus.post(new LoadLeaderboard());
    }

    private void sendlMonthlyCampignEvent() {
        mBus = BusProvider.getInstance();
        mBus.post(new LoadMonthlyCampign());
    }



    @Subscribe
    public void onPicksLoaded(PicksLoadedEvent event) {
        if (event.getUser() != null) {
            // Create required data series on the DecoView
            Long curr_pick = event.getUser().getPicks();
            Long curr_monthlu_pick = event.getUser().getMonthlyPicks();
            AccountUtils.setShopickPicks(getApplicationContext(), curr_pick);
            AccountUtils.setShopickMyPicksMonthly(getApplicationContext(), curr_monthlu_pick);
            if (curr_pick > mSeriesMax || curr_monthlu_pick > mSeriesMax ) {
                 float curr = Math.max(curr_pick, curr_monthlu_pick)*1.4f;
                createBackSeries(curr);
                createBackSeries2(curr);
                createDataSeries1(curr);
                createDataSeries2(curr);
            }
            // Setup events to be fired on a schedule
            createEvents(event.getUser());
        } else {
            SnackbarUtil.showMessage(mDecoView,"Could not load your picks, Please try later!!");
            LOGD(TAG, "User for the picks call is null ???? ");
        }
    }

    @Subscribe
    public void onLeaderboardLoaded(LeaderboardLoadedEvent event) {
        if (event.getBoard() != null) {
            // Create required data series on the DecoView
            leaderboardRecyclerAdapter.mItems.clear();
            leaderboardRecyclerAdapter.mItems.addAll(event.getBoard());
            mRecyclerView.invalidate();
            mRecyclerView.setAdapter(leaderboardRecyclerAdapter);
            linearLayout.setVisibility(View.VISIBLE);

        } else {
            SnackbarUtil.showMessage(mDecoView,"Could not load your picks, Please try later!!");
            LOGD(TAG, "User for the picks call is null ???? ");
        }
    }

    @Subscribe
    public void oMonthlyCampignLoaded(MonthlyCampignLoadedEvent event) {
        if (event.getMonthlyCampign() != null) {
            // Create required data series on the DecoView
            Referral campign = event.getMonthlyCampign();
            leaderboard_desc.setText(campign.rules);
            if (!TextUtils.isEmpty(campign.imageUrl)) {
                if (mNoPlaceHolderImageLoader == null) {
                    mNoPlaceHolderImageLoader =  new ImageLoader(getApplicationContext());
                }
                mNoPlaceHolderImageLoader.loadImage(Config.PROD_BASE_URL+campign.imageUrl, leaderboard_price_image);
                leaderboard_price_image.setVisibility(View.VISIBLE);
            }
        } else {
            LOGD(TAG, "User for the current campign ???? ");
        }
    }
}