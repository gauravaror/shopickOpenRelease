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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;


import com.acquire.shopick.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.acquire.shopick.util.LogUtils.LOGD;
import static com.acquire.shopick.util.LogUtils.makeLogTag;

public class ImageLoader {
    private static final String TAG = makeLogTag(ImageLoader.class);


    private int mPlaceHolderResId = -1;

    private Context mContext;
    /**
     * Construct a standard ImageLoader object.
     */
    public ImageLoader(Context context) {
        mContext = context;
    }

    /**
     * Construct an ImageLoader with a default placeholder drawable.
     */
    public ImageLoader(Context context, int placeHolderResId) {
        this(context);
        mContext = context;
        mPlaceHolderResId = placeHolderResId;
    }


    /**
     * Load an image from a url into an ImageView using the given placeholder drawable.
     *
     * @param url The web URL of an image.
     * @param imageView The target ImageView to load the image into.
     * @param placholderOverride A placeholder to use in place of the default placholder.
     */
    public void loadImage(String url, ImageView imageView,
            Drawable placholderOverride) {
        Picasso.with(mContext).load(url).placeholder(placholderOverride).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView);
        //loadImage(url, imageView, requestListener, placholderOverride, false /*crop*/);
    }


    /**
     * Load an image from a url into the given image view using the default placeholder if
     * available.
     * @param url The web URL of an image.
     * @param imageView The target ImageView to load the image into.
     */
    public void loadImage(String url, ImageView imageView) {
        Picasso.with(mContext).load(url).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView);
        //loadImage(url, imageView, false /*crop*/);
    }

    /**
     * Load an image from a url into an ImageView using the default placeholder
     * drawable if available.
     * @param url The web URL of an image.
     * @param imageView The target ImageView to load the image into.
     * @param crop True to apply a center crop to the image.
     */
    public void loadImage(String url, ImageView imageView, boolean crop) {
        if (crop) {
            Picasso.with(mContext).load(url).centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView);
        } else {
            Picasso.with(mContext).load(url).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView);
        }
        //loadImage(url, imageView, null, null, crop);
    }

}
