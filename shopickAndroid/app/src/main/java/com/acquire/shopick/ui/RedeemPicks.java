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
import com.acquire.shopick.event.LoadPicks;
import com.acquire.shopick.event.LoadRedeemPicks;
import com.acquire.shopick.event.PicksLoadedEvent;
import com.acquire.shopick.event.RedeemPicksLoadedEvent;
import com.acquire.shopick.ui.widget.ProgressView;
import com.acquire.shopick.ui.widget.SimpleDividerItemDecoration;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.acquire.shopick.util.LogUtils;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.acquire.shopick.util.LogUtils.LOGD;

/**
 * Created by gaurav on 4/5/16.
 */
public class RedeemPicks extends CoreActivity {

    private static final String TAG = LogUtils.makeLogTag(RedeemPicks.class);
    private static final String SCREEN_NAME = "OPENED_REEDEM_PICKS";
    @Bind(R.id.recycler_earn_pics)
    public RecyclerView mRecyclerView;


    @Bind(R.id.button_open_reedeem_earn)
    Button reedemPicks;

    @Bind(R.id.progress_bar)
    public ProgressView progressView;



    private RedeemPicksRecyclerAdapter redeemPicksRecyclerAdapter;

    private Bus mBus;

    private Long myPicks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_earn_picks);
        super.onCreate(savedInstanceState);
        mBus = BusProvider.getInstance();
        redeemPicksRecyclerAdapter =  new RedeemPicksRecyclerAdapter(getApplicationContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext(), 3, true));
        mRecyclerView.setAdapter(redeemPicksRecyclerAdapter);
        sendRedeemPicksEvent();
        reedemPicks.setText("Earn Picks");
        myPicks = AccountUtils.getShopickMyPicks(getApplicationContext());
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

    private void sendRedeemPicksEvent() {
        mBus = BusProvider.getInstance();
        mBus.post(new LoadRedeemPicks());
    }


    @OnClick(R.id.button_open_reedeem_earn)
    public void openReedem(View view) {
        DispatchIntentUtils.startActivityWithCondition(this, DispatchIntentUtils.dispatchEarnPicksIntent(getApplicationContext()));
        AnalyticsManager.sendEvent("OPEN_REDEEM_EARN_REDEEM","CLICKED","REDEEM");
    }

    @Subscribe
    public void onRedeemPicksEvenLoaded(RedeemPicksLoadedEvent event) {
        if (event.getRedeemPicks() != null && event.getRedeemPicks().size() > 0 ) {
            Log.d(TAG, event.getRedeemPicks().get(0) != null ? "" : event.getRedeemPicks().get(0).getTitle() + " ");
            redeemPicksRecyclerAdapter.mItems.clear();
            // mFeedAdapters.mItems.add(new Post());
            redeemPicksRecyclerAdapter.mItems.addAll(event.getRedeemPicks());
            mRecyclerView.invalidate();
            mRecyclerView.setAdapter(redeemPicksRecyclerAdapter);
            progressView.stopAnimation();
        } else {
            AnalyticsManager.sendEvent("REDEEM_SIZE_ZERO","ERROR","PICKS");
        }
    }

    private void sendPicksEvent() {
        mBus.post(new LoadPicks());
    }

    @Subscribe
    public void onPicksLoaded(PicksLoadedEvent event) {
        if (event.getUser() != null) {
            myPicks = event.getUser().getPicks();
            AccountUtils.setShopickPicks(getApplicationContext(), event.getUser().getPicks());
        } else {
            LOGD(TAG, "User for the picks call is null ???? ");
            AnalyticsManager.sendEvent("REDEEM_CANT_LOAD_USER","ERROR","PICKS");
        }
    }

}
