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
import com.acquire.shopick.util.ImageLoader;

import java.util.ArrayList;

/**
 * Created by gaurav on 7/1/16.
 */

public class HorizontalOfferFeedRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    // additional top padding to add to first item of list
    int mContentTopClearance = 0;
    private final Context mContext;
    private ImageLoader mNoPlaceholderImageLoader;
    private int mDefaultSessionColor;
    private static final int VIEW_TYPE_FIRST = 0;
    private static final int VIEW_TYPE_SECOND = 2;
    private static final int VIEW_TYPE_LAST = 3;
    public boolean showText = true;
    public boolean addShowAll = false;
    public PostCollection showAllPostCollection;



    // list of items served by this adapter
    ArrayList<Post> mItems = new ArrayList<Post>();

    // observers to notify about changes in the data
    ArrayList<DataSetObserver> mObservers = new ArrayList<DataSetObserver>();


    public HorizontalOfferFeedRecyclerAdapter(Context context) {
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
            case VIEW_TYPE_FIRST:
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_horizontal_offer, parent, false);
                return new HorizonatalOfferViewHolder(view, mContext);

            case VIEW_TYPE_LAST:
                View findanything_view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.see, parent, false);
                return new SeeAllAndPOstViewHolder(findanything_view, mContext);
            default:
                View view_ = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_horizontal_offer, parent, false);
                return new HorizonatalOfferViewHolder(view_, mContext);

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_FIRST:
                Post update = mItems.get(position);
                HorizonatalOfferViewHolder feed_holder = (HorizonatalOfferViewHolder)holder;
                feed_holder.bindHorizontalOffer(update, mContext, showText);
                break;
            case VIEW_TYPE_LAST:
                SeeAllAndPOstViewHolder findanything_holder = (SeeAllAndPOstViewHolder)holder;
                findanything_holder.bindHorizontalOffer(showAllPostCollection, mContext);
                break;

        }
    }

    public int getItemViewType(int position) {
        if (position == (mItems.size()-1)  && addShowAll == true) {
            return VIEW_TYPE_LAST;
        } else {
            return VIEW_TYPE_FIRST;
        }
    }



}
