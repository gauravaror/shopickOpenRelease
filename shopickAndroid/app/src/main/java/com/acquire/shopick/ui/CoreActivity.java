package com.acquire.shopick.ui;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.acquire.shopick.R;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.gcm.RegistrationIntentService;
import com.acquire.shopick.ui.widget.MultiSwipeRefreshLayout;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.acquire.shopick.util.ImageLoader;
import com.acquire.shopick.util.PlayServicesUtils;
import com.acquire.shopick.util.PrefUtils;
import com.google.android.gms.auth.GoogleAuthUtil;

import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.acquire.shopick.util.LogUtils.*;

/**
 * Created by gaurav on 9/11/15.
 */
public class CoreActivity extends AppCompatActivity implements MultiSwipeRefreshLayout.CanChildScrollUpCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = makeLogTag(CoreActivity.class);

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Context context;

    @Nullable @Bind(R.id.drawer)
    public DrawerLayout mDrawerLayout;

    @Nullable @Bind(R.id.navigation_view)
    public NavigationView navigationView;


    // Primary toolbar and drawer toggle
    private Toolbar mActionBarToolbar;

    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShopickApplication.injectMembers(this);
        ButterKnife.bind(this);
        context = this;
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        if(mDrawerLayout != null) {
            mDrawerLayout.setScrimColor(Color.TRANSPARENT);
            mDrawerLayout.setDrawerShadow(R.drawable.selected_navdrawer_item_background, GravityCompat.END);
        }

        AnalyticsManager.initializeAnalyticsTracker(getApplicationContext());
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        PrefUtils.init(this);
        FacebookSdk.sdkInitialize(getApplicationContext());


        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mImageLoader = new ImageLoader(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    private void setDefaultAccountStartup() {
        if (!AccountUtils.hasActiveAccount(this)) {
            LOGD(TAG, "No active account, attempting to pick a default.");
            String defaultAccount = getDefaultAccount();
            if (defaultAccount == null) {
                LOGE(TAG, "Failed to pick default account (no accounts). Failing.");
                //complainMustHaveGoogleAccount();
                Account[] ac = AccountManager.get(this).getAccounts();
                if (ac.length > 0) {
                    AnalyticsManager.sendEvent("NoGoogleAccount", "Errors", ac[0].name, ac.length);
                }
             //   return;
            }
            LOGD(TAG, "Default to: " + defaultAccount);
            AccountUtils.setActiveAccount(this, defaultAccount);
        }
    }

    @Override
    public void onStart() {
        LOGD(TAG, "onStart");
        super.onStart();
        mGoogleApiClient.connect();
        if (TextUtils.isEmpty(AccountUtils.getGCMToken(getApplicationContext()))) {
            setDefaultAccountStartup();
            if (PlayServicesUtils.checkGooglePlaySevices(this)) {
                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            } else {
                AnalyticsManager.sendEvent("PlayServiceNotUpdated", "Errors", "NoIntent");
            }
        }
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(getNavSelector());
            View headerView = navigationView.getHeaderView(0);
            TextView email = (TextView) headerView.findViewById(R.id.email);
            TextView username = (TextView)headerView.findViewById(R.id.username);
            CircleImageView circleImageView = (CircleImageView) headerView.findViewById(R.id.profile_image);
            email.setText(AccountUtils.getActiveAccountName(getApplicationContext()));
            username.setText(AccountUtils.getPlusName(getApplicationContext()));
            if (circleImageView != null && !TextUtils.isEmpty(AccountUtils.getPlusImageUrl(getApplicationContext()))) {
                mImageLoader.loadImage(AccountUtils.getPlusImageUrl(getApplicationContext()), circleImageView, getResources().getDrawable(R.drawable.person_image_empty));
            }
        }
    }





    @Override
    public void onStop() {
        LOGD(TAG, "onStop");
        mGoogleApiClient.disconnect();
        super.onStop();
    }





    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
    }




    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            super.onBackPressed();
        }
    }



    protected boolean isNavDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (mDrawerLayout != null) {
            AnalyticsManager.sendScreenView("CLOSED_NAV_DRAWER");
            mDrawerLayout.closeDrawer(GravityCompat.START);

        }
    }

    protected void openNavDrawer() {
        if (mDrawerLayout != null) {
            AnalyticsManager.sendScreenView("OPENED_NAV_DRAWER");
            mDrawerLayout.openDrawer(GravityCompat.START);
            AnalyticsManager.sendEvent("openNavDrawer", "HamBurgerMenuOPENED", "OPENED");

        }
    }



    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }





    @Override
    public boolean canSwipeRefreshChildScrollUp() {
        return false;
    }




    /**
     * Returns the default account on the device. We use the rule that the first account
     * should be the default. It's arbitrary, but the alternative would be showing an account
     * chooser popup which wouldn't be a smooth first experience with the app. Since the user
     * can easily switch the account with the nav drawer, we opted for this implementation.
     */
    private String getDefaultAccount() {
        // Choose first account on device.
        LOGD(TAG, "Choosing default account (first account on device)");
        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        if (accounts.length == 0) {
            // No Google accounts on d90evice.
            accounts = am.getAccounts();
            if (accounts.length == 0) {
                LOGW(TAG, "No Google accounts on device; not setting default account.");
                return null;
            }
        }

        LOGD(TAG, "Default account is: " + accounts[0].name);
        return accounts[0].name;
    }

    @Override
    public void onConnected(Bundle bundle) {
        LOGD(TAG, "onConnected");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            AccountUtils.setPrefixPrefShopickLastKnownLat(getApplicationContext(), mLastLocation.getLatitude());
            AccountUtils.setPrefixPrefShopickLastKnownLon(getApplicationContext(), mLastLocation.getLongitude());
        }

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public NavigationView.OnNavigationItemSelectedListener getNavSelector() {
        return new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                mDrawerLayout.closeDrawers();

                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {


                    case R.id.alloffers:
                        ShowMetaPostsFragment allOfferFragment = new ShowMetaPostsFragment();
                        allOfferFragment.setArguments(getIntent().getExtras());
                        fragmentTransaction.replace(R.id.frame, allOfferFragment);
                        fragmentTransaction.commit();
                        AnalyticsManager.sendEvent("THREECIRCLEBSCREEN_NAV","CLICKED","OFFER");
                        return true;
                    case R.id.home:
                        ThreeCircleMainHomeFragment threeCircleMainHomeFragment = new ThreeCircleMainHomeFragment();
                        threeCircleMainHomeFragment.setArguments(getIntent().getExtras());
                        fragmentTransaction.replace(R.id.frame, threeCircleMainHomeFragment);
                        fragmentTransaction.commit();
                        AnalyticsManager.sendEvent("THREECIRCLEBSCREEN_NAV","CLICKED","HOME");
                        return true;

                    case R.id.showexplore:
                        LatestLaunchFragment explore_fragment = new LatestLaunchFragment();
                        explore_fragment.setArguments(getIntent().getExtras());
                        fragmentTransaction.replace(R.id.frame, explore_fragment);
                        fragmentTransaction.commit();
                        AnalyticsManager.sendEvent("THREECIRCLEBSCREEN_NAV","CLICKED","LATEST_LAUNCH");
                        return true;
                    case R.id.showfeed:
                        MainFeedFragment feed_fragment = new MainFeedFragment();
                        feed_fragment.setArguments(getIntent().getExtras());
                        // Add the fragment to the 'fragment_container' FrameLayout
                        fragmentTransaction.replace(R.id.frame, feed_fragment);
                        fragmentTransaction.commit();
                        AnalyticsManager.sendEvent("THREECIRCLEBSCREEN_NAV","CLICKED","COLLECTION");
                        return true;
                    case R.id.picks:
                        Intent picks = DispatchIntentUtils.dispatchDisplayPicksIntent(getApplicationContext());
                        DispatchIntentUtils.startActivityWithCondition( context, picks);
                        AnalyticsManager.sendEvent("THREECIRCLEBSCREEN_NAV","CLICKED","PICKS");
                        break;
                    case R.id.post:
                        DispatchIntentUtils.startActivityWithCondition(context, DispatchIntentUtils.dispatchPostIntent(getApplicationContext()));
                        AnalyticsManager.sendEvent("THREECIRCLEBSCREEN_NAV","CLICKED","POST");
                        break;
                    case R.id.referandwin:
                        DispatchIntentUtils.startActivityWithCondition(context,DispatchIntentUtils.dispatchReferralIntent(getApplicationContext()));
                        //startActivity(DispatchIntentUtils.dispatchReferralIntent(getApplicationContext()));
                        AnalyticsManager.sendEvent("THREECIRCLEBSCREEN_NAV","CLICKED","REFER_AND_WIN");
                        break;

                    case R.id.feedback:
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"gaurav@shopick.co.in"});
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Shopick Feedback: "+AccountUtils.getShopickProfileId(getApplicationContext()));
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            try {
                                startActivity(Intent.createChooser(intent, "Send Email"));
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(getApplicationContext(), "Email Client Not installed, Please send feedback email to gaurav@shopick.co.in", Toast.LENGTH_SHORT).show();
                            }

                        }
                        AnalyticsManager.sendEvent("THREECIRCLEBSCREEN_NAV","CLICKED","FEEDBACK");
                        break;
                    case R.id.contactus:
                        Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:9910191466"));
                        try {
                            startActivity(in);
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(getApplicationContext(), "Cannot connect call, Please call 9910191446", Toast.LENGTH_SHORT).show();
                        }
                        AnalyticsManager.sendEvent("THREECIRCLEBSCREEN_NAV","CLICKED","CONTACTUS");
                        break;

                    case R.id.rateus:
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.acquire.shopick")));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(getApplicationContext(), "Cannot Open Play Store", Toast.LENGTH_SHORT).show();
                        }
                        AnalyticsManager.sendEvent("THREECIRCLEBSCREEN_NAV","CLICKED","RATEUS");
                        break;
                    case R.id.settings:
                        DispatchIntentUtils.startActivityWithCondition(context, DispatchIntentUtils.dispatchSettingsIntent(getApplicationContext()));

                        AnalyticsManager.sendEvent("THREECIRCLEBSCREEN_NAV","CLICKED","SETTINGS");
                        break;
                    default:
                        AnalyticsManager.sendEvent("THREECIRCLEBSCREEN_NAV","CLICKED","DEAFULT");
                        break;

                }
                ThreeCircleMainHomeFragment threeCircleMainHomeFragment_ = new ThreeCircleMainHomeFragment();
                threeCircleMainHomeFragment_.setArguments(getIntent().getExtras());
                fragmentTransaction.replace(R.id.frame, threeCircleMainHomeFragment_);
                fragmentTransaction.commit();
                menuItem.setChecked(false);
                return true;
            }
        };
    }
}
