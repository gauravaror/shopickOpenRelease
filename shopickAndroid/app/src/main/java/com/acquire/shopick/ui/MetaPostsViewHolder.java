package com.acquire.shopick.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acquire.shopick.Config;
import com.acquire.shopick.R;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.dao.PostCollection;
import com.acquire.shopick.job.PostCollectionLikeJob;
import com.acquire.shopick.job.PostCollectionUnLikeJob;
import com.acquire.shopick.job.PostLikeJob;
import com.acquire.shopick.job.PostUnLikeJob;
import com.acquire.shopick.ui.widget.BezelImageView;
import com.acquire.shopick.ui.widget.SimpleDividerItemDecoration;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.acquire.shopick.util.FeedUtils;
import com.acquire.shopick.util.FileUtils;
import com.acquire.shopick.util.ImageLoader;
import com.acquire.shopick.util.ShareDialogUtils;
import com.acquire.shopick.util.SnackbarUtil;
import com.path.android.jobqueue.JobManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.acquire.shopick.util.LogUtils.LOGD;
import static com.acquire.shopick.util.LogUtils.makeLogTag;

/**
 * Created by gaurav on 10/4/15.
 */
public class MetaPostsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = makeLogTag(MetaPostsViewHolder.class);

    private PostCollection postCollection;

    @Bind(R.id.feed_post_content)
    public TextView snippet;

    private Context mContext;

    @Bind(R.id.meta_post_title)
    public TextView title;

    @Bind(R.id.poster_image)
    public ImageView bezelImageView;

    private ImageLoader mNoPlaceholderImageLoader;
    private ImageLoader mPlaceholderImageLoader;

    @Bind(R.id.share_post)
    public Button button;

    @Bind(R.id.like_post)
    public Button likebutton;

    @Bind(R.id.find_post)
    public Button findButton;

    @Bind(R.id.recycler_meta_feed_internal)
    RecyclerView mActualPostItems;

    private HorizontalOfferFeedRecyclerAdapter mFeedRecyclerAdapter;

    private View mView;

    @Inject
    JobManager jobManager;

    public MetaPostsViewHolder(View view, Context con) {
        super(view);
        ShopickApplication.injectMembers(this);
        ButterKnife.bind(this, view);
        mView = view;
        mContext = con;
        button.setTag(0);
        likebutton.setTag(1);
        findButton.setTag(2);
        view.setOnClickListener(this);
        button.setOnClickListener(this);
        likebutton.setOnClickListener(this);
        findButton.setOnClickListener(this);
        title.setOnClickListener(this);
        bezelImageView.setOnClickListener(this);
    }

    public void bindMetaPostsItem(final PostCollection update, final Context mContext) {

        postCollection = update;
        if (postCollection.getBrand_name() == null) {
        } else {
            title.setText(FeedUtils.getHorizontalOfferTitle(mContext, update));
        }
        title.setMovementMethod(LinkMovementMethod.getInstance());
        title.setLinkTextColor(mContext.getResources().getColor( R.color.body_text_1));
        if (mNoPlaceholderImageLoader == null) {
            mNoPlaceholderImageLoader = new ImageLoader(mContext);
        }
        if (mPlaceholderImageLoader == null) {
            mPlaceholderImageLoader = new ImageLoader(mContext,R.drawable.person_image_empty);
        }

        if (!TextUtils.isEmpty(postCollection.getBrand_logo()) ) {
            // This is a image url we got from google, so send as it is.
            mPlaceholderImageLoader.loadImage(Config.PROD_BASE_URL + postCollection.getBrand_logo(), bezelImageView, mContext.getResources().getDrawable(R.drawable.person_image_empty));
        } else {
            bezelImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.person_image_empty));
        }

        mFeedRecyclerAdapter = new HorizontalOfferFeedRecyclerAdapter(mContext);
        mFeedRecyclerAdapter.mItems.clear();
        if (update.getPostListCustom() != null) {
            Iterator postItr = update.getPostListCustom().iterator();
            while (postItr.hasNext()) {
                mFeedRecyclerAdapter.mItems.add((Post) postItr.next());
            }
            mFeedRecyclerAdapter.mItems.add(new Post());
            mFeedRecyclerAdapter.addShowAll = true;
            mFeedRecyclerAdapter.showAllPostCollection = update;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        mActualPostItems.setLayoutManager(linearLayoutManager);
        mActualPostItems.setHasFixedSize(true);
        mFeedRecyclerAdapter.showText = false;
        mActualPostItems.setAdapter(mFeedRecyclerAdapter);
        int viewHeight = 500;
        mActualPostItems.getLayoutParams().height = viewHeight;
        mActualPostItems.setVisibility(View.VISIBLE);


        boolean liked = postCollection.getLiked() ? postCollection.getLiked().booleanValue() : false ;
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
                AnalyticsManager.sendEvent("Share","META_POST",postCollection.getGlobalID());
                ShareDialogUtils.sharePostCollectionDialog(mContext, title.getText().toString(), postCollection.getGlobalID(), postCollection.getDescription(), FileUtils.getLocalBitmapUri(bezelImageView), mView);
            } else if ((int)v.getTag() == 2 ) {
                DispatchIntentUtils.startActivityWithCondition(mContext, DispatchIntentUtils.dispatchUpdateMobileIntentPostCollection(mContext, postCollection));
                AnalyticsManager.sendEvent("FIND_THIS","META_POST","FINDTHIS");
                //mContext.startActivity(DispatchIntentUtils.dispatchUpdateMobileIntentPostCollection(mContext, postCollection));
            } else if ((int)v.getTag() == 1 ) {
                if (!AccountUtils.getLoginDone(mContext)) {
                    SnackbarUtil.createStartingLogin(mView);
                    AnalyticsManager.sendEvent("LikeWithoutLoging", "Errors", "META_POST", AccountUtils.getShopickTempProfileId(mContext));
                    DispatchIntentUtils.startActivityWithCondition(mContext, DispatchIntentUtils.dispatchLoginIntent(mContext, true));
                  //  mContext.startActivity(DispatchIntentUtils.dispatchLoginIntent(mContext, true));
                    return;
                }

                boolean liked = postCollection.getLiked() ? postCollection.getLiked().booleanValue() : false ;
                if (liked) {
                    jobManager.addJob(new PostCollectionUnLikeJob(postCollection.getGlobalID()));
                    likebutton.setTextColor(mContext.getResources().getColor(R.color.map_info_2));
                    likebutton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_black_24dp, 0, 0, 0);
                    postCollection.setLiked(false);
                    AnalyticsManager.sendEvent("LIKE","META_POST",postCollection.getGlobalID());
                    AnalyticsManager.sendEvent("META_POST", "UnLiked", AccountUtils.getShopickProfileId(mContext) + "");

                } else {
                    jobManager.addJob(new PostCollectionLikeJob(postCollection.getGlobalID()));
                    likebutton.setTextColor(mContext.getResources().getColor(R.color.theme_primary));
                    likebutton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_orange_50_24dp, 0, 0, 0);
                    postCollection.setLiked(true);
                    AnalyticsManager.sendEvent("META_POST", "Liked", AccountUtils.getShopickProfileId(mContext) + "");

                }
            }
        }  else {
            dispatchPostCollectionOpenIntent(postCollection.getGlobalID());
        }
    }


    private void dispatchPostCollectionOpenIntent(String uniqID) {
        Intent local_post_collection = new Intent(mContext,PostCollectionActivity.class);
        local_post_collection.putExtra(PostCollectionActivity.SHOPICK_POST_COLLECTION_ID, uniqID);
        local_post_collection.putExtra(PostCollectionActivity.SHOPICK_POST_COLLECTION_TITLE, title.getText().toString());
        local_post_collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        DispatchIntentUtils.startActivityWithCondition(mContext, local_post_collection);
        AnalyticsManager.sendEvent("META_POST_CLICKED","META_POST",uniqID);
    }
}