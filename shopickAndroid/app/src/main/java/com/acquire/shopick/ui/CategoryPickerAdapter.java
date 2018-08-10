package com.acquire.shopick.ui;

/**
 * Created by gaurav on 10/7/15.
 */

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.acquire.shopick.Config;
import com.acquire.shopick.R;
import com.acquire.shopick.dao.Categories;
import com.acquire.shopick.util.ImageLoader;

import java.util.ArrayList;


import java.util.Locale;

/**
 * Created by gaurav on 9/13/15.
 */
public class CategoryPickerAdapter implements ListAdapter {

    // additiPal top padding to add to first item of list
    int mContentTopClearance = 0;
    private final Context mContext;
    private ImageLoader mNoPlaceholderImageLoader;
    private int mDefaultSessionColor;



    // list of items served by this adapter
    ArrayList<Categories> mItems = new ArrayList<Categories>();
    ArrayList<Categories> mItemsCopy = new ArrayList<Categories>();

    // observers to notify about changes in the data
    ArrayList<DataSetObserver> mObservers = new ArrayList<DataSetObserver>();


    public CategoryPickerAdapter(Context context) {
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
        int layoutResId = R.layout.list_item_category;
        view = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(layoutResId, parent, false);
        Categories p = mItems.get(position);
        TextView title = (TextView) view.findViewById(R.id.category_title);
        title.setText(p.getName());
        TextView snippet = (TextView) view.findViewById(R.id.category_snippet);
        
        ImageView photoView = (ImageView) view.findViewById(R.id.category_photo_colored);
        int darkSessionColor = 0;

        if (mNoPlaceholderImageLoader == null) {
            mNoPlaceholderImageLoader = new ImageLoader(mContext,R.drawable.ic_shopping_cart_blue_400_24dp);
        }
        photoView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_shopping_cart_blue_400_24dp));
        if (p.getImage_url() != null) {
            mNoPlaceholderImageLoader.loadImage(Config.PROD_BASE_URL+p.getImage_url(), photoView);
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

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mItems.clear();
        if (charText.length() == 0) {
            mItems.addAll(mItemsCopy);
        }
        else
        {
            for (Categories wp : mItemsCopy)
            {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    mItems.add(wp);
                }
            }
        }
    }
}
