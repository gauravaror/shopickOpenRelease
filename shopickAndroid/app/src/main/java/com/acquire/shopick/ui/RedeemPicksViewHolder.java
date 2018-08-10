package com.acquire.shopick.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.acquire.shopick.Config;
import com.acquire.shopick.R;
import com.acquire.shopick.io.model.EarnPicks;
import com.acquire.shopick.io.model.RedeemPick;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.acquire.shopick.util.ImageLoader;
import com.acquire.shopick.util.SnackbarUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gaurav on 4/5/16.
 */
public class RedeemPicksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Context mContext;

    @Bind(R.id.earnPicks_title)
    public TextView title;

    @Bind(R.id.earnPicks_description)
    public TextView subtitle;

    @Bind(R.id.earnPicks_given)
    public TextView givenPicks;

    @Bind(R.id.logo_brand_image)
    public ImageView brandLogo;

    RedeemPick redeemPickNew;

    private ImageLoader mNoPlaceholderImageLoader;

    boolean open_it;

    public RedeemPicksViewHolder(View itemView, Context context) {
        super(itemView);
        mContext = context;
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    public void onBindRedeemPickView(RedeemPick redeemPick, boolean open) {
        title.setText(redeemPick.getTitle());
        subtitle.setText(redeemPick.getDescription());
        givenPicks.setText(redeemPick.getRequiredPicks()+ " Picks ");
        if (mNoPlaceholderImageLoader == null) {
            mNoPlaceholderImageLoader = new ImageLoader(mContext);
        }

        if (!TextUtils.isEmpty(redeemPick.getImageUrl()) ) {
            brandLogo.setVisibility(View.VISIBLE);
            mNoPlaceholderImageLoader.loadImage(Config.PROD_BASE_URL+redeemPick.getImageUrl(), brandLogo);
        } else {
            brandLogo.setVisibility(View.GONE);
        }
        open_it = open;

        redeemPickNew = redeemPick;
    }

    @Override
    public void onClick(View v) {
        if (open_it) {
            Long myPicks = AccountUtils.getShopickMyPicks(mContext);
            if (myPicks > redeemPickNew.getRequiredPicks()) {
                mContext.startActivity(DispatchIntentUtils.dispatchUpdateMobileIntentRedeem(mContext, redeemPickNew));
                AnalyticsManager.sendEvent("haveEnoughPicks", "reedem", redeemPickNew.getTitle(), myPicks);
            } else {
                SnackbarUtil.showMessage(itemView, "You just need " + (redeemPickNew.getRequiredPicks() - myPicks) + " Picks to redeem this!!");
                AnalyticsManager.sendEvent("notEnoughPicks", "reedem", redeemPickNew.getTitle(), myPicks);
            }
        }
    }
}
