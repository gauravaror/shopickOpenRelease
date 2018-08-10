package com.acquire.shopick.ui;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.acquire.shopick.Config;
import com.acquire.shopick.R;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.job.PostLikeJob;
import com.acquire.shopick.job.PostUnLikeJob;
import com.acquire.shopick.ui.widget.BezelImageView;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.acquire.shopick.util.FeedUtils;
import com.acquire.shopick.util.FileUtils;
import com.acquire.shopick.util.ImageLoader;
import com.acquire.shopick.util.ShareDialogUtils;
import com.acquire.shopick.util.SnackbarUtil;
import com.path.android.jobqueue.JobManager;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.acquire.shopick.util.LogUtils.LOGD;
import static com.acquire.shopick.util.LogUtils.makeLogTag;

/**
 * Created by gaurav on 10/4/15.
 */
public class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = makeLogTag(FeedViewHolder.class) ;
    private Post p;

    @Bind(R.id.poster_photo_colored)
    public ImageView photoView;

    @Bind(R.id.feed_post_content)
    public TextView snippet;


    private Context mContext;

    @Bind(R.id.featured_in)
    public TextView featured_in;


    @Bind(R.id.meta_post_title)
    public TextView title;

    @Bind(R.id.poster_image)
    public BezelImageView bezelImageView;


    @Bind(R.id.poster_image_square)
    public ImageView squareUserImage;

    private ImageLoader mNoPlaceholderImageLoader;
    private ImageLoader mPlaceholderImageLoader;

    @Bind(R.id.share_post)
    public Button button;

    @Bind(R.id.like_post)
    public Button likebutton;

    @Bind(R.id.find_post)
    public Button findButton;

    private View mView;

    @Inject
    JobManager jobManager;

    public FeedViewHolder(View view, Context con) {
        super(view);
        ShopickApplication.injectMembers(this);
        ButterKnife.bind(this, view);
        mView = view;
        mContext = con;
        button.setTag(0);
        likebutton.setTag(1);
        findButton.setTag(2);
        featured_in.setTag(0);
        view.setOnClickListener(this);
        button.setOnClickListener(this);
        likebutton.setOnClickListener(this);
        findButton.setOnClickListener(this);
    }

    public void bindExploreItem(final Post update, final Context mContext) {

        p = update;
        featured_in.setVisibility(View.GONE);

        title.setMovementMethod(LinkMovementMethod.getInstance());
        title.setLinkTextColor(mContext.getResources().getColor( R.color.body_text_1));
        snippet.setText(p.getDescription());
        ViewCompat.setTransitionName(photoView, "photo_");
        if (mNoPlaceholderImageLoader == null) {
            mNoPlaceholderImageLoader = new ImageLoader(mContext);
        }
        if (mPlaceholderImageLoader == null) {
            mPlaceholderImageLoader = new ImageLoader(mContext,R.drawable.person_image_empty);
        }
        if (!TextUtils.isEmpty(p.getImage_url())) {
            photoView.setVisibility(View.VISIBLE);
            mNoPlaceholderImageLoader.loadImage(Config.PROD_BASE_URL + p.getImage_url(), photoView);

        } else {
            photoView.setVisibility(View.GONE);
        }
        if ((p.getPost_type() != null) && (p.getPost_type() == 2)) {
            if (!TextUtils.isEmpty(p.getBrandname())) {
                title.setText(p.getBrandname());
            }
            if (!TextUtils.isEmpty(p.getBrand_logo()) ) {
                // This is a image url we got from google, so send as it is.
                mPlaceholderImageLoader.loadImage(Config.PROD_BASE_URL + p.getBrand_logo(), squareUserImage, mContext.getResources().getDrawable(R.drawable.person_image_empty));
                squareUserImage.setVisibility(View.VISIBLE);
                bezelImageView.setVisibility(View.GONE);
            } else {
                bezelImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.person_image_empty));
            }

        } else {
            if (!TextUtils.isEmpty(p.getUser_image()) ) {
                // This is a image url we got from google, so send as it is.
                mPlaceholderImageLoader.loadImage(p.getUser_image(), bezelImageView,mContext.getResources().getDrawable(R.drawable.person_image_empty));
            } else {
                bezelImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.person_image_empty));
            }

            if (p.getUsername() == null) {
            } else {
                title.setText(FeedUtils.getFeedTitle(mContext, p), TextView.BufferType.SPANNABLE);
            }
        }

        if (!TextUtils.isEmpty(p.getFeatured_in_title()) && !TextUtils.isEmpty(p.getFeatured_in_globalID())) {
            featured_in.setText(FeedUtils.getFeaturedTitle(p.getFeatured_in_title(), p.getFeatured_in_globalID(), mContext));
            featured_in.setVisibility(View.VISIBLE);
            featured_in.setMovementMethod(LinkMovementMethod.getInstance());

        }

        boolean liked;
        if (p.getLiked() != null) {
            liked = p.getLiked().booleanValue();
        } else {
            p.setLiked(false);
            liked = p.getLiked().booleanValue();
        }
        if (liked) {
            likebutton.setTextColor(mContext.getResources().getColor(R.color.theme_primary));
            likebutton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_orange_50_24dp, 0, 0, 0);
        } else {
            likebutton.setTextColor(mContext.getResources().getColor(R.color.map_info_2));
            likebutton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_black_24dp, 0, 0, 0);
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button){
            if ((int)v.getTag() == 0 ) {
                LOGD(TAG, "share instance on click");
                ShareDialogUtils.sharePostDialog(mContext, title.getText().toString(), p.getGlobalID(), p.getDescription(), FileUtils.getLocalBitmapUri(photoView), mView);
            } else if ((int)v.getTag() == 2 ) {
                DispatchIntentUtils.startActivityWithCondition(mContext, DispatchIntentUtils.dispatchUpdateMobileIntentPost(mContext, p));
            } else if ((int)v.getTag() == 1 ) {
                if (!AccountUtils.getLoginDone(mContext)) {
                    SnackbarUtil.createStartingLogin(mView);
                    AnalyticsManager.sendEvent("LikeWithoutLoging", "Errors", "Post", AccountUtils.getShopickTempProfileId(mContext));
                    DispatchIntentUtils.startActivityWithCondition(mContext, DispatchIntentUtils.dispatchLoginIntent(mContext, true));
                    return;
                }
                boolean liked = p.getLiked().booleanValue();
                if (liked) {
                    jobManager.addJob(new PostUnLikeJob(p.getGlobalID()));
                    likebutton.setTextColor(mContext.getResources().getColor(R.color.map_info_2));
                    likebutton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_black_24dp, 0, 0, 0);
                    p.setLiked(false);
                    AnalyticsManager.sendEvent("Post", "UnLiked", AccountUtils.getShopickProfileId(mContext) + "");

                } else {
                    jobManager.addJob(new PostLikeJob(p.getGlobalID()));
                    likebutton.setTextColor(mContext.getResources().getColor(R.color.theme_primary));
                    likebutton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_orange_50_24dp, 0, 0, 0);
                    p.setLiked(true);
                    AnalyticsManager.sendEvent("Post", "Liked", AccountUtils.getShopickProfileId(mContext) + "");

                }
            }
        } else if (v instanceof TextView) {
            if ((int)v.getTag() == 0) {
                DispatchIntentUtils.startActivityWithCondition(mContext, DispatchIntentUtils.dispatchDeepLinkIntent(mContext, "android://www.shopick.co.in/post_collection/" + p.getFeatured_in_globalID()));
            } else {
                dispatchPostOpenIntent(p.getGlobalID());
            }

        } else {
          dispatchPostOpenIntent(p.getGlobalID());
        }
    }

    private void dispatchPostOpenIntent(String uniqID) {
        Intent local_post = new Intent(mContext,LocalPostActivity.class);
        local_post.putExtra(LocalPostActivity.SHOPICK_POST_UNIQ_ID, uniqID);
        local_post.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        DispatchIntentUtils.startActivityWithCondition(mContext, local_post);

    }
}