package com.acquire.shopick.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.acquire.shopick.R;
import com.cocosw.bottomsheet.BottomSheet;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ShareEvent;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;

import java.net.URI;
import java.util.ArrayList;

/**
 * Created by gaurav on 12/28/15.
 */
public class ShareDialogUtils {



    public static void shareProductDialog(final Context activityContext, final String title, final String globalID, final Uri uri, final View view) {
        Answers.getInstance().logShare(new ShareEvent()
                .putMethod("shareDialog")
                .putContentName(title)
                .putContentType("product")
                .putContentId(String.valueOf(globalID)));
        Bundle parameters = new Bundle();
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "product");
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, globalID);
        AppEventsLogger logger = AppEventsLogger.newLogger(activityContext);
        logger.logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT);



        Intent intent = DispatchIntentUtils.dispatchShareProductIntent(title, globalID, uri);
        shareDialog(activityContext, view, intent );

    }

    public static void shareExploreDialog(final Context activityContext, final String title, final String globalID, final Uri uri, final View view) {
        Answers.getInstance().logShare(new ShareEvent()
                .putMethod("shareDialog")
                .putContentName(title)
                .putContentType("Explore")
                .putContentId(String.valueOf(globalID)));
        Bundle parameters = new Bundle();
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "explore");
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, globalID);
        AppEventsLogger logger = AppEventsLogger.newLogger(activityContext);
        logger.logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT);

        Intent intent = DispatchIntentUtils.dispatchShareBrandUpdateIntent(title, globalID, uri);
        shareDialog(activityContext, view, intent);

    }


    public static void sharePresentationDialog(final Context activityContext, final String title, final String globalID, final Uri uri, final View view) {
        Answers.getInstance().logShare(new ShareEvent()
                .putMethod("shareDialog")
                .putContentName(title)
                .putContentType("Presentation")
                .putContentId(String.valueOf(globalID)));
        Bundle parameters = new Bundle();
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "presentation");
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, globalID);
        AppEventsLogger logger = AppEventsLogger.newLogger(activityContext);
        logger.logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT);


        Intent intent = DispatchIntentUtils.dispatchSharePresentationIntent(title, globalID, uri);
        shareDialog(activityContext, view, intent);

    }

    public static void shareAllOfferDialog(final Context activityContext, final String title, final String intentUrl, final Uri uri, final View view) {
        Answers.getInstance().logShare(new ShareEvent()
                .putMethod("shareDialog")
                .putContentName(title)
                .putContentType("AllOffer"));
        Bundle parameters = new Bundle();
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "allofffer");
        AppEventsLogger logger = AppEventsLogger.newLogger(activityContext);
        logger.logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT);


        Intent intent = DispatchIntentUtils.dispatchShareAllOfferIntent(title, intentUrl, uri);
        shareDialog(activityContext, view, intent);

    }

    public static void sharePostDialog(final Context activityContext, final String title, final String globalID, final String desc, final Uri uri, final View view) {
        Answers.getInstance().logShare(new ShareEvent()
                .putMethod("shareDialog")
                .putContentName(title)
                .putContentType("Post")
                .putContentId(String.valueOf(globalID)));

        Bundle parameters = new Bundle();
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "post");
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, globalID);
        AppEventsLogger logger = AppEventsLogger.newLogger(activityContext);
        logger.logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT);




        Intent intent = DispatchIntentUtils.dispatchSharePostIntent(title, globalID, desc, uri);
        shareDialog(activityContext, view, intent );

    }

    public static void sharePostCollectionDialog(final Context activityContext, final String title, final String globalID, final String desc, final Uri uri, final View view) {
        Answers.getInstance().logShare(new ShareEvent()
                .putMethod("shareDialog")
                .putContentName(title)
                .putContentType("PostCollection")
                .putContentId(String.valueOf(globalID)));

        Bundle parameters = new Bundle();
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "post_collection");
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, globalID);
        AppEventsLogger logger = AppEventsLogger.newLogger(activityContext);
        logger.logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT);




        Intent intent = DispatchIntentUtils.dispatchSharePostIntent(title, globalID, desc, uri);
        shareDialog(activityContext, view, intent );

    }


    public static void shareReferAndWinDialog(final Context activityContext, final String referralCode, final View view) {
        Answers.getInstance().logShare(new ShareEvent()
                .putMethod("shareDialog")
                .putContentName(referralCode)
                .putContentType("REFERANDWIN")
                .putContentId(String.valueOf(referralCode)));

        Bundle parameters = new Bundle();
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "shareReferAndWin");
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, referralCode);
        AppEventsLogger logger = AppEventsLogger.newLogger(activityContext);
        logger.logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT);
        Intent intent = DispatchIntentUtils.dispatchShareAppIntent(referralCode);
        shareDialog(activityContext, view, intent );

    }


    public static void shareDialog(final Context activityContext, final View view, final Intent old_intent ) {
        BottomSheet.Builder bottomSheet =   new BottomSheet.Builder((Activity)activityContext).title("Share ...").sheet(R.menu.menu_share).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String shareName = "";
                Intent intent = (Intent) old_intent.clone();
                switch (which) {
                    case R.id.whatsapp_share:
                        intent.setPackage("com.whatsapp");
                        shareName = "Whatsapp";
                        break;
                    case R.id.facebook_share:
                        intent.setPackage("com.facebook.katana");
                        shareName = "Facebook";
                        break;
                    case R.id.twitter_share:
                        intent.setPackage("com.twitter.android");
                        shareName = "Twitter";
                        break;
                    case R.id.message_share:
                        intent.setPackage("com.google.android.talk");
                        shareName = "Hangout";
                        break;
                    case R.id.instagram_share:
                        intent.setPackage("com.instagram.android");
                        shareName = "Instagram";
                        break;
                    case R.id.pinterest_share:
                        intent.setPackage("com.pinterest");
                        shareName = "Pinterest";
                        break;
                    case R.id.otherapps_share:
                        break;

                }
                try {
                    activityContext.startActivity(intent);
                } catch (ActivityNotFoundException noactivity) {
                    SnackbarUtil.viewApplicationNotInstalled(view, shareName);
                }

            }
        });
        bottomSheet.show();
    }



}
