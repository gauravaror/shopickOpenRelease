package com.acquire.shopick.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.acquire.shopick.Config;
import com.acquire.shopick.R;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.event.LoadReferralCode;
import com.acquire.shopick.event.RedeemReferralServiceEvent;
import com.acquire.shopick.event.ReferralLoadedEvent;
import com.acquire.shopick.event.StatusRedeemReferralServiceEvent;
import com.acquire.shopick.io.model.Referral;
import com.acquire.shopick.io.model.Status;
import com.acquire.shopick.job.UpdatePhoneNumberJob;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.ImageLoader;
import com.acquire.shopick.util.SnackbarUtil;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaurav on 3/27/16.
 */
public class ReedemReferralActivity extends CoreActivity {
    @Bind(R.id.editText_redeem)
    EditText redeem_edit;

    @Bind(R.id.redeem_referral)
    Button phone_update;

    @Inject
    JobManager jobManager;


    String globalID;
    Long post_id;
    Long product_id;
    boolean post_;
    int user_id;
    String code;

    private Bus eventBus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_redeem_referral);
        super.onCreate(savedInstanceState);

        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyticsManager.sendEvent("RedeemReferral", "Close", "", 0L);
                supportFinishAfterTransition();
            }
        });
        globalID =  getIntent().getStringExtra("globalID");
        post_ = getIntent().getBooleanExtra("post", true);
        if (post_) {
            post_id = getIntent().getLongExtra("post_id", -1L);
        } else {
            product_id = getIntent().getLongExtra("product_id", -1L);
        }

        code = AccountUtils.getShopickReferrer(getApplicationContext());

        Uri data = getIntent().getData();
        String current_code = "";
        if (data != null) {
            current_code = data.getQueryParameter("code");
        }
        if (!TextUtils.isEmpty(code)) {
            SnackbarUtil.showMessage(redeem_edit, "You have already redeemed your referral picks!!");
        }
        redeem_edit.setText(current_code);
    }

    @OnClick(R.id.redeem_referral)
    void onClick(View view) {
        if (redeem_edit.getText().length() > 2) {
            eventBus = BusProvider.getInstance();
            String refcode = redeem_edit.getText().toString();
            eventBus.post(new RedeemReferralServiceEvent(refcode));
        }
    }

    @Subscribe
    public void onStatusRedeemReferralServiceCodeLoaded(StatusRedeemReferralServiceEvent event) {
        Status status = event.getStatus();
        if(status.statusCode == 200) {
            AccountUtils.setShopickReferrer(getApplicationContext(), redeem_edit.getText().toString());
            SnackbarUtil.succesRedeemReferral(findViewById(R.id.editText_redeem));
        } else {
            SnackbarUtil.failureRedeemReferral(findViewById(R.id.editText_redeem), status.message);
        }
    }
}
