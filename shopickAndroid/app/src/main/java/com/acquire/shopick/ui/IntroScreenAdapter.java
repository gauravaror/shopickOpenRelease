package com.acquire.shopick.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.acquire.shopick.R;
import com.acquire.shopick.io.model.Brand;
import com.acquire.shopick.io.model.Intro;
import com.acquire.shopick.util.ImageLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.acquire.shopick.util.LogUtils.LOGV;
import static com.acquire.shopick.util.LogUtils.makeLogTag;

/**
 * Created by gaurav on 9/24/15.
 */
public class IntroScreenAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = makeLogTag(IntroScreenAdapter.class);
    // additional top padding to add to first item of list
    int mContentTopClearance = 0;
    private final Context mContext;
    private ImageLoader mNoPlaceholderImageLoader;
    // list of items served by this adapter
    String filter = "";
    ArrayList<Intro> mItems = new ArrayList<Intro>();

    // observers to notify about changes in the data
    ArrayList<DataSetObserver> mObservers = new ArrayList<DataSetObserver>();


    public IntroScreenAdapter(Context context, FragmentManager mgr) {
        super(mgr);
        mContext = context;
    }


    public void setContentTopClearance(int padding) {
        mContentTopClearance = padding;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Fragment getItem(int position) {
        int pos =  position >= 0 && position < mItems.size() ? (position) : null;
        if (pos == 0) {
            return new IntoScreenFirstFragment();
        } else  if (pos == 1){
            return new IntroScreenSecondFragment();
        } else if (pos == 2) {
            return new IntroScreenThirdFragment();
        } else {
            return new IntroScreenFourthFragment();
        }
    }


    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }


    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }


    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

}
