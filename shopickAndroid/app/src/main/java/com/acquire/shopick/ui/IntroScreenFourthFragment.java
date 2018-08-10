package com.acquire.shopick.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acquire.shopick.R;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.event.LoadRedeemPicks;
import com.acquire.shopick.event.RedeemPicksLoadedEvent;
import com.acquire.shopick.ui.widget.SimpleDividerItemDecoration;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.FeedUtils;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by gaurav on 7/12/16.
 */

public class IntroScreenFourthFragment extends Fragment {

    @Bind(R.id.recycler_earn_pics)
    public RecyclerView mRecyclerView;

    private RedeemPicksRecyclerAdapter redeemPicksRecyclerAdapter;

    private Bus mBus;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRoot = inflater.inflate(R.layout.intro_screen_fourth_fragment, container, false);
        ButterKnife.bind(this, mRoot);
        AnalyticsManager.sendEvent("INTRO_SCREEN","OPENED","FOURTH");
        mBus = BusProvider.getInstance();
        redeemPicksRecyclerAdapter =  new RedeemPicksRecyclerAdapter(getApplicationContext());
        redeemPicksRecyclerAdapter.open_it = false;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext(), 3, true));
        mRecyclerView.setAdapter(redeemPicksRecyclerAdapter);
        sendRedeemPicksEvent();

        return mRoot;
    }

    private void sendRedeemPicksEvent() {
        mBus = BusProvider.getInstance();
        mBus.post(new LoadRedeemPicks());
    }

    @Subscribe
    public void onRedeemPicksEvenLoaded(RedeemPicksLoadedEvent event) {
        redeemPicksRecyclerAdapter.mItems.clear();
        // mFeedAdapters.mItems.add(new Post());
        redeemPicksRecyclerAdapter.mItems.addAll(event.getRedeemPicks());
        mRecyclerView.invalidate();
        mRecyclerView.setAdapter(redeemPicksRecyclerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

}
