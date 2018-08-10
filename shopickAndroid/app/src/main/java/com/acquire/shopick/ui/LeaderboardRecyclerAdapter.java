package com.acquire.shopick.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acquire.shopick.R;
import com.acquire.shopick.dao.User;
import com.acquire.shopick.ui.widget.BezelImageView;

import java.util.ArrayList;

/**
 * Created by gaurav on 4/4/16.
 */
public class LeaderboardRecyclerAdapter extends RecyclerView.Adapter<UserListViewHolder> {


    // list of items served by this adapter
    ArrayList<User> mItems = new ArrayList<User>();

    private Context mContext;


    public LeaderboardRecyclerAdapter(Context context) {
        mContext = context;
    }



    @Override
    public UserListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_users, parent, false);
        return new UserListViewHolder(view, mContext);

    }

    @Override
    public void onBindViewHolder(UserListViewHolder holder, int position) {
        holder.onBindUserView(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
