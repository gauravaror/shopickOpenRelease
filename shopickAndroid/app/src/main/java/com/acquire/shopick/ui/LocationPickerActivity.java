package com.acquire.shopick.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.acquire.shopick.R;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.dao.Store;
import com.acquire.shopick.event.LoadStoresEvent;
import com.acquire.shopick.event.StoresLoadedEvent;
import com.acquire.shopick.job.GetStoresJob;
import com.acquire.shopick.ui.widget.ProgressView;
import com.acquire.shopick.util.AnalyticsManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Subscribe;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.acquire.shopick.util.LogUtils.LOGD;
import static com.acquire.shopick.util.LogUtils.makeLogTag;

public class LocationPickerActivity extends CoreActivity  implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = makeLogTag(LocationPickerActivity.class) ;
    private static final String SCREEN_LABEL = "CategoryPicker";
    private ListView list;
    private Cursor mCursor;
    private LocationPickerAdapter mStoreAdapter;
    private GoogleApiClient mGoogleApiClient;
    boolean locationLoaded = false;
    @Bind(R.id.filter_stores)  EditText mFilterStores;

    @Inject
    public JobManager jobManager;

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.location_picker);
        super.onCreate(savedInstanceState);
        list = (ListView)findViewById(R.id.list_stores);
        mStoreAdapter =  new LocationPickerAdapter(getApplicationContext());
        //Set the back button.
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_blue_400_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyticsManager.sendEvent(TAG, "Discarded item", "category", 0L);
                supportFinishAfterTransition();
            }
        });
        mFilterStores.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String text = mFilterStores.getText().toString().toLowerCase(Locale.getDefault());
                mStoreAdapter.filter(text);
                list.invalidate();
                list.setAdapter(mStoreAdapter);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //Build Api Client.
        buildGoogleApiClient();
        //Set other details.
        toolbar.setTitle("Post to Shopick");
        toolbar.setBackgroundColor(getResources().getColor(R.color.white_pressed));
        toolbar.setTitleTextColor(getResources().getColor(R.color.black_semi_transparent));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Store tag = mStoreAdapter.mItems.get(position);
                intent.putExtra(PostFeedItem.EXTRA_STORE_ID, tag.getId());
                intent.putExtra(PostFeedItem.EXTRA_STORE_NAME, tag.getName());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        AnalyticsManager.sendScreenView(SCREEN_LABEL);


    }

    @Override
    public void onStart() {
        super.onStart();
            mGoogleApiClient.connect();

    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }




    private void SetupCategories() {

        mCursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_category_search, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
       // Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
         //       mGoogleApiClient);
        jobManager.addJob(new GetStoresJob());
        if (!locationLoaded) {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location mLastLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            LOGD(TAG, mLastLocation == null ? "Location not know: giving null" : mLastLocation.toString());
            if (mLastLocation != null) {
                double lat = (mLastLocation.getLatitude());
                double lon = (mLastLocation.getLongitude());
                LoadStoresEvent loadStoresEvent = new LoadStoresEvent();
                loadStoresEvent.setLat(lat);
                loadStoresEvent.setLon(lon);
                BusProvider.getInstance().post(loadStoresEvent);
            }
            BusProvider.getInstance().post(new LoadStoresEvent());
        }
    }


    @Subscribe
    public void OnStoresLoaded(StoresLoadedEvent event) {
        if ( (mStoreAdapter.mItems.size() == 0 || event.isLatAvailable())  && event.getStores() != null &&  (event.getStores()).size() > 0){
            mStoreAdapter.mItems.clear();
            mStoreAdapter.mItemsCopy.clear();
            mStoreAdapter.mItems.addAll(event.getStores());
            mStoreAdapter.mItemsCopy.addAll(event.getStores());
            ProgressView progressView = (ProgressView) findViewById(R.id.progress_stores);
            progressView.stopAnimation();
            list.invalidate();
            list.setAdapter(mStoreAdapter);
        }
    }



    @Override
    public void onConnected(Bundle bundle) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LOGD(TAG,"Connection connected");

        if (mLastLocation != null) {
            locationLoaded = true;
            double lat = (mLastLocation.getLatitude());
            double lon = (mLastLocation.getLongitude());
            LoadStoresEvent loadStoresEvent = new LoadStoresEvent();
            loadStoresEvent.setLat(lat);
            loadStoresEvent.setLon(lon);
            BusProvider.getInstance().post(loadStoresEvent);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        LOGD(TAG,"Connection Suspended");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        LOGD(TAG,"Connection Failed");

    }
}
