package com.acquire.shopick.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.acquire.shopick.Config;
import com.acquire.shopick.R;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.dao.Brands;
import com.acquire.shopick.job.BrandLikeJob;
import com.acquire.shopick.job.BrandUnLikeJob;
import com.acquire.shopick.job.PostUnLikeJob;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.ImageLoader;
import com.acquire.shopick.util.SnackbarUtil;
import com.acquire.shopick.util.UIUtils;
import com.path.android.jobqueue.JobManager;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gaurav on 3/21/16.
 */
public class LikedBrandViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener {

    @Bind(R.id.text_brandname)
    TextView brand_name;

    @Bind(R.id.image_logo)
    ImageView imageView;

    @Bind(R.id.image_tick)
    ImageView selected_symbol;

    View holder_view;

    Brands curr_brand;

    private ImageLoader mNoPlaceholderImageLoader;
    private Context mContext;

    @Inject
    JobManager jobManager;


    public LikedBrandViewHolder(View itemView) {
        super(itemView);
        holder_view = itemView;
        ShopickApplication.injectMembers(this);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    public void bindBrandItem(Brands brand, Context mContext_) {
        mContext = mContext_;
        curr_brand = brand;
      //  brand_name.setText(brand.getName());
        brand_name.setVisibility(View.GONE);
        selected_symbol.setVisibility(View.GONE);
        imageView.setColorFilter(null);
        if (mNoPlaceholderImageLoader == null) {
            mNoPlaceholderImageLoader = new ImageLoader(mContext);
        }
        mNoPlaceholderImageLoader.loadImage(Config.PROD_BASE_URL + brand.getLogo_url(), imageView);

        if (curr_brand != null && curr_brand.getLiked() != null && curr_brand.getLiked().booleanValue()) {
            selected_symbol.setVisibility(View.VISIBLE);
            imageView.setColorFilter(UIUtils.makeSessionImageScrimColorFilter(Color.YELLOW));

        } else {
            selected_symbol.setVisibility(View.GONE);
            imageView.setColorFilter(null);
        }

    }

    @Override
    public void onClick(View v) {
        if (curr_brand == null) {
            return;
        }
        if (selected_symbol.getVisibility() == View.GONE) {
            jobManager.addJob(new BrandLikeJob(curr_brand.getId()));
            selected_symbol.setVisibility(View.VISIBLE);
            imageView.setColorFilter(UIUtils.makeSessionImageScrimColorFilter(Color.YELLOW));
            AnalyticsManager.sendEvent("Likedbrands", "Liked", AccountUtils.getShopickProfileId(mContext) + "");
            curr_brand.setLiked(true);
        } else {
            jobManager.addJob(new BrandUnLikeJob(curr_brand.getId()));
            selected_symbol.setVisibility(View.GONE);
            imageView.setColorFilter(null);
            curr_brand.setLiked(false);
            AnalyticsManager.sendEvent("Likedbrands", "UnLiked", AccountUtils.getShopickProfileId(mContext) + "");

        }
}

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
