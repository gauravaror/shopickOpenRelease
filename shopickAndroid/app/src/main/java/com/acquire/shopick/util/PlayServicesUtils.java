/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.acquire.shopick.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Helper for Google Play services-related operations.
 */
public class PlayServicesUtils {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static boolean checkGooglePlaySevices(final Activity activity) {
        final int googlePlayServicesCheck = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity.getApplicationContext());
        switch (googlePlayServicesCheck) {
            case ConnectionResult.SUCCESS:
                return true;
            case ConnectionResult.SERVICE_DISABLED:
            case ConnectionResult.SERVICE_INVALID:
            case ConnectionResult.SERVICE_MISSING:
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(googlePlayServicesCheck, activity, 0);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                       // activity.finish();
                    }
                });
                dialog.show();
        }
        return false;
    }

}
