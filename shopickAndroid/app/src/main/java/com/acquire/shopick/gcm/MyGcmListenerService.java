package com.acquire.shopick.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.acquire.shopick.Config;
import com.acquire.shopick.R;
import com.acquire.shopick.ui.ChoiceBrowserActivity;
import com.acquire.shopick.ui.Shopick;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.google.android.gms.gcm.GcmListenerService;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.acquire.shopick.util.LogUtils.LOGD;
import static com.acquire.shopick.util.LogUtils.LOGE;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        String title = data.getString("title");
        String image = data.getString("image");
        String largeIcon = data.getString("largeIcon");
        String intentUrl = data.getString("intentUrl");

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "Image: " + image);
        Log.d(TAG, "Large Icon: " + largeIcon);
        Log.d(TAG, "Intent Url: " + largeIcon);


        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }



        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(title, message, largeIcon,getBitmapFromURL(image),getBitmapFromURL(largeIcon),intentUrl,true);
        // [END_EXCLUDE]
    }
    // [END receive_message]
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(Config.PROD_BASE_URL+strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String title, String message,String imageUrl, Bitmap image, Bitmap  largeIcon, String intentUrl, boolean post) {
        Intent intent;
        if (!TextUtils.isEmpty(intentUrl)) {
            intent = DispatchIntentUtils.dispatchDeepLinkIntent(getApplicationContext(), intentUrl);
        } else {
            intent = new Intent(this, ChoiceBrowserActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder;
        if (largeIcon == null) {
            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_stat_name2)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentIntent(pendingIntent);

        } else {
            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_stat_name2)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setLargeIcon(largeIcon)
                    .setSound(defaultSoundUri)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(image)
                            .bigLargeIcon(largeIcon)
                            .setBigContentTitle(title)
                            .setSummaryText(message))
                    .setContentIntent(pendingIntent);
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}