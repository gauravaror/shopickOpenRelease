package com.acquire.shopick.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.acquire.shopick.R;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.event.EarnPicksLoadedEvent;
import com.acquire.shopick.event.LoadEarnPicks;
import com.acquire.shopick.ui.widget.ProgressView;
import com.acquire.shopick.ui.widget.SimpleDividerItemDecoration;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.acquire.shopick.util.LogUtils;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaurav on 4/5/16.
 */
public class EarnPicks extends CoreActivity {

    private static final String TAG = LogUtils.makeLogTag(EarnPicks.class);
    private static final String SCREEN_NAME = "OPENED_EARN_PICKS";
    @Bind(R.id.recycler_earn_pics)
    public RecyclerView mRecyclerView;

    @Bind(R.id.button_open_reedeem_earn)
    Button earnPicks;

    @Bind(R.id.progress_bar)
    public ProgressView progressView;


    private EarnPicksRecyclerAdapter earnPicksRecyclerAdapter;

    private Bus mBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_earn_picks);
        super.onCreate(savedInstanceState);
        mBus = BusProvider.getInstance();
        earnPicksRecyclerAdapter =  new EarnPicksRecyclerAdapter(getApplicationContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext(), 3, true));
        mRecyclerView.setAdapter(earnPicksRecyclerAdapter);
        sendEarnPicksEvent();
        earnPicks.setText("Reedem Picks");
        //Set Navigational Items
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyticsManager.sendEvent(TAG, "Discarded item", "category", 0L);
                supportFinishAfterTransition();
            }
        });
        //Setting Title Color as white.
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        AnalyticsManager.sendScreenView(SCREEN_NAME);
    }

    private void sendEarnPicksEvent() {
        mBus = BusProvider.getInstance();
        mBus.post(new LoadEarnPicks());
    }


    @OnClick(R.id.button_open_reedeem_earn)
    public void openReedem(View view) {
        DispatchIntentUtils.startActivityWithCondition(this, DispatchIntentUtils.dispatchRedeemPicksIntent(getApplicationContext()));
        AnalyticsManager.sendEvent("OPEN_REDEEM_EARN_REDEEM","CLICKED","EARN");
    }

    @Subscribe
    public void OnLocalFeedLoaded(EarnPicksLoadedEvent event) {
        if (event.getEarnPickses() != null && event.getEarnPickses().size() > 0 ) {
            Log.d(TAG, event.getEarnPickses().get(0) != null ? "" : event.getEarnPickses().get(0).getTitle() + " ");
            earnPicksRecyclerAdapter.mItems.clear();
            // mFeedAdapters.mItems.add(new Post());
            earnPicksRecyclerAdapter.mItems.addAll(event.getEarnPickses());
            mRecyclerView.invalidate();
            mRecyclerView.setAdapter(earnPicksRecyclerAdapter);
            progressView.stopAnimation();

        } else {
            AnalyticsManager.sendEvent("EARNPICKS_SIZE_ZERO","ERROR","PICKS");
        }
    }
}
