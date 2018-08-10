package com.acquire.shopick.ui;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acquire.shopick.R;
import com.acquire.shopick.dao.BrandUpdates;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.io.model.Product;
import com.acquire.shopick.io.model.SearchResult;
import com.acquire.shopick.util.ImageLoader;

import java.util.ArrayList;

/**
 * Created by gaurav on 3/9/16.
 */

public class SearchRecyclerAdapter   extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    // additional top padding to add to first item of list
    int mContentTopClearance = 0;
    private final Context mContext;
    private ImageLoader mNoPlaceholderImageLoader;
    private int mDefaultSessionColor;

    private static final int VIEW_TYPE_POST = 0;
    private static final int VIEW_TYPE_PRODUCT = 1;
    private static final int VIEW_TYPE_BRAND = 2;
    private static final int VIEW_TYPE_CATEGORY = 3;
    private static final int VIEW_TYPE_DEFAULT = 9;



    // list of items served by this adapter
    ArrayList<SearchResult> mItems = new ArrayList<SearchResult>();

    // observers to notify about changes in the data
    ArrayList<DataSetObserver> mObservers = new ArrayList<DataSetObserver>();

    public SearchRecyclerAdapter(Context context) {
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
            case VIEW_TYPE_POST:
                View post = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_feed, parent, false);
                return new FeedViewHolder(post, mContext);
            case VIEW_TYPE_PRODUCT:
                View view__ = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_collection_product, parent, false);
                return new ProductCollectionViewHandler(view__, mContext);

            case VIEW_TYPE_BRAND:
                View view_ = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_brandcategory, parent, false);
                return new SearchViewHolder(view_, mContext);

            case VIEW_TYPE_CATEGORY:
                View view___ = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_brandcategory, parent, false);
                return new SearchViewHolder(view___, mContext);

            default:
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_explore, parent, false);
                return new SearchViewHolder(view, mContext);

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_POST:
                SearchResult update = mItems.get(position);
                Post p =  new Post();
                p.setGlobalID(update._source.globalID);
                p.setUsername(update._source.username);
                p.setCategoryname(update._source.categoryname);
                p.setStorename(update._source.storename);
                p.setCategoryname(update._source.categoryname);
                p.setPost_type(update._source.post_type);
                p.setImage_url(update._source.image_url);
                p.setTitle(update._source.title);
                p.setDescription(update._source.description);
                p.setUser_id(update._source.user_id);
                p.setStore_id(update._source.store_id);
                p.setBrandname(update._source.storename);
                p.setBrand_logo(update._source.brand_logo);
                p.setLiked(false);
                FeedViewHolder feedViewHolder = (FeedViewHolder)holder;
                feedViewHolder.bindExploreItem(p, mContext);
                break;
            case VIEW_TYPE_PRODUCT:
                SearchResult updatep = mItems.get(position);
                Product product =  new Product();
                product.setImage_url(updatep._source.productImageUrl);
                product.setTitle(updatep._source.name);
                product.setDescription(updatep._source.description);
                product.setGlobalID(updatep._source.globalID);
                product.setBrand(updatep._source.brand_name);
                ProductCollectionViewHandler searchViewHolderp = (ProductCollectionViewHandler)holder;
                searchViewHolderp.bindExploreItem(product, mContext);
                break;
            case VIEW_TYPE_BRAND:
                SearchResult updateb = mItems.get(position);
                SearchViewHolder searchViewHolderb = (SearchViewHolder)holder;
                searchViewHolderb.bindSearchItem(updateb, mContext);
                break;
            case VIEW_TYPE_CATEGORY:
                SearchResult updatec = mItems.get(position);
                SearchViewHolder searchViewHolderc = (SearchViewHolder)holder;
                searchViewHolderc.bindSearchItem(updatec, mContext);
                break;
            default:
                SearchResult updated = mItems.get(position);
                SearchViewHolder searchViewHolderd = (SearchViewHolder)holder;
                searchViewHolderd.bindSearchItem(updated, mContext);

        }

    }

    public int getItemViewType(int position) {

        SearchResult result = mItems.get(position);
        if (result._type.equalsIgnoreCase( "post")) {
            return VIEW_TYPE_POST;
        } else if (result._type.equalsIgnoreCase( "product")) {
            return VIEW_TYPE_PRODUCT;
        } else if (result._type.equalsIgnoreCase("category")) {
            return VIEW_TYPE_CATEGORY;
        } else if (result._type.equalsIgnoreCase("brand")) {
            return VIEW_TYPE_BRAND;
        }
        return VIEW_TYPE_DEFAULT;
    }

}
