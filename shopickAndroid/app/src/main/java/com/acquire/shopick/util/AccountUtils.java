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

import android.accounts.Account;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.android.gms.auth.*;
import com.google.android.gms.common.Scopes;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import android.provider.Settings.Secure;


 import static com.acquire.shopick.util.LogUtils.*;

/**
 * Account and login utilities. This class manages a local shared preferences object
 * that stores which account is currently active, and can store associated information
 * such as Google+ profile info (name, image URL, cover URL) and also the auth token
 * associated with the account.
 */
public class AccountUtils {
    private static final String TAG = makeLogTag(AccountUtils.class);

    private static final String PREF_ACTIVE_ACCOUNT = "chosen_account";
    private static final String PREF_ACTIVE_INSTANCE_ID = "instance_id";
    private static final String PREF_ACTIVE_GCM_TOKEN = "gcm_token_active";
    private static final String PREF_LOGIN_STARTED = "login_started_";
    private static final String PREF_LOGIN_DONE = "login_signup_done_";

    private static final String PREF_SHOW_OFFER_COLLECTION_FOR = "show_offer_collection_for_";


    private static final String PREF_SHOW_ALL_OFFER_ONCE = "show_all_offer_once_";

    // these names are are prefixes; the account is appended to them
    private static final String PREFIX_PREF_AUTH_TOKEN = "auth_token_";
    private static final String PREFIX_PREF_SHOPICK_AUTH_TOKEN = "shopick_auth_token_";
    private static final String PREFIX_PREF_SHOPICK_PROFILE_ID = "shopick_profile_id_";
    private static final String PREFIX_PREF_FACEBOOK_PROFILE_ID = "facebook_profile_id_";
    private static final String PREFIX_PREF_SHOPICK_TEMP_PROFILE_ID = "shopick_temp_profile_id_";
    private static final String PREFIX_PREF_PLUS_PROFILE_ID = "plus_profile_id_";
    private static final String PREFIX_PREF_PLUS_PROFILE_GENDER = "plus_profile_gender_";
    private static final String PREFIX_PREF_PLUS_PROFILE_AGE_MAX = "plus_profile_age_max_";
    private static final String PREFIX_PREF_PLUS_PROFILE_AGE_MIN = "plus_profile_age_min_";
    private static final String PREFIX_PREF_PLUS_NAME = "plus_name_";
    private static final String PREFIX_PREF_PLUS_IMAGE_URL = "plus_image_url_";
    private static final String PREFIX_PREF_PLUS_COVER_URL = "plus_cover_url_";
    private static final String PREFIX_PREF_GCM_KEY = "gcm_key_";
    private static final String PREFIX_PREF_SHOPICK_PHONE_NO = "shopick_phone_number_";
    private static final String PREFIX_PREF_SHOPICK_REDEEM_REFERRAL = "shopick_redeem_referral_";
    private static final String PREFIX_PREF_SHOPICK_MY_PICKS = "shopick_my_picks_";
    private static final String PREFIX_PREF_SHOPICK_LAST_KNOWN_LAT = "shopick_last_known_lat_";
    private static final String PREFIX_PREF_SHOPICK_LAST_KNOWN_LON = "shopick_last_known_lon_";

    private static final String PREFIX_PREF_SHOPICK_MY_PICKS_MONTHLY = "shopick_my_picks_monthly_";


    public static final String AUTH_SCOPES[] = {
            Scopes.PLUS_LOGIN,
            Scopes.DRIVE_APPFOLDER,
            "https://www.googleapis.com/auth/userinfo.email"};

    static final String AUTH_TOKEN_TYPE;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("oauth2:");
        for (String scope : AUTH_SCOPES) {
            sb.append(scope);
            sb.append(" ");
        }
        AUTH_TOKEN_TYPE = sb.toString();
    }

    private static SharedPreferences getSharedPreferences(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean hasActiveAccount(final Context context) {
        return !TextUtils.isEmpty(getActiveAccountName(context));
    }

    public static boolean isLoginEnabled(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getBoolean(PREF_LOGIN_STARTED, false);    }


    public static boolean hasAllOfferShown(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getBoolean(PREF_SHOW_ALL_OFFER_ONCE, false);    }

    public static String getActiveAccountName(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(PREF_ACTIVE_ACCOUNT, null);
    }

    public static Account getActiveAccount(final Context context) {
        String account = getActiveAccountName(context);
        if (account != null) {
            return new Account(account, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        } else {
            return null;
        }
    }

    public static boolean setActiveAccount(final Context context, final String accountName) {
        LOGD(TAG, "Set active account to: " + accountName);
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(PREF_ACTIVE_ACCOUNT, accountName).commit();
        return true;
    }

    public static boolean setLoginEnabled(final Context context) {
        LOGD(TAG, "User Enabled Login, Now try best to login.. ");
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putBoolean(PREF_LOGIN_STARTED, true).commit();
        return true;
    }

    public static boolean setAllOfferShownOnce(final Context context) {
        LOGD(TAG, "User ALl offer shown once.. ");
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putBoolean(PREF_SHOW_ALL_OFFER_ONCE, true).commit();
        return true;
    }


    public static Boolean getLoginDone(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getBoolean(PREF_LOGIN_DONE, false);
    }

    public static boolean setLoginDone(final Context context, final Boolean value) {
        LOGD(TAG, "Set Instance Id to: " + value);
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putBoolean(PREF_LOGIN_DONE, value).commit();

        return true;
    }




    public static String getInstanceId(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(PREF_ACTIVE_INSTANCE_ID, null);
    }





    public static boolean setInstanceId(final Context context, final String instanceId) {
        LOGD(TAG, "Set Instance Id to: " + instanceId);
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(PREF_ACTIVE_INSTANCE_ID, instanceId).commit();
        return true;
    }

    public static String getGCMToken(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(PREF_ACTIVE_GCM_TOKEN, null);
    }




    public static boolean setGCMToken(final Context context, final String gcm_token) {
        LOGD(TAG, "Set Instance Id to: " + gcm_token);
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(PREF_ACTIVE_GCM_TOKEN, gcm_token).commit();
        return true;
    }



    private static String makeAccountSpecificPrefKey(Context ctx, String prefix) {
        return hasActiveAccount(ctx) ? makeAccountSpecificPrefKey(getActiveAccountName(ctx),
                prefix) : null;
    }

    private static String makeAccountSpecificPrefKey(String accountName, String prefix) {
        return prefix + accountName;
    }

    public static String getAuthToken(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return hasActiveAccount(context) ?
                sp.getString(makeAccountSpecificPrefKey(context, PREFIX_PREF_AUTH_TOKEN), null) : null;
    }

    public static String getShopickAuthToken(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return hasActiveAccount(context) ?
                sp.getString(makeAccountSpecificPrefKey(context, PREFIX_PREF_SHOPICK_AUTH_TOKEN), null) : null;
    }

    public static void setAuthToken(final Context context, final String accountName, final String authToken) {
        LOGI(TAG, "Auth token of length "
                + (TextUtils.isEmpty(authToken) ? 0 : authToken.length()) + " for "
                + accountName);
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(makeAccountSpecificPrefKey(accountName, PREFIX_PREF_AUTH_TOKEN),
                authToken).commit();
        LOGV(TAG, "Auth Token: " + authToken);
    }

    public static void setShopickAuthToken(final Context context, final String accountName, final String authToken) {
        LOGI(TAG, "Auth token of length "
                + (TextUtils.isEmpty(authToken) ? 0 : authToken.length()) + " for "
                + accountName);
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(makeAccountSpecificPrefKey(accountName, PREFIX_PREF_SHOPICK_AUTH_TOKEN),
                authToken).commit();

        LOGV(TAG, "Auth Token: " + authToken);
    }

    public static void deleteShopickAuthToken(final Context context, final String accountName) {
        LOGI(TAG, "Delete shopick Auth token.");
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().remove(makeAccountSpecificPrefKey(accountName, PREFIX_PREF_SHOPICK_AUTH_TOKEN)).commit();
    }

    public static void setAuthToken(final Context context, final String authToken) {
        if (hasActiveAccount(context)) {
            setAuthToken(context, getActiveAccountName(context), authToken);
        } else {
            LOGE(TAG, "Can't set auth token because there is no chosen account!");
        }
    }

    static void invalidateAuthToken(final Context context) {
        GoogleAuthUtil.invalidateToken(context, getAuthToken(context));
        setAuthToken(context, null);
    }

    public static void setShopickProfileId(final Context context, final String accountName, final int profileId) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putInt(makeAccountSpecificPrefKey(accountName, PREFIX_PREF_SHOPICK_PROFILE_ID),
                profileId).commit();
    }

    public static void setPrefShowOfferCollectionFor(final Context context, final String accountName, final int gender) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putInt(makeAccountSpecificPrefKey(accountName, PREF_SHOW_OFFER_COLLECTION_FOR),
                gender).commit();
    }

    public static void setFacebookProfileId(final Context context, final String accountName, final String profileId) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(makeAccountSpecificPrefKey(accountName, PREFIX_PREF_FACEBOOK_PROFILE_ID),
                profileId).commit();
    }

    public static void setShopickProfilePhoneno(final Context context, final String accountName, final String phoneno) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(makeAccountSpecificPrefKey(accountName, PREFIX_PREF_SHOPICK_PHONE_NO),
                phoneno).commit();
    }

    public static void setShopickReferrer(final Context context, final String referrer) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(PREFIX_PREF_SHOPICK_REDEEM_REFERRAL, referrer).commit();
    }

    public static void setShopickPicks(final Context context, final Long picks) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putLong(PREFIX_PREF_SHOPICK_MY_PICKS, picks).commit();
    }



    public static void setPrefixPrefShopickLastKnownLat(final Context context, final double picks) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(PREFIX_PREF_SHOPICK_LAST_KNOWN_LAT, String.valueOf(picks)).commit();
    }

    public static String getPrefixPrefShopickLastKnownLat(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return  sp.getString(PREFIX_PREF_SHOPICK_LAST_KNOWN_LAT, "");
    }


    public static void setPrefixPrefShopickLastKnownLon(final Context context, final double picks) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(PREFIX_PREF_SHOPICK_LAST_KNOWN_LON, String.valueOf(picks)).commit();
    }

    public static String getPrefixPrefShopickLastKnownLon(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return  sp.getString(PREFIX_PREF_SHOPICK_LAST_KNOWN_LON, "");
    }


    public static void setShopickMyPicksMonthly(final Context context, final Long picks) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putLong(PREFIX_PREF_SHOPICK_MY_PICKS_MONTHLY, picks).commit();
    }

    public static void setShopickTempProfileId(final Context context, final String accountName, final int profileId) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putInt(makeAccountSpecificPrefKey(accountName, PREFIX_PREF_SHOPICK_TEMP_PROFILE_ID),
                profileId).commit();
    }

    public static int getShopickProfileId(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return hasActiveAccount(context) ? sp.getInt(makeAccountSpecificPrefKey(context,
                PREFIX_PREF_SHOPICK_PROFILE_ID), -1) : -1;
    }

    public static int getShowOfferCollectionFor(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return hasActiveAccount(context) ? sp.getInt(makeAccountSpecificPrefKey(context,
                PREF_SHOW_OFFER_COLLECTION_FOR), -1) : -1;
    }

    public static String getFacebookProfileId(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return hasActiveAccount(context) ? sp.getString(makeAccountSpecificPrefKey(context,
                PREFIX_PREF_FACEBOOK_PROFILE_ID), "") : "";
    }

    public static String getShopickProfilePhoneno(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return hasActiveAccount(context) ? sp.getString(makeAccountSpecificPrefKey(context,
                PREFIX_PREF_SHOPICK_PHONE_NO), "") : "";
    }

    public static String getShopickReferrer(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return  sp.getString( PREFIX_PREF_SHOPICK_REDEEM_REFERRAL, "");
    }

    public static Long getShopickMyPicks(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return  sp.getLong(PREFIX_PREF_SHOPICK_MY_PICKS, 10L);
    }

    public static Long getShopickMyPicksMonthly(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return  sp.getLong(PREFIX_PREF_SHOPICK_MY_PICKS_MONTHLY, 10);
    }

    public static int getShopickTempProfileId(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return hasActiveAccount(context) ? sp.getInt(makeAccountSpecificPrefKey(context,
                PREFIX_PREF_SHOPICK_TEMP_PROFILE_ID), -1) : -1;
    }


    public static void setPlusProfileId(final Context context, final String accountName, final String profileId) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(makeAccountSpecificPrefKey(accountName, PREFIX_PREF_PLUS_PROFILE_ID),
                profileId).commit();
    }

    public static void setPlusGender(final Context context, final String accountName, final int gender) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putInt(makeAccountSpecificPrefKey(accountName, PREFIX_PREF_PLUS_PROFILE_GENDER),
                gender).commit();
    }

    public static void setPlusAgeMAX(final Context context, final String accountName, final int age) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putInt(makeAccountSpecificPrefKey(accountName, PREFIX_PREF_PLUS_PROFILE_AGE_MAX),
                age).commit();
    }

    public static void setPlusAgeMIN(final Context context, final String accountName, final int age) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putInt(makeAccountSpecificPrefKey(accountName, PREFIX_PREF_PLUS_PROFILE_AGE_MIN),
                age).commit();
    }

    public static String getPlusProfileId(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return hasActiveAccount(context) ? sp.getString(makeAccountSpecificPrefKey(context,
                PREFIX_PREF_PLUS_PROFILE_ID), null) : null;
    }

    public static boolean hasPlusInfo(final Context context, final String accountName) {
        SharedPreferences sp = getSharedPreferences(context);
        return !TextUtils.isEmpty(sp.getString(makeAccountSpecificPrefKey(accountName,
                PREFIX_PREF_PLUS_PROFILE_ID), null));
    }

    public static boolean hasToken(final Context context, final String accountName) {
        SharedPreferences sp = getSharedPreferences(context);
        return !TextUtils.isEmpty(sp.getString(makeAccountSpecificPrefKey(accountName,
                PREFIX_PREF_AUTH_TOKEN), null));
    }

    public static boolean hasShopickToken(final Context context, final String accountName) {
        SharedPreferences sp = getSharedPreferences(context);
        return !TextUtils.isEmpty(sp.getString(makeAccountSpecificPrefKey(accountName,
                PREFIX_PREF_SHOPICK_AUTH_TOKEN), null));
    }

    public static void setPlusName(final Context context, final String accountName, final String name) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(makeAccountSpecificPrefKey(accountName, PREFIX_PREF_PLUS_NAME),
                name).commit();
    }

    public static String getPlusName(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return hasActiveAccount(context) ? sp.getString(makeAccountSpecificPrefKey(context,
                PREFIX_PREF_PLUS_NAME), null) : null;
    }

    public static void setPlusImageUrl(final Context context, final String accountName, final String imageUrl) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(makeAccountSpecificPrefKey(accountName, PREFIX_PREF_PLUS_IMAGE_URL),
                imageUrl).commit();
    }

    public static String getPlusImageUrl(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return hasActiveAccount(context) ? sp.getString(makeAccountSpecificPrefKey(context,
                PREFIX_PREF_PLUS_IMAGE_URL), null) : null;
    }

    public static String getPlusImageUrl(final Context context, final String accountName) {
        SharedPreferences sp = getSharedPreferences(context);
        return hasActiveAccount(context) ? sp.getString(makeAccountSpecificPrefKey(accountName,
                PREFIX_PREF_PLUS_IMAGE_URL), null) : null;
    }


    public static int getPlusGender(final Context context, final String accountName) {
        SharedPreferences sp = getSharedPreferences(context);
        return hasActiveAccount(context) ? sp.getInt(makeAccountSpecificPrefKey(accountName,
                PREFIX_PREF_PLUS_PROFILE_GENDER), -1) : -1;
    }

    public static int getPlusAgeMax(final Context context, final String accountName) {
        SharedPreferences sp = getSharedPreferences(context);
        return hasActiveAccount(context) ? sp.getInt(makeAccountSpecificPrefKey(accountName,
                PREFIX_PREF_PLUS_PROFILE_AGE_MIN), -1) : -1;
    }

    public static int getPlusAgeMin(final Context context, final String accountName) {
        SharedPreferences sp = getSharedPreferences(context);
        return hasActiveAccount(context) ? sp.getInt(makeAccountSpecificPrefKey(accountName,
                PREFIX_PREF_PLUS_PROFILE_AGE_MIN), -1) : -1;
    }

    public static void setPlusCoverUrl(final Context context, final String accountName, String coverPhotoUrl) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(makeAccountSpecificPrefKey(accountName, PREFIX_PREF_PLUS_COVER_URL),
                coverPhotoUrl).commit();
    }

    public static String getPlusCoverUrl(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return hasActiveAccount(context) ? sp.getString(makeAccountSpecificPrefKey(context,
                PREFIX_PREF_PLUS_COVER_URL), null) : null;
    }

    static void tryAuthenticateWithErrorNotification(Context context, String syncAuthority) {
        try {
            String accountName = getActiveAccountName(context);
            if (accountName != null) {
                LOGI(TAG, "Requesting new auth token (with notification)");
                final String token = GoogleAuthUtil.getTokenWithNotification(context, accountName, AUTH_TOKEN_TYPE,
                        null, syncAuthority, null);
                setAuthToken(context, token);
            } else {
                LOGE(TAG, "Can't try authentication because no account is chosen.");
            }

        } catch (UserRecoverableNotifiedException e) {
            // Notification has already been pushed.
            LOGW(TAG, "User recoverable exception. Check notification.", e);
        } catch (GoogleAuthException e) {
            // This is likely unrecoverable.
            LOGE(TAG, "Unrecoverable authentication exception: " + e.getMessage(), e);
        } catch (IOException e) {
            LOGE(TAG, "transient error encountered: " + e.getMessage());
        }
    }

    public static void setGcmKey(final Context context, final String accountName, final String gcmKey) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(makeAccountSpecificPrefKey(accountName, PREFIX_PREF_GCM_KEY),
                gcmKey).commit();
        LOGD(TAG, "GCM key of account " + accountName + " set to: " + sanitizeGcmKey(gcmKey));
    }

    public static String getGcmKey(final Context context, final String accountName) {
        SharedPreferences sp = getSharedPreferences(context);
        String gcmKey = sp.getString(makeAccountSpecificPrefKey(accountName,
                PREFIX_PREF_GCM_KEY), null);

        // if there is no current GCM key, generate a new random one
        if (TextUtils.isEmpty(gcmKey)) {
            gcmKey = UUID.randomUUID().toString();
            LOGD(TAG, "No GCM key on account " + accountName + ". Generating random one: "
                    + sanitizeGcmKey(gcmKey));
            setGcmKey(context, accountName, gcmKey);
        }

        return gcmKey;
    }

    public static String sanitizeGcmKey(String key) {
        if (key == null) {
            return "(null)";
        } else if (key.length() > 8) {
            return key.substring(0, 4) + "........" + key.substring(key.length() - 4);
        } else {
            return "........";
        }
    }


    public static HashMap<String,String> getRequestMap(Context mContext) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("format", "json");
        options.put("nojsoncallback", "1");
        if(!TextUtils.isEmpty(getActiveAccountName(mContext)) && !TextUtils.isEmpty(getShopickAuthToken(mContext))) {
            options.put("user_email",getActiveAccountName(mContext));
            options.put("user_token", getShopickAuthToken(mContext));
        } else {
            options.put("user_email", "dummy@gmail.com");
            options.put("user_token", "CzwSm5ZQ94xqqCS_StTc");

        }
        options.put("last_known_lat",getPrefixPrefShopickLastKnownLat(mContext));
        options.put("last_known_lon",getPrefixPrefShopickLastKnownLon(mContext));
        options.put("default_email", getActiveAccountName(mContext));
        String android_id = Secure.getString(mContext.getContentResolver(),
                Secure.ANDROID_ID);

        options.put("uniq_device_id", android_id );

        if (getShopickProfileId(mContext) != -1 ) {
         options.put("user_id",String.valueOf(getShopickProfileId(mContext)));
        }
        if (getShopickTempProfileId(mContext) != -1 ) {
            options.put("temp_user_id",String.valueOf(getShopickTempProfileId(mContext)));
        }
        options.put("show_offer_collection_for", String.valueOf(getShowOfferCollectionFor(mContext)));
        options.put("distance","5");
        options.put("instanceID", getInstanceId(mContext));

        return options;
    }
}
