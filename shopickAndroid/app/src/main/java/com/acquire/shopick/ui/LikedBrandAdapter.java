package com.acquire.shopick.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acquire.shopick.R;
import com.acquire.shopick.dao.Brands;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by gaurav on 3/21/16.
 */
public class LikedBrandAdapter extends RecyclerView.Adapter<LikedBrandViewHolder> {

    private final Context mContext;
    // list of items served by this adapter
    ArrayList<Brands> mItems = new ArrayList<Brands>();
    ArrayList<Brands> mItemsCopy = new ArrayList<Brands>();



    public LikedBrandAdapter(Context context) {
        mContext = context;

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
            for (Brands wp : mItemsCopy)
            {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    mItems.add(wp);
                }
            }
        }
    }

    @Override
    public LikedBrandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_likedbrand, parent, false);
        return new LikedBrandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LikedBrandViewHolder holder, int position) {
        Brands update = mItems.get(position);
        LikedBrandViewHolder feed_holder = (LikedBrandViewHolder)holder;
        feed_holder.bindBrandItem(update, mContext);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}

