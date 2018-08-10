package com.acquire.shopick.ui;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acquire.shopick.R;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.io.model.Feed;
import com.acquire.shopick.io.model.ProductMini;
import com.acquire.shopick.util.ImageLoader;

import java.util.ArrayList;


import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acquire.shopick.R;
import com.acquire.shopick.io.model.ProductMini;
import com.acquire.shopick.model.UpdatesCollectionItem;
import com.acquire.shopick.util.ImageLoader;
import com.google.common.base.FinalizablePhantomReference;

import java.util.ArrayList;

/**
 * Created by gaurav on 9/13/15.
 */
public class FeedRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    // additional top padding to add to first item of list
    int mContentTopClearance = 0;
    private final Context mContext;
    private ImageLoader mNoPlaceholderImageLoader;
    private int mDefaultSessionColor;
    private static final int VIEW_TYPE_FIRST = 0;
    private static final int VIEW_TYPE_SECOND = 2;





    // list of items served by this adapter
    ArrayList<Post> mItems = new ArrayList<Post>();

    // observers to notify about changes in the data
    ArrayList<DataSetObserver> mObservers = new ArrayList<DataSetObserver>();


    public FeedRecyclerAdapter(Context context) {
        mContext = context;
        mDefaultSessionColor = mContext.getResources().getColor(R.color.theme_accent_1_light);
    }



    public void setContentTopClearance(int padding) {
        mContentTopClearance = padding;
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_FIRST:  View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_feed, parent, false);
                      return new FeedViewHolder(view, mContext);
            case VIEW_TYPE_SECOND: View findanything_view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.findanything_widget, parent, false);
                    return new FindAnythingViewHolder(findanything_view, mContext);
            default:  View view_default = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_feed, parent, false);
                return new FeedViewHolder(view_default, mContext);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_FIRST:
                Post update = mItems.get(position);
                FeedViewHolder feed_holder = (FeedViewHolder)holder;
                feed_holder.bindExploreItem(update, mContext);
                break;
            case VIEW_TYPE_SECOND:
                FindAnythingViewHolder findanything_holder = (FindAnythingViewHolder)holder;
                findanything_holder.bindExploreItem(null, mContext);
                break;

        }
    }

    public int getItemViewType(int position) {
        if (position == 0 ) {
            return VIEW_TYPE_FIRST;
        } else {
            return VIEW_TYPE_FIRST;
        }
    }



}
