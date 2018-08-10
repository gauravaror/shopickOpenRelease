package com.acquire.shopick.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.acquire.shopick.Config;
import com.acquire.shopick.R;
import com.acquire.shopick.io.model.Product;
import com.acquire.shopick.util.ImageLoader;
import com.acquire.shopick.util.UIUtils;

/**
 * Created by gaurav on 10/4/15.
 */
public class ProductCollectionViewHandler extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Product item;
    private ImageView photoView;
    private TextView snippet;
    private TextView snippet_short;

    private TextView title;
    private ImageLoader mNoPlaceholderImageLoader;
    private int mDefaultSessionColor;
    private FrameLayout linear;
    private Context mContext;



    public ProductCollectionViewHandler(View view, Context context) {
        super(view);
        mContext = context;
        title = (TextView) view.findViewById(R.id.collection_title);
        snippet = (TextView) view.findViewById(R.id.collection_snippet);
        photoView = (ImageView) view.findViewById(R.id.collection_item_photo_colored);
        view.setOnClickListener(this);

    }

    public void bindExploreItem(Product update,Context context) {
        item = update;
        title.setText(item.getBrand());
        snippet.setText(item.getTitle());
        int darkSessionColor = 0;
        mContext = context;
        mDefaultSessionColor = mContext.getResources().getColor(R.color.theme_accent_1_light);

        darkSessionColor = UIUtils.scaleSessionColorToDefaultBG(mDefaultSessionColor);

      //  ViewCompat.setTransitionName(photoView, "photo_");
        if (mNoPlaceholderImageLoader == null) {
            mNoPlaceholderImageLoader = new ImageLoader(mContext,R.drawable.placeholder_verticle);
        }
        if (item.getImage_url() != null) {
          mNoPlaceholderImageLoader.loadImage(Config.PROD_BASE_URL + item.getImage_url(), photoView,
                  mContext.getResources().getDrawable(R.drawable.placeholder_verticle));
        }


    }

    @Override
    public void onClick(View v) {
        dispatchProductIntent(item.getGlobalID(), item.getImage_url(), item.getTitle());
    }


    private void dispatchProductIntent(String product_id, String photoUrl, String title) {
        Intent collection = new Intent(mContext,ProductActivity.class);
        collection.putExtra(ProductActivity.SHOPICK_PRODUCT_ID, product_id);
        collection.putExtra(ProductActivity.SHOPICK_PRODUCT_PHOTO, photoUrl);
        collection.putExtra(ProductActivity.SHOPICK_PRODUCT_TITLE, title);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Ensure that there's a camera activity to handle the intent
        mContext.startActivity(collection);

    }

}
