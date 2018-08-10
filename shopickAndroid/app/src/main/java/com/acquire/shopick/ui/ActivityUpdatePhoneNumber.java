package com.acquire.shopick.ui;

import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.acquire.shopick.R;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.job.UpdatePhoneNumberJob;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.path.android.jobqueue.JobManager;

import org.w3c.dom.Text;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.acquire.shopick.util.LogUtils.LOGD;
import static com.acquire.shopick.util.LogUtils.makeLogTag;

/**
 * Created by gaurav on 3/3/16.
 */

public class ActivityUpdatePhoneNumber extends CoreActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String SCREEN_NAME = "OPENED_UPDATE_PHONENO_SCREEN";
    private static final String TAG = makeLogTag(ActivityUpdatePhoneNumber.class) ;
    @Bind(R.id.editText_phonenumber)
    EditText phone_edit;

    @Bind(R.id.update_phonenumber)
    Button phone_update;

    @Bind(R.id.display_text_number)
    TextView display;

    @Inject
    JobManager jobManager;


    String globalID;
    Long post_id;
    Long product_id;
    Long redeem_id;
    Long banner_id;
    Long post_collection_id;
    boolean post_;
    boolean redeem;
    boolean banner;
    boolean post_collection;

    int user_id;
    String phoneno_;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.update_number);
        super.onCreate(savedInstanceState);


        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyticsManager.sendEvent("FindUpdateNumber", "Close", "", 0L);
                supportFinishAfterTransition();
            }
        });


        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        
        
       globalID =  getIntent().getStringExtra("globalID");
        post_ = getIntent().getBooleanExtra("post", true);
        redeem = getIntent().getBooleanExtra("redeem", false);
        banner = getIntent().getBooleanExtra("banner", false);
        post_collection = getIntent().getBooleanExtra("post_collection", false);
        if (redeem) {
            display.setText(getIntent().getStringExtra("redeem_title"));
            phone_update.setText("Redeem");
            redeem_id = getIntent().getLongExtra("redeem_id", -1L);

        } else if (banner) {
            display.setText(getIntent().getStringExtra("banner_title"));
            phone_update.setText("Locate");
            banner_id = getIntent().getLongExtra("banner_id", -1L);
        } else if (post_collection) {
            phone_update.setText("Locate");
            post_collection_id = getIntent().getLongExtra("post_collection_id", -1L);
        } else if (post_) {
            post_id = getIntent().getLongExtra("post_id", -1L);
        } else {
            product_id = getIntent().getLongExtra("product_id", -1L);
        }
        user_id = AccountUtils.getShopickProfileId(getApplicationContext());

        phoneno_ = AccountUtils.getShopickProfilePhoneno(getApplicationContext());

        if (!TextUtils.isEmpty(phoneno_)) {
            phone_edit.setText(phoneno_);
        }
        AnalyticsManager.sendScreenView(SCREEN_NAME);
        toolbar.setTitleTextColor(getResources().getColor(R.color.material_white));

    }

    @OnClick(R.id.update_phonenumber)
    void onClick(View view) {
        if (phone_edit.getText().length() > 8) {
            AccountUtils.setShopickProfilePhoneno(getApplicationContext(), AccountUtils.getActiveAccountName(getApplicationContext()), phone_edit.getText().toString());
            if (mLastLocation != null) {
                jobManager.addJob(new UpdatePhoneNumberJob(globalID, post_id, product_id, redeem_id, banner_id, post_collection_id, banner, redeem, post_, post_collection, user_id, phone_edit.getText().toString(),mLastLocation.getLatitude(), mLastLocation.getLongitude()));
            } else {
                jobManager.addJob(new UpdatePhoneNumberJob(globalID, post_id, product_id, redeem_id, banner_id, post_collection_id, banner, redeem, post_, post_collection,user_id, phone_edit.getText().toString(), -1, -1));
            }
            dialogBox();
        }
    }

    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        LOGD(TAG, "onConnected");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

    }

    @Override
    public void onConnectionSuspended(int i) {
        LOGD(TAG, "onConnectionSuspended");
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        LOGD(TAG, "onConnectionFailed");
    }

    public void dialogBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Thanks for your query");
        alertDialogBuilder.setMessage("We are working on your query. Our executive will call you as soon as possible with the details.");
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();

                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
