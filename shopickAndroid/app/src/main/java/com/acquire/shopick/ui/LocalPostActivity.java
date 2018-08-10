package com.acquire.shopick.ui;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acquire.shopick.Config;
import com.acquire.shopick.R;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.event.PostLoadedEvent;
import com.acquire.shopick.event.SimilarPostLoadedEvent;
import com.acquire.shopick.job.GetLocalJob;
import com.acquire.shopick.job.GetPostJob;
import com.acquire.shopick.job.GetSimilarPostJob;
import com.acquire.shopick.job.PostLikeJob;
import com.acquire.shopick.job.PostUnLikeJob;
import com.acquire.shopick.ui.widget.BezelImageView;
import com.acquire.shopick.ui.widget.ProgressView;
import com.acquire.shopick.ui.widget.SimpleDividerItemDecoration;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsEvent;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.acquire.shopick.util.FeedUtils;
import com.acquire.shopick.util.FileUtils;
import com.acquire.shopick.util.ImageLoader;
import com.acquire.shopick.util.ImageUtil;
import com.acquire.shopick.util.ShareDialogUtils;
import com.acquire.shopick.util.SnackbarUtil;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Subscribe;

import java.io.FileNotFoundException;

import javax.inject.Inject;

import butterknife.Bind;

import static com.acquire.shopick.util.LogUtils.LOGD;
import static com.acquire.shopick.util.LogUtils.makeLogTag;

/**
 * Created by gaurav on 12/5/15.
 */
public class LocalPostActivity extends CoreActivity {

    private static final String TAG = makeLogTag(LocalPostActivity.class);
    private static final String SCREEN_LABEL = "OPENED_POST";
    private String post_global_id;
    private Post local_post;

    private ImageLoader mNoPlaceholderImageLoader;
    private ImageLoader mPlaceholderImageLoader;
    private HorizontalOfferFeedRecyclerAdapter similarRecyclerAdapter;

    public static String SHOPICK_LOCAL_POST_UNIQ_ID = "com.acquire.shopick.android.local_post_uniq_id";
    public static String SHOPICK_POST_UNIQ_ID = "com.acquire.shopick.android.post_uniq_id";



    //View Items
    @Bind(R.id.meta_post_title)
    TextView post_title;

    @Bind(R.id.feed_post_content)
    TextView post_content;

    @Bind(R.id.poster_photo_colored)
    ImageView post_photo_color;



    @Bind(R.id.poster_image_square)
    public ImageView squareUserImage;


    @Bind(R.id.poster_image)
    BezelImageView user_image;

    @Bind(R.id.share_post)
    Button shareButton;

    @Bind(R.id.find_post)
    Button findButton;


    @Bind(R.id.like_post)
    Button likeButton;

    @Bind(R.id.progress_local_post)
    ProgressView progress_view;

    @Bind(R.id.similar_post_recycler)
    RecyclerView similar_posts;

    @Bind(R.id.similar_product_layout)
    LinearLayout similarPostLayout;


    @Inject
    JobManager jobManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_local_post);
        super.onCreate(savedInstanceState);

        similarRecyclerAdapter =  new HorizontalOfferFeedRecyclerAdapter(this);
        similar_posts.setAdapter(similarRecyclerAdapter);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        similar_posts.setLayoutManager(layoutManager);
        similar_posts.setItemAnimator(new DefaultItemAnimator());
       /* similar_posts.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position < similarRecyclerAdapter.mItems.size()) {
                    Post post = similarRecyclerAdapter.mItems.get( position );
                    dispatchPostOpenIntent(post.getGlobalID());
                }
            }
        }));*/
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        Bundle b = getIntent().getExtras();
        boolean local = false;
        boolean intentBased = false;

        if (b == null || ( b != null && (b.getString(SHOPICK_LOCAL_POST_UNIQ_ID, null) == null) && (b.getString(SHOPICK_POST_UNIQ_ID, null) == null))) {
            LOGD(TAG, "data lf " + data.toString());
            post_global_id = data.getLastPathSegment();
            LOGD(TAG, "local post id " + post_global_id);
            if (!TextUtils.isEmpty(post_global_id)) {
                intentBased = true;
            }
            local = false;
        } else {
            post_global_id = b.getString(SHOPICK_LOCAL_POST_UNIQ_ID, null);
            local = true;
        }

        if (local && !intentBased && post_global_id != null) {
            getLocalPost(post_global_id);
        } else if (intentBased) {
            if (post_global_id != null) {
                getPost(post_global_id);
            }
        }else
            {
            post_global_id = b.getString(SHOPICK_POST_UNIQ_ID, null);
            if (post_global_id != null) {
                getPost(post_global_id);
            }
        }

        AnalyticsEvent.contentEvent(this, post_global_id, "postView");

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
        AnalyticsManager.sendScreenView(SCREEN_LABEL);
    }

    private void dispatchPostOpenIntent(String uniqID) {
        Intent local_post = new Intent(getApplicationContext(), LocalPostActivity.class);
        local_post.putExtra(LocalPostActivity.SHOPICK_POST_UNIQ_ID, uniqID);
        startActivity(local_post);

    }

    void getSimilarPosts(Post post) {
        GetSimilarPostJob localJob =  new GetSimilarPostJob(post);
        jobManager.addJob(localJob);
    }

    void getLocalPost(String post_global_id) {
        GetLocalJob localJob =  new GetLocalJob(post_global_id);
        jobManager.addJob(localJob);
    }

    void getPost(String post_global_id) {
        GetPostJob postJob =  new GetPostJob(post_global_id);
        jobManager.addJob(postJob);
    }

    @Subscribe
    public  void onSimilarPostLoadedEvent(SimilarPostLoadedEvent localPostEvent) {
        if (localPostEvent.getSimilarPost() != null ) {
            similarRecyclerAdapter.mItems.clear();
            similarRecyclerAdapter.mItems.addAll(localPostEvent.getSimilarPost());
            similar_posts.invalidate();
            similar_posts.setAdapter(similarRecyclerAdapter);
         //   int viewHeight = 500;
           // similar_posts.getLayoutParams().height = viewHeight;
            similarPostLayout.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    public  void onPostLoadedEvent(PostLoadedEvent localPostEvent) {
        local_post = localPostEvent.getNew_post();
        if(local_post != null) {
            getSimilarPosts(local_post);
            post_content.setText(local_post.getDescription());

            //post_content.setText();
            post_title.setMovementMethod(LinkMovementMethod.getInstance());
            ViewCompat.setTransitionName(post_photo_color, "photo_");
            final Context mContext = this;
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareDialogUtils.sharePostDialog(mContext, post_title.getText().toString(), local_post.getGlobalID(), local_post.getDescription(), FileUtils.getLocalBitmapUri(post_photo_color), post_photo_color );
                }
            });

            findButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DispatchIntentUtils.startActivityWithCondition(mContext, DispatchIntentUtils.dispatchUpdateMobileIntentPost(mContext, local_post));
                    //mContext.startActivity(DispatchIntentUtils.dispatchUpdateMobileIntentPost(mContext, local_post));
                }
            });
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean liked = local_post.getLiked().booleanValue();
                    if (!AccountUtils.getLoginDone(getApplicationContext())) {
                        SnackbarUtil.createStartingLogin(post_title);
                        AnalyticsManager.sendEvent("LikeWithoutLoging", "Errors", "LocalPostActivity", AccountUtils.getShopickTempProfileId(mContext));
                        DispatchIntentUtils.startActivityWithCondition(mContext, DispatchIntentUtils.dispatchLoginIntent(mContext, true));
                        //mContext.startActivity(DispatchIntentUtils.dispatchLoginIntent(mContext, true));
                        return;
                    }
                    if (liked) {
                        jobManager.addJob(new PostUnLikeJob(local_post.getGlobalID()));
                        likeButton.setTextColor(mContext.getResources().getColor(R.color.map_info_2));
                        likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_black_24dp, 0, 0, 0);
                        local_post.setLiked(false);
                    } else {
                        jobManager.addJob(new PostLikeJob(local_post.getGlobalID()));
                        likeButton.setTextColor(mContext.getResources().getColor(R.color.theme_primary));
                        likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_orange_50_24dp, 0, 0, 0);
                        local_post.setLiked(true);
                    }                }
            });

            boolean liked = local_post.getLiked().booleanValue();

            if (liked) {
                likeButton.setTextColor(mContext.getResources().getColor(R.color.theme_primary));
                likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_orange_50_24dp, 0, 0, 0);
            } else {
                likeButton.setTextColor(mContext.getResources().getColor(R.color.map_info_2));
                likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_black_24dp, 0, 0, 0);

            }

            if (mNoPlaceholderImageLoader == null) {
                mNoPlaceholderImageLoader = new ImageLoader(getApplicationContext());
            }
            if (mPlaceholderImageLoader == null) {
                mPlaceholderImageLoader = new ImageLoader(getApplicationContext(), R.drawable.person_image_empty);
            }
            if ( (local_post.getIsLocal() == null || !local_post.getIsLocal()) && !TextUtils.isEmpty(local_post.getImage_url()) ) {
                post_photo_color.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams param = post_photo_color.getLayoutParams();
                param.height = ActionBar.LayoutParams.WRAP_CONTENT;
                mNoPlaceholderImageLoader.loadImage(Config.PROD_BASE_URL+ local_post.getImage_url(), post_photo_color);
            } else if (local_post.getIsLocal() != null && local_post.getIsLocal() && !TextUtils.isEmpty(local_post.getLocalFileUri())) {
                Uri local_uri = Uri.parse(local_post.getLocalFileUri());

                try {
                    Bitmap post = decodeUri(local_uri, 300, 300);
                    ViewGroup.LayoutParams param = post_photo_color.getLayoutParams();
                    param.height = ActionBar.LayoutParams.WRAP_CONTENT;
                    post_photo_color.setImageBitmap(post);
                } catch (Exception e) {

                }

            } else  {
                post_photo_color.setVisibility(View.GONE);
            }

            if (local_post.getPost_type() == 2) {
                if (!TextUtils.isEmpty(local_post.getBrandname())) {
                    post_title.setText(local_post.getBrandname());
                }
                if (!TextUtils.isEmpty(local_post.getBrand_logo()) ) {
                    // This is a image url we got from google, so send as it is.
                    mPlaceholderImageLoader.loadImage(Config.PROD_BASE_URL + local_post.getBrand_logo(), squareUserImage,mContext.getResources().getDrawable(R.drawable.person_image_empty));
                    user_image.setVisibility(View.GONE);
                    squareUserImage.setVisibility(View.VISIBLE);
                } else {
                    user_image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.person_image_empty));
                }

            } else {
                if (!TextUtils.isEmpty(local_post.getUser_image()) ) {
                    // This is a image url we got from google, so send as it is.
                    mPlaceholderImageLoader.loadImage(local_post.getUser_image(), user_image,mContext.getResources().getDrawable(R.drawable.person_image_empty));
                } else {
                    user_image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.person_image_empty));
                }

                if (local_post.getUsername() == null) {
                } else {
                        post_title.setText(FeedUtils.getFeedTitle(mContext, local_post), TextView.BufferType.SPANNABLE);
                        post_title.setMovementMethod(LinkMovementMethod.getInstance());
                }
            }

        }
        progress_view.stopAnimation();
    }

    public Bitmap decodeUri(Uri uri, int reqWidth, int reqHeight) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(uri)
                , null, o);
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = ImageUtil.calculateInSampleSize(o, reqWidth, reqHeight);
        return BitmapFactory.decodeStream(
                getContentResolver().openInputStream(uri), null, o2);
    }

}
