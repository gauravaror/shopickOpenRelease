package com.acquire.shopick.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.acquire.shopick.Config;
import com.acquire.shopick.R;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.ImageLoader;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.acquire.shopick.util.LogUtils.makeLogTag;

/**
 * Created by gaurav on 7/1/16.
 */

public class HorizonatalOfferViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

private static final String TAG = makeLogTag(HorizonatalOfferViewHolder.class) ;
private Post p;

    @Bind(R.id.poster_photo_colored)
    public ImageView photoView;

    @Bind(R.id.brand_name)
    public TextView brandName;

    @Bind(R.id.post_desc)
    public TextView postDesc;


    private Context mContext;
    private View mView;
    private ImageLoader mNoPlaceholderImageLoader;
    private ImageLoader mPlaceholderImageLoader;
    boolean showText_;



    public HorizonatalOfferViewHolder(View view, Context con) {
        super(view);
        ButterKnife.bind(this, view);
        mView = view;
        mContext = con;
        mView.setOnClickListener(this);
        photoView.setOnClickListener(this);
    }

    public void bindHorizontalOffer(final Post update, final Context mContext, boolean showText) {

        p = update;
        if (mNoPlaceholderImageLoader == null) {
        mNoPlaceholderImageLoader = new ImageLoader(mContext);
        }
        if (mPlaceholderImageLoader == null) {
        mPlaceholderImageLoader = new ImageLoader(mContext,R.drawable.person_image_empty);
        }
        showText_ = showText;
        if (!TextUtils.isEmpty(p.getImage_url()) ) {
            photoView.setVisibility(View.VISIBLE);
            mNoPlaceholderImageLoader.loadImage(Config.PROD_BASE_URL + p.getImage_url(), photoView, mContext.getResources().getDrawable(R.drawable.placeholder_verticle));

        } else {
            photoView.setVisibility(View.GONE);
        }
        if (showText) {
            if (!TextUtils.isEmpty(p.getDescription())) {
                postDesc.setText(p.getDescription());
                postDesc.setVisibility(View.VISIBLE);
            } else {
                postDesc.setVisibility(View.GONE);
            }

            postDesc.setMovementMethod(LinkMovementMethod.getInstance());
            if (!TextUtils.isEmpty(p.getBrandname())) {
                brandName.setText(p.getBrandname());
                brandName.setVisibility(View.VISIBLE);
            } else {
                brandName.setVisibility(View.GONE);
            }
        } else {
            brandName.setVisibility(View.GONE);
            postDesc.setVisibility(View.GONE);
        }


    }


    @Override
    public void onClick(View v) {
        if (showText_) {
            AnalyticsManager.sendEvent("OPENED_POST","Similar_POSTS",p.getGlobalID());
        } else {
            AnalyticsManager.sendEvent("OPENED_POST","META_POST",p.getGlobalID());
        }

        dispatchPostOpenIntent(p.getGlobalID());
    }


    private void dispatchPostOpenIntent(String uniqID) {
        Intent local_post = new Intent(mContext,LocalPostActivity.class);
        local_post.putExtra(LocalPostActivity.SHOPICK_POST_UNIQ_ID, uniqID);
        local_post.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Activity activity = (Activity)mContext;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity.startActivity(local_post, ActivityOptionsCompat
                    .makeSceneTransitionAnimation((Activity) mContext, photoView,  mContext.getResources().getString(R.string.image_photo)).toBundle());
        } else  {
            activity.startActivity(local_post);
        }
    }
}