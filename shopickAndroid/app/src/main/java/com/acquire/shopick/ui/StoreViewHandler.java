package com.acquire.shopick.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.acquire.shopick.Config;
import com.acquire.shopick.R;
import com.acquire.shopick.dao.Store;
import com.acquire.shopick.io.model.ProductMini;
import com.acquire.shopick.util.ImageLoader;
import com.acquire.shopick.util.UIUtils;

/**
 * Created by gaurav on 10/4/15.
 */
public class StoreViewHandler extends RecyclerView.ViewHolder {

    private Store item;
    private ImageView photoView;
    private TextView snippet;
    private TextView snippet_short;

    private TextView title;
    private ImageLoader mNoPlaceholderImageLoader;
    private int mDefaultSessionColor;
    private FrameLayout linear;



    public StoreViewHandler(View view) {
        super(view);

        title = (TextView) view.findViewById(R.id.store_title);
        snippet = (TextView) view.findViewById(R.id.store_snippet);
        photoView = (ImageView) view.findViewById(R.id.store_item_photo_colored);


    }

    public void bindExploreItem(Store store,Context mContext) {
        item = store;
        title.setText(item.getName());
        snippet.setText(item.getAddress());
        int darkSessionColor = 0;
        mDefaultSessionColor = mContext.getResources().getColor(R.color.theme_accent_1_light);

        darkSessionColor = UIUtils.scaleSessionColorToDefaultBG(mDefaultSessionColor);

      //  ViewCompat.setTransitionName(photoView, "photo_");
        if (mNoPlaceholderImageLoader == null) {
            mNoPlaceholderImageLoader = new ImageLoader(mContext,R.drawable.placeholder);
        }
        if (item.getBrand_logo() != null) {
          mNoPlaceholderImageLoader.loadImage(Config.PROD_BASE_URL + item.getBrand_logo(), photoView);
        }


    }
}
