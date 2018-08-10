package com.acquire.shopick.receivers;

/**
 * Created by gaurav on 3/27/16.
 */

import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
import android.text.TextUtils;

import com.acquire.shopick.util.DispatchIntentUtils;
import com.google.android.gms.analytics.CampaignTrackingReceiver;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static com.acquire.shopick.util.LogUtils.LOGD;
import static com.acquire.shopick.util.LogUtils.makeLogTag;

/*
 *  A simple Broadcast Receiver to receive an INSTALL_REFERRER
 *  intent and pass it to other receivers, including
 *  the Google Analytics receiver.
 */
public class InstallReferralReceiver extends BroadcastReceiver {

    private static final String TAG = makeLogTag(InstallReferralReceiver.class);

    @Override
    public void onReceive(Context context, Intent intent) {

        // Pass the intent to other receivers.

        String referrer = intent.getStringExtra("referrer");
        String action = intent.getAction();


        String rawReferrer = intent.getStringExtra("referrer");

        LOGD(TAG, "received broadcast");

        if (rawReferrer != null) {
            LOGD(TAG, "raw: " + rawReferrer);


            try {
                referrer = URLDecoder.decode(rawReferrer, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace(); // This should not happen.
            }

            LOGD(TAG, "decoded: " + referrer);
        }
            LOGD(TAG, "Got the referral");
        if("com.android.vending.INSTALL_REFERRER".equals(action) && !TextUtils.isEmpty(referrer)) {
            LOGD(TAG, "Got the referral");
            String deepLink = parseReferrer(referrer, "deep_link_id");
            LOGD(TAG, "deep link " + deepLink);
            if (!TextUtils.isEmpty(deepLink)) {
                context.startActivity(DispatchIntentUtils.dispatchDeepLinkIntent(context, deepLink));
            }
        }
            // When you're done, pass the intent to the Google Analytics receiver.
        new CampaignTrackingReceiver().onReceive(context, intent);
    }

    private static String parseReferrer(String referrer, String field) {
        String[] array = referrer.split("&");

        for (String string : array) {
            if (string.contains(field)) {
                int index = string.indexOf("=");

                return string.substring(index + 1);
            }
        }
        return null;
    }
}
