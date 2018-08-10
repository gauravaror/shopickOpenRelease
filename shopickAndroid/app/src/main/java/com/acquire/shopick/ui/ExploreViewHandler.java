package com.acquire.shopick.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acquire.shopick.Config;
import com.acquire.shopick.R;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.dao.BrandUpdates;
import com.acquire.shopick.io.model.Updates;
import com.acquire.shopick.job.BrandUpdateLikeJob;
import com.acquire.shopick.job.BrandUpdateUnLikeJob;
import com.acquire.shopick.job.PostLikeJob;
import com.acquire.shopick.job.PostUnLikeJob;
import com.acquire.shopick.model.UpdatesCollectionItem;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.acquire.shopick.util.FileUtils;
import com.acquire.shopick.util.ImageLoader;
import com.acquire.shopick.util.ShareDialogUtils;
import com.acquire.shopick.util.SnackbarUtil;
import com.acquire.shopick.util.UIUtils;
import com.path.android.jobqueue.JobManager;

import javax.inject.Inject;

/**
 * Created by gaurav on 10/4/15.
 */
public class ExploreViewHandler  extends RecyclerView.ViewHolder implements View.OnClickListener {

    private BrandUpdates item;
    private ImageView photoView;
    private TextView snippet;
    private TextView snippet_short;
    private TextView brand;

    private TextView title;
    private ImageLoader mNoPlaceholderImageLoader;
    private int mDefaultSessionColor;
    private FrameLayout linear;
    private ImageButton likeButton;
    private ImageButton shareButton;
    private View mView;
    private Context mContext;

    @Inject
    JobManager jobManager;


    public ExploreViewHandler(View view, Context context) {
        super(view);
        ShopickApplication.injectMembers(this);
        mView =  view;
        mContext = context;
        title = (TextView) view.findViewById(R.id.session_title);
        snippet = (TextView) view.findViewById(R.id.session_snippet);
        likeButton = (ImageButton) view.findViewById(R.id.like_explore);
        shareButton = (ImageButton) view.findViewById(R.id.share_explore);
        photoView = (ImageView) view.findViewById(R.id.session_photo_colored);
        linear = (FrameLayout) view.findViewById(R.id.session_target);
        brand = (TextView) view.findViewById(R.id.session_category);
        view.setOnClickListener(this);
        likeButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);
    }

    private void dispatchUpdateCollectionIntent(String globalID, String photoUrl, String title) {
        Intent collection = new Intent(mContext, ProductCollectionActivity.class);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        collection.putExtra(ProductCollectionActivity.SHOPICK_COLLECTION_ID, globalID);
        collection.putExtra(ProductCollectionActivity.SHOPICK_COLLECTION_PHOTO, photoUrl);
        collection.putExtra(ProductCollectionActivity.SHOPICK_COLLECTION_TITLE, title);
        collection.putExtra(ProductCollectionActivity.SHOPICK_COLLECTION_TIP, false);
        DispatchIntentUtils.startActivityWithCondition(mContext, collection);
        // Ensure that there's a camera activity to handle the intent
    }

    public void bindExploreItem(BrandUpdates update,Context mContext, boolean showBrandLabel) {
        item = update;
        title.setText(item.getTitle());
        snippet.setText(item.getDescription());
        int darkSessionColor = 0;
        mDefaultSessionColor = mContext.getResources().getColor(R.color.theme_accent_1_light);

        darkSessionColor = UIUtils.scaleSessionColorToDefaultBG(mDefaultSessionColor);

        if (photoView != null) {
            linear.getBackground().setColorFilter(UIUtils.makeSessionImageScrimColorFilter(darkSessionColor));

        }
        ViewCompat.setTransitionName(photoView, "photo_");
        if (mNoPlaceholderImageLoader == null) {
            mNoPlaceholderImageLoader = new ImageLoader(mContext,R.drawable.placeholder);
        }
        if (item.getImage_url() != null) {
            mNoPlaceholderImageLoader.loadImage(Config.PROD_BASE_URL + item.getImage_url(), photoView, mContext.getResources().getDrawable(R.drawable.placeholder));
        }
        boolean liked;
        if (item.getLiked() == null) {
            item.setLiked(false);
        }
        if (showBrandLabel) {
            brand.setVisibility(View.VISIBLE);
            brand.setText(item.getBrand_name());
        }

        liked = item.getLiked().booleanValue() ;
        if (liked) {
            likeButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_orange_50_24dp));
        } else {
            likeButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof  ImageButton) {
            if ( R.id.share_explore == v.getId()) {
                ShareDialogUtils.shareExploreDialog(mContext, title.getText().toString(), item.getGlobalID(), FileUtils.getLocalBitmapUri(photoView), mView);
            } else if (R.id.like_explore == v.getId()) {
                if (!AccountUtils.getLoginDone(mContext)) {
                    SnackbarUtil.createStartingLogin(mView);
                    AnalyticsManager.sendEvent("LikeWithoutLoging", "Errors", "PostCollection", AccountUtils.getShopickTempProfileId(mContext));
                    DispatchIntentUtils.startActivityWithCondition(mContext, DispatchIntentUtils.dispatchLoginIntent(mContext, true));
                    //mContext.startActivity(DispatchIntentUtils.dispatchLoginIntent(mContext, true));
                    return;
                }
                boolean liked = item.getLiked().booleanValue();
                if (liked) {
                    jobManager.addJob(new BrandUpdateUnLikeJob(item.getGlobalID()));
                    likeButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                    item.setLiked(false);
                    AnalyticsManager.sendEvent("Post", "UnLiked", AccountUtils.getShopickProfileId(mContext) + "");

                } else {
                    jobManager.addJob(new BrandUpdateLikeJob(item.getGlobalID()));
                    item.setLiked(true);
                    likeButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_orange_50_24dp));
                    AnalyticsManager.sendEvent("Post", "Liked", AccountUtils.getShopickProfileId(mContext) + "");

                }
            }
        } else {
            dispatchUpdateCollectionIntent(item.getGlobalID(), item.getImage_url(), item.getTitle());

        }
    }

}
