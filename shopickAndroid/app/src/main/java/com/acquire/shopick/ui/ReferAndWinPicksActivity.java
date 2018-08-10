package com.acquire.shopick.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.acquire.shopick.Config;
import com.acquire.shopick.R;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.event.LoadReferralCode;
import com.acquire.shopick.event.ReferralLoadedEvent;
import com.acquire.shopick.event.StoredBrandUpdatesLoadedEvent;
import com.acquire.shopick.io.model.Referral;
import com.acquire.shopick.job.UpdatePhoneNumberJob;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.acquire.shopick.util.ImageLoader;
import com.acquire.shopick.util.ShareDialogUtils;
import com.acquire.shopick.util.SnackbarUtil;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaurav on 3/28/16.
 */
public class ReferAndWinPicksActivity extends CoreActivity {

    @Bind(R.id.text_referral_code)
    TextView referralCode;

    @Bind(R.id.referral_rules_text)
    TextView referralRules;

    @Bind(R.id.send_to_promo)
    Button redeemCode;


    @Bind(R.id.share_code_referral)
    Button shareCode;

    @Bind(R.id.referral_image)
    ImageView imageView;

    private ImageLoader  mNoPlaceholderImageLoader;


    @Inject
    JobManager jobManager;


    String referralCode_ = "Code";

    Bus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_refer_and_win_picks);
        super.onCreate(savedInstanceState);
        eventBus = BusProvider.getInstance();
        eventBus.post(new LoadReferralCode());
        referralCode.setClickable(true);
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyticsManager.sendEvent("ReferAndWinPicks", "Close", "", 0L);
                supportFinishAfterTransition();
            }
        });
        shareCode.setTag(0);
        redeemCode.setTag(1);

    }

    @OnClick({R.id.share_code_referral, R.id.text_referral_code, R.id.send_to_promo})
    void onClick(View view) {
        if (view instanceof Button) {
            if ((int)view.getTag() == 0) {
                ShareDialogUtils.shareReferAndWinDialog(this, referralCode_, view);
            } else {
                startActivity(DispatchIntentUtils.dispatchRedeemIntent(this));
            }
        } else if (view instanceof  TextView) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("referral_code", referralCode_);
            clipboard.setPrimaryClip(clip);
            SnackbarUtil.referralCodeCopied(view);
        }
        //startActivity(DispatchIntentUtils.dispatchShareAppIntent(AccountUtils.getShopickTempProfileId(getApplicationContext())));
    }


    @Subscribe
    public void onReferralCodeLoaded(ReferralLoadedEvent event) {
        Referral ref = event.getReferral();
        if (ref != null) {
            referralCode_ = ref.referralCode;
            referralCode.setText(referralCode_);
            referralRules.setText(ref.rules);
            if (mNoPlaceholderImageLoader == null) {
                mNoPlaceholderImageLoader = new ImageLoader(getApplicationContext(), R.drawable.placeholder);
            }
            if (ref.imageUrl != null) {
                mNoPlaceholderImageLoader.loadImage(Config.PROD_BASE_URL + ref.imageUrl, imageView);
            }
        }

    }
}
