package com.acquire.shopick.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.acquire.shopick.R;
import com.acquire.shopick.io.model.SearchResult;
import com.acquire.shopick.util.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gaurav on 3/9/16.
 */
public class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    private View mView;
    private Context mContext;
    @Bind(R.id.session_snippet)
    public TextView title;
    @Bind(R.id.session_title)
    public TextView type;


    @Bind(R.id.offer_brand_category)
    Button showOffer;

    @Bind(R.id.collection_brand_category)
    Button showCollection;

    private SearchResult item;

    private String title_string;
    private String image_string;


    private ImageLoader mNoPlaceholderImageLoader;

    public SearchViewHolder(View view, Context context) {
        super(view);
        mView =  view;
        mContext = context;
        ButterKnife.bind(this, view);
        view.setOnClickListener(this);
        showCollection.setOnClickListener(this);
        showCollection.setTag(1);
        showOffer.setOnClickListener(this);
        showOffer.setTag(0);
    }

    public void bindSearchItem(SearchResult update,Context mContext) {
        item = update;
        if (item._type.equalsIgnoreCase("post")) {
            title_string = item._source.description;
            image_string = item._source.image_url;

        } else  {
            title_string = item._source.name;
            image_string = item._source.productImageUrl;

        }
        title.setText(title_string);
        if (item._type.equalsIgnoreCase("category")) {
            showCollection.setText(title_string + " Collection");
            showOffer.setText("Offers on " + title_string);
        } else {
            showCollection.setText(title_string + "'s Collection");
            showOffer.setText(title_string + "'s Offer");
        }
        type.setText(item._type);
    }

    @Override
    public void onClick(View v) {
        if (item._type.equalsIgnoreCase("category")) {
            if (v instanceof  Button) {
                if ((int)v.getTag() == 0) {
                    dispatchCategoryOfferIntent(item._source.id, item._source.image_url, item._source.name);
                } else if ((int)v.getTag() == 1){
                    dispatchCategoryCollectionIntent(item._source.id, item._source.image_url, item._source.name);
                }
            } else {
                dispatchCategoryIntent(item._source.id, item._source.image_url, item._source.name);
            }
        } else if (item._type.equalsIgnoreCase("brand")) {
            if (v instanceof  Button) {
                if ((int)v.getTag() == 0) {
                    dispatchBrandOfferIntent(item._source.id, item._source.image_url, item._source.name);
                } else if ((int)v.getTag() == 1){
                    dispatchBrandCollectionIntent(item._source.id, item._source.image_url, item._source.name);
                }
            } else {
                dispatchBrandIntent(item._source.id, item._source.logo_url, item._source.name);
            }
        }
    }

    private void dispatchBrandOfferIntent(Long id, String photoUrl, String title) {
        Intent collection = new Intent(mContext, FragmentContainerActivity.class);
        collection.putExtra(FragmentContainerActivity.SHOPICK_BRAND_ID, id);
        collection.putExtra(FragmentContainerActivity.SHOPICK_BRAND_PHOTO, photoUrl);
        collection.putExtra(FragmentContainerActivity.SHOPICK_BRAND_TITLE, title);
        collection.putExtra(FragmentContainerActivity.SHOPICK_BRAND_OFFER, true);
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_TITLE, title);
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_WHICH, FragmentContainerActivity.FRAGMENT_TYPE_METAPOSTITEMFRAGMENT);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Ensure that there's a camera activity to handle the intent
        mContext.startActivity(collection);

    }


    private void dispatchCategoryOfferIntent(Long id, String photoUrl, String title) {
        Intent collection = new Intent(mContext, FragmentContainerActivity.class);
        collection.putExtra(FragmentContainerActivity.SHOPICK_CATEGORY_ID, id);
        collection.putExtra(FragmentContainerActivity.SHOPICK_CATEGORY_PHOTO, photoUrl);
        collection.putExtra(FragmentContainerActivity.SHOPICK_CATEGORY_TITLE, title);
        collection.putExtra(FragmentContainerActivity.SHOPICK_CATEGORY_OFFER, true);
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_TITLE, title);
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_WHICH, FragmentContainerActivity.FRAGMENT_TYPE_FEEDITEMFRAGMENT);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Ensure that there's a camera activity to handle the intent
        mContext.startActivity(collection);

    }
    private void dispatchBrandCollectionIntent(Long id, String photoUrl, String title) {
        Intent collection = new Intent(mContext, FragmentContainerActivity.class);
        collection.putExtra(FragmentContainerActivity.SHOPICK_BRAND_ID, id);
        collection.putExtra(FragmentContainerActivity.SHOPICK_BRAND_PHOTO, photoUrl);
        collection.putExtra(FragmentContainerActivity.SHOPICK_BRAND_TITLE, title);
        collection.putExtra(FragmentContainerActivity.SHOPICK_BRAND_OFFER, false);
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_TITLE, title);
        // Ensure that there's a camera activity to handle the intent
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_WHICH, FragmentContainerActivity.FRAGMENT_TYPE_FEEDITEMFRAGMENT);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(collection);

    }


    private void dispatchCategoryCollectionIntent(Long id, String photoUrl, String title) {
        Intent collection = new Intent(mContext, FragmentContainerActivity.class);
        collection.putExtra(FragmentContainerActivity.SHOPICK_CATEGORY_ID, id);
        collection.putExtra(FragmentContainerActivity.SHOPICK_CATEGORY_PHOTO, photoUrl);
        collection.putExtra(FragmentContainerActivity.SHOPICK_CATEGORY_TITLE, title);
        collection.putExtra(FragmentContainerActivity.SHOPICK_CATEGORY_OFFER, false);
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_TITLE, title);
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_WHICH, FragmentContainerActivity.FRAGMENT_TYPE_FEEDITEMFRAGMENT);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Ensure that there's a camera activity to handle the intent
        mContext.startActivity(collection);

    }
    private void dispatchBrandIntent(Long id, String photoUrl, String title) {
        Intent collection = new Intent(mContext, FragmentContainerActivity.class);
        collection.putExtra(FragmentContainerActivity.SHOPICK_BRAND_ID, id);
        collection.putExtra(FragmentContainerActivity.SHOPICK_BRAND_PHOTO, photoUrl);
        collection.putExtra(FragmentContainerActivity.SHOPICK_BRAND_TITLE, title);
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_TITLE, title);
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_WHICH, FragmentContainerActivity.FRAGMENT_TYPE_FEEDITEMFRAGMENT);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Ensure that there's a camera activity to handle the intent
        mContext.startActivity(collection);

    }


    private void dispatchCategoryIntent(Long id, String photoUrl, String title) {
        Intent collection = new Intent(mContext, FragmentContainerActivity.class);
        collection.putExtra(FragmentContainerActivity.SHOPICK_CATEGORY_ID, id);
        collection.putExtra(FragmentContainerActivity.SHOPICK_CATEGORY_PHOTO, photoUrl);
        collection.putExtra(FragmentContainerActivity.SHOPICK_CATEGORY_TITLE, title);
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_TITLE, title);
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_WHICH, FragmentContainerActivity.FRAGMENT_TYPE_FEEDITEMFRAGMENT);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Ensure that there's a camera activity to handle the intent
        mContext.startActivity(collection);

    }

}
