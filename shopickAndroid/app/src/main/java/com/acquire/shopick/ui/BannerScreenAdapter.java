package com.acquire.shopick.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.database.DataSetObserver;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.acquire.shopick.io.model.Banner;
import com.acquire.shopick.io.model.Intro;
import com.acquire.shopick.util.ImageLoader;

import java.util.ArrayList;

import static com.acquire.shopick.util.LogUtils.makeLogTag;

/**
 * Created by gaurav on 9/24/15.
 */
public class BannerScreenAdapter extends FragmentStatePagerAdapter  {

    private static final String TAG = makeLogTag(BannerScreenAdapter.class);
    // additional top padding to add to first item of list
    int mContentTopClearance = 0;
    private final Context mContext;
    private ImageLoader mNoPlaceholderImageLoader;
    // list of items served by this adapter
    String filter = "";
    ArrayList<Banner> mItems = new ArrayList<Banner>();

    // observers to notify about changes in the data
    ArrayList<DataSetObserver> mObservers = new ArrayList<DataSetObserver>();


    public BannerScreenAdapter(Context context, FragmentManager mgr) {
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
        return BannerScreen.newInstance(pos, mItems.get(position),filter);
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
