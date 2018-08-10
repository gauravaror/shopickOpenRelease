package com.acquire.shopick.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.acquire.shopick.Config;
import com.acquire.shopick.R;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.event.LoadPresentationCollectionEvent;
import com.acquire.shopick.event.LoadPresentationDesc;
import com.acquire.shopick.event.LoadUpdateDesc;
import com.acquire.shopick.event.PresentationCollectionLoadedEvent;
import com.acquire.shopick.event.PresentationDescLoaded;
import com.acquire.shopick.event.UpdateCollectionLoadedEvent;
import com.acquire.shopick.event.LoadUpdateCollectionEvent;
import com.acquire.shopick.event.UpdateDescLoaded;
import com.acquire.shopick.io.model.Presentation;
import com.acquire.shopick.io.model.Product;
import com.acquire.shopick.io.model.ProductMini;
import com.acquire.shopick.io.model.Updates;
import com.acquire.shopick.ui.widget.ProgressView;
import com.acquire.shopick.ui.widget.RecyclerItemClickListener;
import com.acquire.shopick.ui.widget.SimpleDividerItemDecoration;
import com.acquire.shopick.ui.widget.SpaceDividerItemDecoration;
import com.acquire.shopick.util.AnalyticsEvent;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.ImageLoader;
import com.acquire.shopick.util.SnackbarUtil;
import com.squareup.otto.Subscribe;


import java.util.ArrayList;

import static com.acquire.shopick.util.LogUtils.LOGD;
import static com.acquire.shopick.util.LogUtils.LOGV;
import static com.acquire.shopick.util.LogUtils.makeLogTag;


public class ProductCollectionActivity extends CoreActivity   {

    private static final String SCREEN_NAME = "OPENED_PRODUCT_COLLECTION_SCREEN";
    private static String TAG = makeLogTag(ProductCollectionActivity.class);
    public int brand_id = -1;
    private String filter;
    private String collection_id;
    private String photoUrl;
    private String title;
    private ImageView mImageView;
    private ImageLoader  mNoPlaceholderImageLoader;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ProductCollectionRecyclerAdapter mCollectionAdaptersRecycler;
    // the cursor whose data we are currently displaying
    private int mUpdateQueryToken;

    private Cursor mCursor;
    private Bundle mArguments;
    private ProgressView mProgressView;
    private RecyclerView mRecyclerView;
    private ArrayList<ProductMini> productMiniList;
    private boolean tip = false;
    private boolean loadedServer = false;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    public static String SHOPICK_COLLECTION_ID = "com.acquire.shopick.android.collection_id_intent";
    public static String SHOPICK_COLLECTION_PHOTO = "com.acquire.shopick.android.collection_photo_intent";
    public static String SHOPICK_COLLECTION_TITLE = "com.acquire.shopick.android.collection_title_intent";
    public static String SHOPICK_COLLECTION_TIP = "com.acquire.shopick.android.collection_tip_intent";

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.collection_layout);
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        Uri data = getIntent().getData();

        boolean intentBased =  false;
        if (b == null || ( b != null && b.getString(SHOPICK_COLLECTION_ID, null) == null ))  {
            intentBased = true;
            String segment =  data.getPathSegments().get(0);
            LOGD(TAG, "presenta sedgl " + segment);
            collection_id  = data.getLastPathSegment();
            if ( "presentation".compareTo(segment) == 0 ) {
                tip =  true;
            }
            title = "Products";

        } else {
            collection_id = b.getString(SHOPICK_COLLECTION_ID, "");
            photoUrl = b.getString(SHOPICK_COLLECTION_PHOTO, "");
            title = b.getString(SHOPICK_COLLECTION_TITLE, "");
            tip = b.getBoolean(SHOPICK_COLLECTION_TIP, false);
        }

        mCollectionAdaptersRecycler = new ProductCollectionRecyclerAdapter(getApplicationContext());
        if (mNoPlaceholderImageLoader == null) {
            mNoPlaceholderImageLoader = new ImageLoader(getApplicationContext(),R.drawable.placeholder);
        }
        mImageView = (ImageView) findViewById(R.id.collection_main_photo);
        if (photoUrl != null) {
            mNoPlaceholderImageLoader.loadImage(Config.PROD_BASE_URL + photoUrl, mImageView);
        }
        mCollectionAdaptersRecycler = new ProductCollectionRecyclerAdapter(getApplicationContext());

       // ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_IMAGE);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

     //   collapsingToolbarLayout.setContentScrimColor(R.color.theme_primary);

        //Set Navigational Items
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_grey_300_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyticsManager.sendEvent(TAG, "Discarded item", "category", 0L);
                supportFinishAfterTransition();
            }
        });

        // collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        staggeredGridLayoutManager = new StaggeredGridLayoutManager( 2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView = (RecyclerView)findViewById(R.id.collection_recycler);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.addItemDecoration(new SpaceDividerItemDecoration(getApplicationContext(), 3, true));
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

        mProgressView =  (ProgressView) findViewById(R.id.progress_collection);
        if( !loadedServer) {
            if (tip) {
                AnalyticsEvent.contentEvent(this, String.valueOf(collection_id), "presentationEvent");

                BusProvider.getInstance().post(new LoadPresentationCollectionEvent(collection_id));
                BusProvider.getInstance().post(new LoadPresentationDesc(collection_id));

            } else {
                AnalyticsEvent.contentEvent(this,String.valueOf(collection_id), "brandupdateEvent");

                BusProvider.getInstance().post(new LoadUpdateCollectionEvent(collection_id));
                BusProvider.getInstance().post(new LoadUpdateDesc(collection_id));

            }
        }
        AnalyticsManager.sendScreenView(SCREEN_NAME);

    }




    @Override
    public   void onResume() {
        super.onResume();
        if (collection_id != null && !loadedServer) {
            if (tip) {
                AnalyticsEvent.contentEvent(this, String.valueOf(collection_id), "presentationEvent");

                BusProvider.getInstance().post(new LoadPresentationCollectionEvent(collection_id));
                BusProvider.getInstance().post(new LoadPresentationDesc(collection_id));
            } else {
                AnalyticsEvent.contentEvent(this, String.valueOf(collection_id), "brandupdateEvent");

                BusProvider.getInstance().post(new LoadUpdateCollectionEvent(collection_id));
                BusProvider.getInstance().post(new LoadUpdateDesc(collection_id));
            }
        }
    }

    @Subscribe
    public void onCollectionDescLoaded(UpdateDescLoaded event) {
        Updates update = event.getUpdate();
        if (update != null) {
            if (mNoPlaceholderImageLoader == null) {
                mNoPlaceholderImageLoader = new ImageLoader(getApplicationContext(), R.drawable.placeholder);
            }
            mNoPlaceholderImageLoader.loadImage(Config.PROD_BASE_URL + update.photoUrl, mImageView);
            collapsingToolbarLayout.setTitle(update.title);
        }

    }

    @Subscribe
    public void onPresentationCollectionDescLoaded(PresentationDescLoaded event) {
        Presentation presentation = event.getPresentation();
        if (presentation != null) {
            if (mNoPlaceholderImageLoader == null) {
                mNoPlaceholderImageLoader = new ImageLoader(getApplicationContext(), R.drawable.placeholder);
            }
            mNoPlaceholderImageLoader.loadImage(Config.PROD_BASE_URL + presentation.photoUrl, mImageView);
            collapsingToolbarLayout.setTitle(presentation.title);
        }

    }

    @Subscribe
    public void onCollectionLoaded(UpdateCollectionLoadedEvent event) {
        if (event.getCollection() != null && !loadedServer) {
            loadedServer = true;
            ArrayList<Product> productMiniList = event.getCollection();
            mCollectionAdaptersRecycler.mItems = productMiniList;
            mRecyclerView.invalidate();
            mRecyclerView.swapAdapter(mCollectionAdaptersRecycler, true);
            mProgressView.stopAnimation();
            mRecyclerView.requestLayout();
        } else if (loadedServer) {
            //Ignore nothing here
        } else {
            mProgressView.stopAnimation();
            SnackbarUtil.viewEmptyNoItems(mRecyclerView);
            finish();

        }

    }

    @Subscribe
    public void onPresentationCollectionLoaded(PresentationCollectionLoadedEvent event) {
        if (event.getCollection() != null && !loadedServer) {
            loadedServer = true;
            ArrayList<Product> productMiniList = event.getCollection();
            mCollectionAdaptersRecycler.mItems = productMiniList;
            mRecyclerView.invalidate();
            mRecyclerView.swapAdapter(mCollectionAdaptersRecycler, true);
            mProgressView.stopAnimation();
        }  else if (loadedServer) {
            //Ignore nothing here
        } else {

            mProgressView.stopAnimation();
            SnackbarUtil.viewEmptyNoItems(mRecyclerView);
            finish();
        }

    }


    private void dispatchProductIntent(String product_id, String photoUrl, String title) {
        Intent collection = new Intent(getApplicationContext(),ProductActivity.class);
        collection.putExtra(ProductActivity.SHOPICK_PRODUCT_ID, product_id);
        collection.putExtra(ProductActivity.SHOPICK_PRODUCT_PHOTO, photoUrl);
        collection.putExtra(ProductActivity.SHOPICK_PRODUCT_TITLE, title);

        // Ensure that there's a camera activity to handle the intent
        startActivity(collection);

    }

}
