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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.acquire.shopick.Config;
import com.acquire.shopick.R;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.dao.PostCollection;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.acquire.shopick.util.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.acquire.shopick.util.LogUtils.makeLogTag;

/**
 * Created by gaurav on 7/1/16.
 */
public class SeeAllAndPOstViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = makeLogTag(HorizonatalOfferViewHolder.class) ;

    @Bind(R.id.see_all_button)
    public Button see_all;

    @Bind(R.id.post_post_collection)
    public Button post_collection;

    PostCollection post_col;

    private Context mContext;
    private View mView;
    private ImageLoader mNoPlaceholderImageLoader;
    private ImageLoader mPlaceholderImageLoader;




    public SeeAllAndPOstViewHolder(View view, Context con) {
        super(view);
        ButterKnife.bind(this, view);
        mView = view;
        mContext = con;
        mView.setOnClickListener(this);
        see_all.setTag(0);
        post_collection.setTag(1);
        see_all.setOnClickListener(this);
        post_collection.setOnClickListener(this);
    }

    public void bindHorizontalOffer(final PostCollection pc, final Context mContext) {

        post_col = pc;
        if (mNoPlaceholderImageLoader == null) {
            mNoPlaceholderImageLoader = new ImageLoader(mContext);
        }
        if (mPlaceholderImageLoader == null) {
            mPlaceholderImageLoader = new ImageLoader(mContext,R.drawable.person_image_empty);
        }
    }


    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            if ((int)v.getTag() == 0) {
                AnalyticsManager.sendEvent("META_POST_COLLECTION_OPEN","CLICKED",post_col.getGlobalID());
                DispatchIntentUtils.startActivityWithCondition(mContext, DispatchIntentUtils.dispatchPostCollectionOpenIntent(mContext, post_col));
            } else {
                AnalyticsManager.sendEvent("META_POST_POST_WITH_THIS","CLICKED",post_col.getGlobalID());
                DispatchIntentUtils.startActivityWithCondition(mContext, DispatchIntentUtils.dispatchPostWithPostCollectionIntent(mContext, post_col));
            }
        }

    }



}