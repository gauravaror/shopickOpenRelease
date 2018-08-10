package com.acquire.shopick.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acquire.shopick.R;
import com.acquire.shopick.io.model.EarnPicks;
import com.acquire.shopick.io.model.RedeemPick;

import java.util.ArrayList;

/**
 * Created by gaurav on 4/5/16.
 */
public class RedeemPicksRecyclerAdapter extends RecyclerView.Adapter<RedeemPicksViewHolder> {


    boolean open_it = true;

    // list of items served by this adapter
    ArrayList<RedeemPick> mItems = new ArrayList<RedeemPick>();

    private Context mContext;


    public RedeemPicksRecyclerAdapter(Context context) {
        mContext = context;
    }


    @Override
    public RedeemPicksViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_redeem_picks, viewGroup, false);
        return new RedeemPicksViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(RedeemPicksViewHolder redeemPicksViewHolder, int position) {
        redeemPicksViewHolder.onBindRedeemPickView(mItems.get(position), open_it);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
