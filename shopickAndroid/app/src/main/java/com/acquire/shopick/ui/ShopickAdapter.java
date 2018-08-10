package com.acquire.shopick.ui;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.acquire.shopick.R;
import com.acquire.shopick.model.PickCollectionItem;
import com.acquire.shopick.util.ImageLoader;

import java.util.ArrayList;

/**
 * Created by gaurav on 9/13/15.
 */
public class ShopickAdapter implements ListAdapter {

    // additional top padding to add to first item of list
    int mContentTopClearance = 0;
    private final Context mContext;
    private ImageLoader mNoPlaceholderImageLoader;

    // list of items served by this adapter
    ArrayList<PickCollectionItem> mItems = new ArrayList<PickCollectionItem>();

    // observers to notify about changes in the data
    ArrayList<DataSetObserver> mObservers = new ArrayList<DataSetObserver>();


    public ShopickAdapter(Context context) {
        mContext = context;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    public void setContentTopClearance(int padding) {
        mContentTopClearance = padding;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return position >= 0 && position < mItems.size() ? mItems.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Resources res = mContext.getResources();
        int layoutResId = R.layout.list_item_explore;
        view = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(layoutResId, parent, false);
        PickCollectionItem p = mItems.get(position);
        TextView title = (TextView) view.findViewById(R.id.session_title);
        title.setText(p.getTitle());
        TextView snippet = (TextView) view.findViewById(R.id.session_snippet);
        snippet.setText(p.getDescription());
        ImageView imageView = (ImageView) view.findViewById(R.id.session_photo);
        if (mNoPlaceholderImageLoader == null) {
            mNoPlaceholderImageLoader = new ImageLoader(mContext,R.drawable.placeholder);
        }
        if (p.getPhotoUrl() != null) {
            mNoPlaceholderImageLoader.loadImage(p.getPhotoUrl(), imageView);
        }

        return view;
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
    public boolean isEmpty() {
        return mItems.isEmpty();
    }
    @Override
    public int getViewTypeCount() {
        return 3;
    }

}
