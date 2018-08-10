package com.acquire.shopick.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.acquire.shopick.R;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.acquire.shopick.util.LogUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by gaurav on 7/2/16.
 */

public class SettingActivity extends CoreActivity {

    private static final String TAG = LogUtils.makeLogTag(SettingActivity.class);
    @Bind(R.id.button_liked_brands)
    public Button likedBrands;

    @Bind(R.id.button_male)
    public Button maleButton;

    @Bind(R.id.button_female)
    public Button femaleButton;


    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting_layout);
        super.onCreate(savedInstanceState);
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
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        if (AccountUtils.getShowOfferCollectionFor(getApplicationContext()) == 0) {
            femaleButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.material_white));
            maleButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.accentPrimary));
            femaleButton.setTextColor(getApplicationContext().getResources().getColor(R.color.accentPrimary));
            maleButton.setTextColor(getApplicationContext().getResources().getColor(R.color.material_white));
        }
        if (AccountUtils.getShowOfferCollectionFor(getApplicationContext()) == 1) {
            maleButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.material_white));
            femaleButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.accentPrimary));
            maleButton.setTextColor(getApplicationContext().getResources().getColor(R.color.accentPrimary));
            femaleButton.setTextColor(getApplicationContext().getResources().getColor(R.color.material_white));
        }

        if (AccountUtils.getShowOfferCollectionFor(getApplicationContext()) == -1) {
            maleButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.material_white));
            femaleButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.material_white));
            maleButton.setTextColor(getApplicationContext().getResources().getColor(R.color.accentPrimary));
            femaleButton.setTextColor(getApplicationContext().getResources().getColor(R.color.accentPrimary));

        }
    }

    @OnClick(R.id.button_liked_brands)
    public void likeBrands(View view) {
        startActivity(DispatchIntentUtils.dispatchLikedBrandntent(getApplicationContext()));
        AnalyticsManager.sendEvent("SETTINGS","CLICKED","OPEN_LIKE_BRAND");
    }

    @OnClick(R.id.button_male)
    public void buttonMale(View view) {
        AccountUtils.setPrefShowOfferCollectionFor(getApplicationContext(), AccountUtils.getActiveAccountName(getApplicationContext()), 0);
        femaleButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.material_white));
        maleButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.accentPrimary));
        femaleButton.setTextColor(getApplicationContext().getResources().getColor(R.color.accentPrimary));
        maleButton.setTextColor(getApplicationContext().getResources().getColor(R.color.material_white));
        AnalyticsManager.sendEvent("SETTINGS","CLICKED","MALE");

    }

    @OnClick(R.id.button_female)
    public void buttonFemale(View view) {
        AccountUtils.setPrefShowOfferCollectionFor(getApplicationContext(), AccountUtils.getActiveAccountName(getApplicationContext()), 1);
        maleButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.material_white));
        femaleButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.accentPrimary));
        maleButton.setTextColor(getApplicationContext().getResources().getColor(R.color.accentPrimary));
        femaleButton.setTextColor(getApplicationContext().getResources().getColor(R.color.material_white));
        AnalyticsManager.sendEvent("SETTINGS","CLICKED","FEMALE");
    }

}
