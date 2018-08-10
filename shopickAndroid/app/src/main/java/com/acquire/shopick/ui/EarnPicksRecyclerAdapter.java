package com.acquire.shopick.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acquire.shopick.R;
import com.acquire.shopick.io.model.*;
import com.acquire.shopick.io.model.EarnPicks;

import java.util.ArrayList;

/**
 * Created by gaurav on 4/5/16.
 */
public class EarnPicksRecyclerAdapter extends RecyclerView.Adapter<EarnPicksViewHolder> {



    // list of items served by this adapter
    ArrayList<com.acquire.shopick.io.model.EarnPicks> mItems = new ArrayList<EarnPicks>();

    private Context mContext;


    public EarnPicksRecyclerAdapter(Context context) {
        mContext = context;
    }


    @Override
    public EarnPicksViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_earn_picks, viewGroup, false);
        return new EarnPicksViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(EarnPicksViewHolder earnPicksViewHolder, int position) {
        earnPicksViewHolder.onBindEarnPickView(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
