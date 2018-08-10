package com.acquire.shopick.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.acquire.shopick.R;
import com.acquire.shopick.dao.User;
import com.acquire.shopick.ui.widget.BezelImageView;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gaurav on 4/4/16.
 */
public class UserListViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.profile_image)
    public BezelImageView profileImage;

    @Bind(R.id.profile_usercode_text)
    public TextView usercode;

    @Bind(R.id.profile_rank_text)
    public TextView userRank;

    @Bind(R.id.profile_picks_text)
    public TextView picks;

    @Bind(R.id.user_account_frame)
    public FrameLayout frameLayout;

    private ImageLoader mPlaceholderImageLoader;

    private Context mContext;

    public UserListViewHolder(View view, Context con) {
        super(view);
        ButterKnife.bind(this, view);
        mContext = con;

    }

    public void onBindUserView(final User user) {

        if (mPlaceholderImageLoader == null) {
            mPlaceholderImageLoader = new ImageLoader(mContext,R.drawable.person_image_empty);
        }
        if (!TextUtils.isEmpty(user.getProfileImage()) ) {
            // This is a image url we got from google, so send as it is.
            mPlaceholderImageLoader.loadImage(user.getProfileImage(), profileImage);
        } else {
            profileImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.person_image_empty));
        }
        usercode.setText(user.getUsercode());

        picks.setText(user.getMonthlyPicks().toString());

        userRank.setText(user.getRank().toString());

        if (user.getEmail().equalsIgnoreCase(AccountUtils.getActiveAccountName(mContext))) {
            frameLayout.setBackgroundColor(mContext.getResources().getColor(R.color.com_facebook_blue));
        } else {
            frameLayout.setBackgroundResource(R.color.transparent_new);
        }
    }
}
