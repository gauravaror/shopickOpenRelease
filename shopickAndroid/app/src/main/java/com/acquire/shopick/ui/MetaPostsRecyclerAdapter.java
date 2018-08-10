package com.acquire.shopick.ui;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acquire.shopick.R;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.dao.PostCollection;
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
public class MetaPostsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    // additional top padding to add to first item of list
    int mContentTopClearance = 0;
    private final Context mContext;



    // list of items served by this adapter
    ArrayList<PostCollection> mItems = new ArrayList<PostCollection>();

    // observers to notify about changes in the data
    ArrayList<DataSetObserver> mObservers = new ArrayList<DataSetObserver>();


    public MetaPostsRecyclerAdapter(Context context) {
        mContext = context;
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
     View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.generic_meta_post_item, parent, false);
                return new MetaPostsViewHolder(view, mContext);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PostCollection update = mItems.get(position);
        MetaPostsViewHolder meta_posts_holder = (MetaPostsViewHolder)holder;
        meta_posts_holder.bindMetaPostsItem(update, mContext);
    }


}
