package com.acquire.shopick.ui;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.acquire.shopick.Config;
import com.acquire.shopick.R;
import com.acquire.shopick.model.PickCollectionItem;
import com.acquire.shopick.model.UpdatesCollectionItem;
import com.acquire.shopick.util.ImageLoader;
import com.acquire.shopick.util.UIUtils;

import java.util.ArrayList;

/**
 * Created by gaurav on 9/13/15.
 */
public class ExploreItemsAdapter implements ListAdapter {

    // additional top padding to add to first item of list
    int mContentTopClearance = 0;
    private final Context mContext;
    private ImageLoader mNoPlaceholderImageLoader;
    private int mDefaultSessionColor;



    // list of items served by this adapter
    ArrayList<UpdatesCollectionItem> mItems = new ArrayList<UpdatesCollectionItem>();

    // observers to notify about changes in the data
    ArrayList<DataSetObserver> mObservers = new ArrayList<DataSetObserver>();


    public ExploreItemsAdapter(Context context) {
        mContext = context;
        mDefaultSessionColor = mContext.getResources().getColor(R.color.theme_accent_1_light);


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
        UpdatesCollectionItem p = mItems.get(position);
        TextView title = (TextView) view.findViewById(R.id.session_title);
        title.setText(p.getTitle());
        TextView snippet = (TextView) view.findViewById(R.id.session_snippet);
        snippet.setText(p.getDescription());
        ImageView photoView = (ImageView) view.findViewById(R.id.session_photo_colored);
        int darkSessionColor = 0;

        darkSessionColor = UIUtils.scaleSessionColorToDefaultBG(mDefaultSessionColor);

        if (photoView != null) {

            //photoView.setColorFilter( UIUtils.makeSessionImageScrimColorFilter(darkSessionColor));
        } else {
            photoView = (ImageView) view.findViewById(R.id.session_photo);
        }
        ViewCompat.setTransitionName(photoView, "photo_" );
        if (mNoPlaceholderImageLoader == null) {
            mNoPlaceholderImageLoader = new ImageLoader(mContext,R.drawable.placeholder);
        }
        if (p.getPhotoUrl() != null) {
            mNoPlaceholderImageLoader.loadImage(Config.PROD_BASE_URL +p.getPhotoUrl(), photoView);
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
