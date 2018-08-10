package com.acquire.shopick.ui;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acquire.shopick.R;
import com.acquire.shopick.io.model.Product;
import com.acquire.shopick.io.model.ProductMini;
import com.acquire.shopick.model.UpdatesCollectionItem;
import com.acquire.shopick.util.ImageLoader;

import java.util.ArrayList;

/**
 * Created by gaurav on 9/13/15.
 */
public class ProductCollectionRecyclerAdapter extends RecyclerView.Adapter<ProductCollectionViewHandler>  {

    // additional top padding to add to first item of list
    int mContentTopClearance = 0;
    private final Context mContext;
    private ImageLoader mNoPlaceholderImageLoader;
    private int mDefaultSessionColor;



    // list of items served by this adapter
    ArrayList<Product> mItems = new ArrayList<Product>();

    // observers to notify about changes in the data
    ArrayList<DataSetObserver> mObservers = new ArrayList<DataSetObserver>();


    public ProductCollectionRecyclerAdapter(Context context) {
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
    public ProductCollectionViewHandler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_collection_two, parent, false);
        return new ProductCollectionViewHandler(view, mContext);
    }

    @Override
    public void onBindViewHolder(ProductCollectionViewHandler holder, int position) {
        Product update = mItems.get(position);
        holder.bindExploreItem(update,mContext);

    }

    public int getItemViewType(int position) {
        return 0;
    }



}
