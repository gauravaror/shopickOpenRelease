package com.acquire.shopick.util;

import android.content.Context;
import android.os.Bundle;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.SearchEvent;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by gaurav on 12/29/15.
 */
public class AnalyticsEvent {

    public static void contentEvent(Context activityContext, String id, String type) {
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentType(type)
                .putContentId(id));
        Bundle parameters = new Bundle();
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, type);
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, id);
        AppEventsLogger logger = AppEventsLogger.newLogger(activityContext);
        logger.logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT, parameters);


    }

    public static void postEventStarted(Context activityContext, int id, int type) {
        Answers.getInstance().logCustom(new CustomEvent("Posting Started Fab")
                .putCustomAttribute("user_id", id)
                .putCustomAttribute("event_type", type));
        Bundle parameters = new Bundle();
        parameters.putInt(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, type);
        parameters.putInt(AppEventsConstants.EVENT_PARAM_CONTENT_ID, id);
        AppEventsLogger logger = AppEventsLogger.newLogger(activityContext);
        logger.logEvent(AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT, parameters);


    }


    public static void postEvenPosted(Context activityContext, int id, int type, Long store_id, Long category_id, int img) {
        Answers.getInstance().logCustom(new CustomEvent("Posted Event")
                .putCustomAttribute("user_id", id)
                .putCustomAttribute("event_type", type)
                .putCustomAttribute("store_id", store_id)
                .putCustomAttribute("category_id", category_id)
                .putCustomAttribute("img_added", img));

        Bundle parameters = new Bundle();
        parameters.putInt(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, type);
        parameters.putInt(AppEventsConstants.EVENT_PARAM_CONTENT_ID, id);
        AppEventsLogger logger = AppEventsLogger.newLogger(activityContext);

        logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_PAYMENT_INFO, parameters);


    }

    public static void searchEventStarted(Context activityContext, String  query) {
        Answers.getInstance().logSearch(new SearchEvent()
                .putQuery(query));
        Bundle parameters = new Bundle();
        parameters.putString(AppEventsConstants.EVENT_PARAM_SEARCH_STRING, query);
        AppEventsLogger logger = AppEventsLogger.newLogger(activityContext);
        logger.logEvent(AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT, parameters);


    }

}
