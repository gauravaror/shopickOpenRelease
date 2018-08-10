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

package com.acquire.shopick;


import java.util.HashMap;
import java.util.Map;

public class Config {
    // Is this an internal dogfood build?
    public static final boolean IS_DOGFOOD_BUILD = false;

    // Warning messages for dogfood build
    public static final String DOGFOOD_BUILD_WARNING_TITLE = "Test build";
    public static final String DOGFOOD_BUILD_WARNING_TEXT = "This is a test build.";


    // shorthand for some units of time
    public static final long SECOND_MILLIS = 1000;
    public static final long MINUTE_MILLIS = 60 * SECOND_MILLIS;
    public static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;

    // OAuth 2.0 related config
    public static final String APP_NAME = "Shopick";
    public static final String API_KEY = "";

    // Announcements
    public static final String ANNOUNCEMENTS_PLUS_ID = "";

    public static final String CHAT_SERVER_URL = "chat.shopick.co.in";
    // YouTube API config
    public static final String YOUTUBE_API_KEY = "";

    // YouTube share URL
    public static final String YOUTUBE_SHARE_URL_PREFIX = "http://youtu.be/";

    // Live stream captions config
    public static final String LIVESTREAM_CAPTIONS_DARK_THEME_URL_PARAM = "&theme=dark";

    // GCM config
    public static final String GCM_SERVER_PROD_URL = "";
    public static final String GCM_SERVER_URL = "";

    public static final int MAX_POST_RESULTS = 200;

    // the GCM sender ID is the ID of the app in Google Cloud Console
    public static final String GCM_SENDER_ID = "322099374098";

    // The registration api KEY in the gcm server (configured in the GCM
    // server's AuthHelper.java file)
    public static final String GCM_API_KEY = "";

    public static final String VIDEO_LIBRARY_URL_FMT = "https://www.youtube.com/watch?v=%s";

    // Fallback URL to get a youtube video thumbnail in case one is not provided in the data
    // (normally it should, but this is a safety fallback if it doesn't)
    public static final String VIDEO_LIBRARY_FALLBACK_THUMB_URL_FMT =
            "http://img.youtube.com/vi/%s/default.jpg";

    // Link to Google I/O Extended events presented in Explore screen
    public static final String IO_EXTENDED_LINK = "http://www.google.com/events/io/io-extended";

    // 2014-07-25: Time of expiration for experts directory data.
    // Represented as elapsed milliseconds since the epoch.
    public static final long EXPERTS_DIRECTORY_EXPIRATION = 1406214000000L;

    public static final int DEFAULT_SELECTED_TAB = 1;
    /**
     * Check if the experts directory data expired.
     *
     * @return True if the experts directory data expired and should be removed.
     */
    public static boolean hasExpertsDirectoryExpired() {
        return EXPERTS_DIRECTORY_EXPIRATION < System.currentTimeMillis();
    }

    // URL to use for resolving NearbyDevice metadata.
    public static final String METADATA_URL =
            // "http://url-caster.appspot.com/resolve-scan"
            rep("http://example-caster", "example", "url") + "."
                    + rep("example.com", "example", "appspot")
                    + rep("/resolve-link", "link", "scan");

    // Auto sync interval. Shouldn't be too small, or it might cause battery drain.
    public static final long AUTO_SYNC_INTERVAL_AROUND_CONFERENCE = 12 * HOUR_MILLIS;



    // Public data manifest URL
    public static final String PROD_BASE_URL = "https://shopick.co.in/";

    // Public data manifest URL
    public static final String PROD_DATA_MANIFEST_URL = PROD_BASE_URL+ "manifest.json";


    // Manifest URL override for Debug (staging) builds:
    public static final String MANIFEST_URL = PROD_DATA_MANIFEST_URL;


    public static final String BOOTSTRAP_DATA_TIMESTAMP = "Thu, 10 Apr 2014 00:01:03 GMT";

    // Values for the EventPoint feedback API. Sync happens at the same time as schedule sync,
    // and before that values are stored locally in the database.


    private static String rep(String s, String orig, String replacement) {
        return s.replaceAll(orig, replacement);
    }

    //Tags that Let's you select top level category.
    // Known session tags that induce special behaviors
    public interface Tags {

        // the tag category that we use to group sessions together when displaying them
        public static final String SESSION_GROUPING_TAG_CATEGORY = "TYPE";

        // tag categories
        public static final String CATEGORY_ONE = "WOMEN";
        public static final String CATEGORY_TWO = "MEN";
        public static final String CATEGORY_THREE = "KIDS";
        public static final String CATEGORY_FOUR = "LIFESTYLE";

        public static final Map<String, Integer> CATEGORY_DISPLAY_ORDERS
                = new HashMap<String, Integer>();

        public static final String[] EXPLORE_CATEGORIES =
                { CATEGORY_ONE, CATEGORY_TWO, CATEGORY_THREE, CATEGORY_FOUR };

        public static final int[] EXPLORE_CATEGORY_TITLE = {
                R.string.all_one, R.string.all_two, R.string.all_three, R.string.all_four
        };

        public static final String CATEGORY_THEME = "THEME";
        public static final String CATEGORY_TOPIC = "TOPIC";
        public static final String CATEGORY_TYPE = "TYPE";


        public static final String SPECIAL_KEYNOTE = "FLAG_KEYNOTE";

        public static final String[] EXPLORE_CATEGORIES_NON =
                { CATEGORY_THEME, CATEGORY_TOPIC, CATEGORY_TYPE };

        public static final int[] EXPLORE_CATEGORY_ALL_STRING = {
                R.string.all_themes, R.string.all_topics, R.string.all_types
        };

        public static final int[] EXPLORE_CATEGORY_TITLE_NON = {
                R.string.themes, R.string.topics, R.string.types
        };




    }
}
