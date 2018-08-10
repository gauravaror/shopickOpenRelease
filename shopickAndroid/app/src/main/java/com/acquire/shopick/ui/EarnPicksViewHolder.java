package com.acquire.shopick.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.acquire.shopick.R;
import com.acquire.shopick.io.model.*;
import com.acquire.shopick.io.model.EarnPicks;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gaurav on 4/5/16.
 */
public class EarnPicksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Context mContext;

    @Bind(R.id.earnPicks_title)
    public TextView title;

    @Bind(R.id.earnPicks_description)
    public TextView subtitle;

    @Bind(R.id.earnPicks_given)
    public TextView givenPicks;

    EarnPicks earnPicksNew;

    public EarnPicksViewHolder(View itemView, Context context) {
        super(itemView);
        mContext = context;
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    public void onBindEarnPickView(com.acquire.shopick.io.model.EarnPicks earnPicks) {
        title.setText(earnPicks.getTitle());
        subtitle.setText(earnPicks.getDescription());
        givenPicks.setText(earnPicks.getGivenPicks()+ " Picks ");
        earnPicksNew = earnPicks;
    }

    @Override
    public void onClick(View v) {
        AnalyticsManager.sendEvent("PICKS", "earnPicks", earnPicksNew.getTitle(), AccountUtils.getShopickMyPicks(mContext));
        Intent intent_deep = new Intent (Intent.ACTION_VIEW);
        intent_deep.setData(Uri.parse(earnPicksNew.getIntentUrl()));
        intent_deep.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent_deep);
    }
}
