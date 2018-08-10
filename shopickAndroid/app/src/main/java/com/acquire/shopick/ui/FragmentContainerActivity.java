package com.acquire.shopick.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.acquire.shopick.R;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.LogUtils;
import com.acquire.shopick.util.SnackbarUtil;

/**
 * Created by gaurav on 3/14/16.
 */
public class FragmentContainerActivity extends CoreActivity {

    private static final String TAG = LogUtils.makeLogTag(FragmentContainerActivity.class);
    public static String SHOPICK_BRAND_ID = "com.acquire.shopick.android.brand_id_intent";
    public static String SHOPICK_BRAND_PHOTO = "com.acquire.shopick.android.brand_photo_intent";
    public static String SHOPICK_BRAND_TITLE = "com.acquire.shopick.android.brand_title_intent";
    public static String SHOPICK_BRAND_OFFER = "com.acquire.shopick.android.brand_offer_intent";
    public static String SHOPICK_CATEGORY_ID = "com.acquire.shopick.android.category_id_intent";
    public static String SHOPICK_CATEGORY_PHOTO = "com.acquire.shopick.android.category_photo_intent";
    public static String SHOPICK_CATEGORY_TITLE = "com.acquire.shopick.android.category_title_intent";
    public static String SHOPICK_CATEGORY_OFFER = "com.acquire.shopick.android.category_offer_intent";


    public static String SHOPICK_FRAGMENT_CONTAINER_GENERIC_WHICH = "com.acquire.shopick.android.shopick_fragment_container_generic_which";
    public static String SHOPICK_FRAGMENT_CONTAINER_GENERIC_TITLE = "com.acquire.shopick.android.shopick_fragment_container_generic_title";

    public static final int FRAGMENT_TYPE_FEEDITEMFRAGMENT = 8029;
    public static final int FRAGMENT_TYPE_FEEDMAINFRAGMENT = 4949;
    public static final int FRAGMENT_TYPE_TIPSFRAGMENT = 9229;
    public static final int FRAGMENT_TYPE_EXPLOREFRAGMENT = 6429;
    public static final int FRAGMENT_TYPE_AllOFFERFRAGMENT = 8349;
    public static final int FRAGMENT_TYPE_METAPOSTSFRAGMENT = 9838;
    public static final int FRAGMENT_TYPE_LatestLaunch = 7449;
    public static final int FRAGMENT_TYPE_EXPLOREFRAGMENT_ITEM = 6353;
    public static final int FRAGMENT_TYPE_METAPOSTITEMFRAGMENT = 8736;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_post_collection);
        super.onCreate(savedInstanceState);


        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

        }
        Uri data = getIntent().getData();

        int fragmentType = getIntent().getIntExtra(SHOPICK_FRAGMENT_CONTAINER_GENERIC_WHICH, -1);
        String fragmentTitle = getIntent().getStringExtra(SHOPICK_FRAGMENT_CONTAINER_GENERIC_TITLE);
        if (fragmentType == -1) {
            if (data.getLastPathSegment() != null) {
                fragmentType = Integer.parseInt(data.getLastPathSegment());
                fragmentTitle = "Shopick";
            } else {
                SnackbarUtil.showMessage(findViewById(R.id.fragment_container), "Sorry! Content is not available!");
                finish();
            }
        }

        switch (fragmentType) {
            case FRAGMENT_TYPE_FEEDMAINFRAGMENT:
                MainFeedFragment mainFeedFragment = new MainFeedFragment();
                mainFeedFragment.setArguments(getIntent().getExtras());
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, mainFeedFragment).commit();
                break;
            case FRAGMENT_TYPE_FEEDITEMFRAGMENT:
                FeedItemFragment feed_fragment = new FeedItemFragment();
                feed_fragment.setArguments(getIntent().getExtras());
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, feed_fragment).commit();
                break;

            case FRAGMENT_TYPE_METAPOSTITEMFRAGMENT:
                MetaPostItemFragment metaPostItemFragment = new MetaPostItemFragment();
                metaPostItemFragment.setArguments(getIntent().getExtras());
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, metaPostItemFragment).commit();
                break;
            case FRAGMENT_TYPE_EXPLOREFRAGMENT:
                ExploreFragment tips_fragment = new ExploreFragment();
                tips_fragment.setArguments(getIntent().getExtras());
                // Add tips_fragment fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, tips_fragment).commit();
                break;
            case FRAGMENT_TYPE_EXPLOREFRAGMENT_ITEM:
                ExploreItemFragment exploreItemFragment = new ExploreItemFragment();
                exploreItemFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, exploreItemFragment).commit();
                break;
            case FRAGMENT_TYPE_TIPSFRAGMENT:
                PresentationFragment presentationFragment = new PresentationFragment();
                presentationFragment.setArguments(getIntent().getExtras());
                // Add tips_fragment fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, presentationFragment).commit();
                break;
            case FRAGMENT_TYPE_AllOFFERFRAGMENT:
                AllOfferFragment allOfferFragment = new AllOfferFragment();
                allOfferFragment.setArguments(getIntent().getExtras());
                // Add tips_fragment fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, allOfferFragment).commit();
                break;
            case FRAGMENT_TYPE_METAPOSTSFRAGMENT:
                ShowMetaPostsFragment metaPostsFragment = new ShowMetaPostsFragment();
                metaPostsFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, metaPostsFragment).commit();
                break;
            case FRAGMENT_TYPE_LatestLaunch:
                LatestLaunchFragment latestLaunchFragment = new LatestLaunchFragment();
                latestLaunchFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, latestLaunchFragment).commit();
                break;
            default:
                MainFeedFragment feed_fragment_ = new MainFeedFragment();
                feed_fragment_.setArguments(getIntent().getExtras());
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, feed_fragment_).commit();


        }


        //Set Navigational Items
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyticsManager.sendEvent(TAG, "Discarded item", "postCollectionEvent", 0L);
                supportFinishAfterTransition();
            }
        });
        //Setting Title Color as white.
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle(fragmentTitle);
        setTitle(fragmentTitle);

    }

}
