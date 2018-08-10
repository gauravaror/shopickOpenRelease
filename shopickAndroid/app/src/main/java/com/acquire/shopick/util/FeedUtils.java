package com.acquire.shopick.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.acquire.shopick.R;
import com.acquire.shopick.dao.BrandUpdates;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.dao.PostCollection;
import com.acquire.shopick.ui.CategoryPickerActivity;
import com.acquire.shopick.ui.CoreActivity;
import com.acquire.shopick.ui.LocationPickerActivity;
import com.acquire.shopick.ui.Shopick;

import java.util.ArrayList;

/**
 * Created by gaurav on 11/5/15.
 */
public class FeedUtils {


    public static SpannableStringBuilder getHorizontalOfferTitle(final Context mContext, PostCollection post) {
        int brandName_end = 0;

        String textName = "";
        if (post.getBrand_name() != null) {
            textName = post.getBrand_name();
            textName += " : ";
            brandName_end = textName.length();

        }
        textName += post.getTitle();
        SpannableStringBuilder spannable = new SpannableStringBuilder(textName);
        ClickableSpan clickSpan = new ClickableSpan() {

            @Override
            public void onClick(View widget)
            {

            }
            public void updateDrawState(TextPaint ds) {// override updateDrawState
                ds.setColor(mContext.getResources().getColor(R.color.body_text_1));
                ds.setUnderlineText(false); // set to false to remove underline
                ds.density = 2.0f;

            }
        };
        spannable.setSpan(clickSpan, 0, brandName_end , Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannable;

    }


    public static SpannableStringBuilder getBrandUpdateTitle(final Context mContext, BrandUpdates brand_updates) {
        int brandName_end = 0;

        String textName = "";
        if (brand_updates.getBrand_name() != null) {
            textName = brand_updates.getBrand_name();
            textName += " : ";
            brandName_end = textName.length();

        }
        textName += brand_updates.getTitle();
        SpannableStringBuilder spannable = new SpannableStringBuilder(textName);
        ClickableSpan clickSpan = new ClickableSpan() {

            @Override
            public void onClick(View widget)
            {

            }
            public void updateDrawState(TextPaint ds) {// override updateDrawState
                ds.setColor(mContext.getResources().getColor(R.color.body_text_1));
                ds.setUnderlineText(false); // set to false to remove underline
                ds.density = 2.0f;

            }
        };
        spannable.setSpan(clickSpan, 0, brandName_end , Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannable;

    }

    public static SpannableStringBuilder getPostCollectionTitle(final Context mContext, PostCollection post_collection) {
        return getHorizontalOfferTitle(mContext, post_collection);
    }

    public static Spanned getFeedMarqueeString(final Context mContext, ArrayList<Post> posts) {
        Spanned string = new SpannableStringBuilder(" ");
        int size = 0;
        if (posts.size() > 5) {
            size = 5;
        } else {
            size = posts.size();
        }
        for (int i = 0; i < size; i++) {
            string =  (Spanned)TextUtils.concat(string, getFeedTitle(mContext, posts.get(i)).toString());
        }
        return string;

    }

    public static SpannableStringBuilder getFeedTitle(final Context mContext, Post local_post) {
        return getFeedTitle(mContext, local_post.getUsername(), local_post.getUser_id(), local_post.getCategoryname(),
                local_post.getStorename(), local_post.getStore_id(), local_post.getCategoryname(), local_post.getPost_type());

    }

    public static SpannableStringBuilder getFeedTitle(final Context mContext, String username, Long user_id,String category,
                                         String storename, Long store_id, String timeunit, int posttype) {

        int categorySpanEnd = -1;
        int storeSpanEnd = -1;
        String postText = username;
        int nameSpanEnd = postText.length();
        postText += " ";
        if (!TextUtils.isEmpty(storename) || !TextUtils.isEmpty(category)) {
            int betweentextSpanStart = postText.length();
            switch (posttype) {
                case 0:
                    postText += "bought ";
                    break;
                case 1:
                    postText += "liked ";
                    break;
                case 2:
                    postText += "discovered a discount ";
                    break;
                default:
                    postText += "visited ";
                    break;
            }
            int betweentextSpanEnd = postText.length();
            if ((posttype == 0 || posttype == 1) && !TextUtils.isEmpty(category)) {
                postText += category ;
            }
            if ((posttype == 0 || posttype == 1) && !TextUtils.isEmpty(storename) && !TextUtils.isEmpty(category)) {
                postText += " at ";
            } else if ((posttype == 0 ) &&  !TextUtils.isEmpty(storename) && TextUtils.isEmpty(category) ) {
                postText += " from ";
            }
            }

            if (!TextUtils.isEmpty(storename) && posttype  == 2) {
                postText += " at ";
            }

            categorySpanEnd = postText.length();

            if (!TextUtils.isEmpty(storename)) {
                postText += storename;
            }
            storeSpanEnd = postText.length();


        ClickableSpan clickSpan = new ClickableSpan() {

            @Override
            public void onClick(View widget)
            {

            }
            public void updateDrawState(TextPaint ds) {// override updateDrawState
                ds.setColor(mContext.getResources().getColor(R.color.body_text_1));
                ds.setUnderlineText(false); // set to false to remove underline
                ds.density = 2.0f;

            }
        };

        ClickableSpan clickSpan2 = new ClickableSpan() {

            @Override
            public void onClick(View widget)
            {
            }

            public void updateDrawState(TextPaint ds) {// override updateDrawState
                ds.setColor(mContext.getResources().getColor(R.color.body_text_1));
                ds.density = 2.0f;
                ds.setUnderlineText(false); // set to false to remove underline

            }
        };
        SpannableStringBuilder spannable  =  new SpannableStringBuilder(postText);

        spannable.setSpan(clickSpan, 0, nameSpanEnd , Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        if (!TextUtils.isEmpty(storename) && categorySpanEnd != -1 && storeSpanEnd != -1) {
            spannable.setSpan(clickSpan2, categorySpanEnd, storeSpanEnd , Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        return  spannable;
    }


    public static SpannableStringBuilder getFeaturedTitle(String title, String globalID, Context mContext) {
        String featured_main = "  Featured in: ";

        int start_length = featured_main.length();

        featured_main += title;

        int end_length = featured_main.length();

        ClickableSpan clickSpan = new ClickableSpan() {

            @Override
            public void onClick(View widget)
            {
                mContext.startActivity(DispatchIntentUtils.dispatchDeepLinkIntent(mContext, "android://www.shopick.co.in/collection/"+globalID));
            }
            public void updateDrawState(TextPaint ds) {// override updateDrawState
                ds.setColor(mContext.getResources().getColor(R.color.body_text_1));
                ds.setUnderlineText(false); // set to false to remove underline
                ds.density = 2.0f;

            }
        };

        SpannableStringBuilder spannable  =  new SpannableStringBuilder(featured_main);

        spannable.setSpan(clickSpan, start_length, end_length , Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return  spannable;
    }
}
