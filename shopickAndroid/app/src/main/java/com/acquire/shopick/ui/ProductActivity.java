package com.acquire.shopick.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acquire.shopick.Config;
import com.acquire.shopick.R;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.dao.Store;
import com.acquire.shopick.event.LoadProductEvent;
import com.acquire.shopick.event.ProductLoadedEvent;


import com.acquire.shopick.io.model.Product;
import com.acquire.shopick.job.ProductLikeJob;
import com.acquire.shopick.job.ProductUnLikeJob;
import com.acquire.shopick.ui.widget.ProgressView;
import com.acquire.shopick.ui.widget.RecyclerItemClickListener;
import com.acquire.shopick.ui.widget.SimpleDividerItemDecoration;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsEvent;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.acquire.shopick.util.FileUtils;
import com.acquire.shopick.util.ImageLoader;
import com.acquire.shopick.util.ShareDialogUtils;
import com.acquire.shopick.util.SnackbarUtil;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import static com.acquire.shopick.util.LogUtils.LOGD;
import static com.acquire.shopick.util.LogUtils.makeLogTag;

/**
 * Created by gaurav on 9/24/15.
 */
public class ProductActivity extends CoreActivity   {

    private static final String SCREEN_NAME = "OPENED_PRODUCT_SCREEN";
    private static String TAG = makeLogTag(ProductActivity.class);
    public int brand_id = -1;
    private String filter;
    private String product_id;
    private String photoUrl;
    private String title;
    private ImageView mImageView;
    private ImageLoader  mNoPlaceholderImageLoader;
    private ProductCollectionRecyclerAdapter mCollectionAdaptersRecycler;
    private StoreRecyclerAdapter mStoreAdapterRecycler;
    private TextView mTitle;
    private TextView mSnippet;

    private TextView storeTitle;
    private TextView storeSnippet;
    private TextView storeDistance;
    private ImageView storePhotoView;
    private LinearLayout mainStoreLayout;
    private Product product;

    private LinearLayout similarProductLayout;

    // the cursor whose data we are currently displaying
    private int mUpdateQueryToken;

    private ProgressView mProgressView;
    private RecyclerView mRecyclerView;
    private boolean loadedServer = false;
    private Button shareButton;
    private Button findButton;
    private Button likeButton;
    private Context mContext;

    @Inject
    public JobManager jobManager;

    public static String SHOPICK_PRODUCT_ID = "com.acquire.shopick.android.product_id_intent";
    public static String SHOPICK_PRODUCT_PHOTO = "com.acquire.shopick.android.product_photo_intent";
    public static String SHOPICK_PRODUCT_TITLE = "com.acquire.shopick.android.product_title_intent";

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.product_layout);
        super.onCreate(savedInstanceState);
        mContext =  this;
        Intent intent = getIntent();
        Uri data = intent.getData();
        Bundle b = getIntent().getExtras();
        if (b == null || ( b != null && TextUtils.isEmpty(b.getString(SHOPICK_PRODUCT_ID, ""))) ) {
            LOGD(TAG, "data product " + data.getLastPathSegment());
            product_id = data.getLastPathSegment();
        } else {
            product_id = b.getString(SHOPICK_PRODUCT_ID, "");
            photoUrl = b.getString(SHOPICK_PRODUCT_PHOTO, "");
            title = b.getString(SHOPICK_PRODUCT_TITLE, "");
        }

        mCollectionAdaptersRecycler = new ProductCollectionRecyclerAdapter(getApplicationContext());

        mTitle = (TextView) findViewById(R.id.product_title);
        mSnippet = (TextView) findViewById(R.id.product_snippet);
        similarProductLayout = (LinearLayout) findViewById(R.id.similar_product_layout);
        shareButton = (Button) findViewById(R.id.share_product);
        likeButton = (Button) findViewById(R.id.like_product);
        findButton = (Button) findViewById(R.id.find_product);


        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialogUtils.shareProductDialog(mContext, mTitle.getText().toString(), product_id, FileUtils.getLocalBitmapUri(mImageView), storePhotoView);
            }
        });

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product != null) {
                    DispatchIntentUtils.startActivityWithCondition(mContext, DispatchIntentUtils.dispatchUpdateMobileIntentProduct(mContext, product));
                    //mContext.startActivity(DispatchIntentUtils.dispatchUpdateMobileIntentProduct(mContext, product));
                } else {
                    SnackbarUtil.showMessage(similarProductLayout, "Wait for product to be loaded!");
                    AnalyticsManager.sendEvent("ProductNotLoaded", "Error", "clickedFindThis");
                }
            }
        });


        AnalyticsEvent.contentEvent(this, String.valueOf(product_id), "productView");

        mStoreAdapterRecycler = new StoreRecyclerAdapter(getApplicationContext());


        if (mNoPlaceholderImageLoader == null) {
            mNoPlaceholderImageLoader = new ImageLoader(getApplicationContext(),R.drawable.placeholder);
        }
        mImageView = (ImageView) findViewById(R.id.product_main_photo);
        if (!TextUtils.isEmpty(photoUrl)) {
            mNoPlaceholderImageLoader.loadImage(Config.PROD_BASE_URL + photoUrl, mImageView);
        }
        mCollectionAdaptersRecycler = new ProductCollectionRecyclerAdapter(getApplicationContext());


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

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mRecyclerView = (RecyclerView)findViewById(R.id.product_recycler);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext(), 3, true));
        mRecyclerView.setAdapter(mCollectionAdaptersRecycler);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // do whatever
                if (position < mCollectionAdaptersRecycler.mItems.size()) {
                    Product product = mCollectionAdaptersRecycler.mItems.get(position);
                    dispatchProductIntent(product.getGlobalID(), product.getImage_url(), product.getTitle());
                }
            }
        }));
        storeTitle = (TextView) findViewById(R.id.store_title);
        storeSnippet = (TextView) findViewById(R.id.store_snippet);
        storePhotoView = (ImageView) findViewById(R.id.store_item_photo_colored);
        mainStoreLayout = (LinearLayout) findViewById(R.id.main_store_layout);
        storeDistance = (TextView) findViewById(R.id.store_distance);

        mProgressView =  (ProgressView) findViewById(R.id.progress_collection);
        if( !loadedServer) {
                BusProvider.getInstance().post(new LoadProductEvent(product_id));
        }
        AnalyticsManager.sendScreenView(SCREEN_NAME);
    }


    @Override
    public   void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(product_id) && !loadedServer) {
                BusProvider.getInstance().post(new LoadProductEvent(product_id));
        }
    }

    @Subscribe
    public void onProductLoaded(ProductLoadedEvent event) {
        if (event.getProduct() != null) {
            loadedServer = true;
            product = event.getProduct();
            mCollectionAdaptersRecycler.mItems = product.getSimilar_products();
            if (product.getSimilar_products().size() == 0 ) {
                similarProductLayout.setVisibility(View.GONE);
            } else {
                similarProductLayout.setVisibility(View.VISIBLE);
            }
            mRecyclerView.invalidate();
            mRecyclerView.setAdapter(mCollectionAdaptersRecycler);

            mTitle.setText(product.getTitle());
            mSnippet.setText(product.getDescription());
            photoUrl = product.getImage_url();
            if (!TextUtils.isEmpty(photoUrl)) {
                mNoPlaceholderImageLoader.loadImage(Config.PROD_BASE_URL + photoUrl, mImageView);
            }
            boolean liked = product.getLiked() ? product.getLiked().booleanValue() : false;
            if (liked) {
                likeButton.setTextColor(mContext.getResources().getColor(R.color.theme_primary));
                likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_orange_50_24dp, 0, 0, 0);

            } else {
                likeButton.setTextColor(mContext.getResources().getColor(R.color.map_info_2));
                likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_black_24dp, 0, 0, 0);

            }
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        if (!AccountUtils.getLoginDone(mContext)) {

                            SnackbarUtil.createStartingLogin(mRecyclerView);
                            AnalyticsManager.sendEvent("LikeWithoutLoging", "Errors", "Product", AccountUtils.getShopickTempProfileId(mContext));
                            DispatchIntentUtils.startActivityWithCondition(mContext, DispatchIntentUtils.dispatchLoginIntent(mContext, true));
                            return;
                        }


                    boolean liked = product.getLiked() ? product.getLiked().booleanValue() : false;
                    if (liked) {
                        jobManager.addJob(new ProductUnLikeJob(product.getGlobalID()));
                        likeButton.setTextColor(mContext.getResources().getColor(R.color.map_info_2));
                        likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_black_24dp, 0, 0, 0);
                        product.setLiked(false);
                        AnalyticsManager.sendEvent("Product", "UnLiked", AccountUtils.getShopickProfileId(mContext) + "");
                    } else {
                        jobManager.addJob(new ProductLikeJob(product.getGlobalID()));
                        likeButton.setTextColor(mContext.getResources().getColor(R.color.theme_primary));
                        likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_orange_50_24dp, 0, 0, 0);
                        product.setLiked(true);
                        AnalyticsManager.sendEvent("Product", "Liked", AccountUtils.getShopickProfileId(mContext) + "");

                    }

                }
            });
            if (product.getStores().size() > 0) {
                Store storeC = product.getStores().get(0);
                storeTitle.setText(storeC.getName());
                storeSnippet.setText(storeC.getAddress());
                storeDistance.setText(storeC.getDistance() + " KM" );
                mNoPlaceholderImageLoader = new ImageLoader(getApplicationContext(), R.drawable.ic_location_on_blue_400_24dp);
                mNoPlaceholderImageLoader.loadImage(Config.PROD_BASE_URL + storeC.getBrand_logo(), storePhotoView);
            } else {
                storeTitle.setVisibility(View.GONE);
                storeSnippet.setVisibility(View.GONE);
                mainStoreLayout.setVisibility(View.GONE);
            }
            mProgressView.stopAnimation();
        } else {
            supportFinishAfterTransition();
        }

    }


    private void dispatchProductIntent(String product_id, String photoUrl, String title) {
        Intent collection = new Intent(getApplicationContext(),ProductActivity.class);
        collection.putExtra(ProductActivity.SHOPICK_PRODUCT_ID, product_id);
        collection.putExtra(ProductActivity.SHOPICK_PRODUCT_PHOTO, photoUrl);
        collection.putExtra(ProductActivity.SHOPICK_PRODUCT_TITLE, title);
        DispatchIntentUtils.startActivityWithCondition(mContext, collection);
        // Ensure that there's a camera activity to handle the intent
        //startActivity(collection);

    }
}
