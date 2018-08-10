package com.acquire.shopick.ui;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.acquire.shopick.R;
import com.acquire.shopick.dao.BrandUpdates;
import com.acquire.shopick.io.model.Updates;
import com.acquire.shopick.model.UpdatesCollectionItem;
import com.acquire.shopick.util.ImageLoader;
import com.acquire.shopick.util.UIUtils;
import java.util.ArrayList;

/**
 * Created by gaurav on 9/13/15.
 */
public class ExploreItemsRecyclerAdapter  extends RecyclerView.Adapter<ExploreViewHandler>  {

    // additional top padding to add to first item of list
    int mContentTopClearance = 0;
    private final Context mContext;
    private ImageLoader mNoPlaceholderImageLoader;
    private int mDefaultSessionColor;
    public boolean showBrandLabel = true;



    // list of items served by this adapter
    ArrayList<BrandUpdates> mItems = new ArrayList<BrandUpdates>();

    // observers to notify about changes in the data
    ArrayList<DataSetObserver> mObservers = new ArrayList<DataSetObserver>();


    public ExploreItemsRecyclerAdapter(Context context) {
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
    public ExploreViewHandler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_explore, parent, false);
        return new ExploreViewHandler(view, mContext);
    }

    @Override
    public void onBindViewHolder(ExploreViewHandler holder, int position) {
        BrandUpdates update = mItems.get(position);
        holder.bindExploreItem(update, mContext, showBrandLabel);

    }

    public int getItemViewType(int position) {
        return 0;
    }



}
