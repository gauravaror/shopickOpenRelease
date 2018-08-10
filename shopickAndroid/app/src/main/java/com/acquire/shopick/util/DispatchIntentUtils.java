package com.acquire.shopick.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;

import com.acquire.shopick.dao.Post;
import com.acquire.shopick.dao.PostCollection;
import com.acquire.shopick.dao.Product;
import com.acquire.shopick.io.model.AllOffer;
import com.acquire.shopick.io.model.RedeemPick;
import com.acquire.shopick.ui.ActivityUpdatePhoneNumber;
import com.acquire.shopick.ui.BrandPickerActivity;
import com.acquire.shopick.ui.FragmentContainerActivity;
import com.acquire.shopick.ui.LikedBrandActivity;
import com.acquire.shopick.ui.LocalPostActivity;
import com.acquire.shopick.ui.LoginActivity;
import com.acquire.shopick.ui.PickDisplayActivity;
import com.acquire.shopick.ui.PostCollectionActivity;
import com.acquire.shopick.ui.PostFeedItem;
import com.acquire.shopick.ui.RedeemPicks;
import com.acquire.shopick.ui.ReedemReferralActivity;
import com.acquire.shopick.ui.ReferAndWinPicksActivity;
import com.acquire.shopick.ui.SearchableActivity;
import com.acquire.shopick.ui.SettingActivity;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ShareEvent;

/**
 * Created by gaurav on 10/31/15.
 */
public class DispatchIntentUtils {


    public static Intent dispatchDeepLinkIntent(Context mContext, String deepLink) {
        Intent intent_deep = new Intent (Intent.ACTION_VIEW);
        intent_deep.setData(Uri.parse(deepLink));
        intent_deep.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent_deep.setPackage("com.acquire.shopick");
        return intent_deep;
    }

    public static Intent dispatchRedeemPicksIntent(Context mContext) {
        Intent collection = new Intent(mContext, RedeemPicks.class);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return collection;
    }

    public static Intent dispatchEarnPicksIntent(Context mContext) {
        Intent collection = new Intent(mContext, com.acquire.shopick.ui.EarnPicks.class);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return collection;
    }


    public static Intent dispatchReferralIntent(Context mContext) {
        Intent collection = new Intent(mContext, ReferAndWinPicksActivity.class);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return collection;
    }

    public static Intent dispatchRedeemIntent(Context mContext) {
        Intent collection = new Intent(mContext, ReedemReferralActivity.class);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return collection;
    }

    public static Intent dispatchDisplayPicksIntent(Context mContext) {
        Intent collection = new Intent(mContext, PickDisplayActivity.class);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return collection;
    }


    public static Intent dispatchTipsIntent(Context mContext) {
        Intent collection = new Intent(mContext, FragmentContainerActivity.class);
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_TITLE, "Tips");
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_WHICH, FragmentContainerActivity.FRAGMENT_TYPE_TIPSFRAGMENT);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Ensure that there's a camera activity to handle the intent
        return collection;
    }


    public static Intent dispatchPostOpenIntent(Context mContext, String uniqID) {
        Intent local_post = new Intent(mContext,LocalPostActivity.class);
        local_post.putExtra(LocalPostActivity.SHOPICK_POST_UNIQ_ID, uniqID);
        local_post.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return local_post;

    }


    public static Intent dispatchPostCollectionOpenIntent(Context mContext, PostCollection pc) {
        Intent collection = new Intent(mContext, PostCollectionActivity.class);
        collection.putExtra(PostCollectionActivity.SHOPICK_POST_COLLECTION_ID, pc.getGlobalID());
        collection.putExtra(PostCollectionActivity.SHOPICK_POST_COLLECTION_TITLE, pc.getTitle());
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return collection;
    }

    public static Intent dispatchAllOfferIntent(Context mContext) {
        Intent collection = new Intent(mContext, FragmentContainerActivity.class);
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_TITLE, "Offers");
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_WHICH, FragmentContainerActivity.FRAGMENT_TYPE_AllOFFERFRAGMENT);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Ensure that there's a camera activity to handle the intent
        return collection;
    }

    public static Intent dispatchMetaPostsIntent(Context mContext) {
        Intent collection = new Intent(mContext, FragmentContainerActivity.class);
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_TITLE, "Offers");
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_WHICH, FragmentContainerActivity.FRAGMENT_TYPE_METAPOSTSFRAGMENT);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Ensure that there's a camera activity to handle the intent
        return collection;
    }

    public static Intent dispatchExploreIntent(Context mContext) {
        Intent collection = new Intent(mContext, FragmentContainerActivity.class);
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_TITLE, "Curated Collection");
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_WHICH, FragmentContainerActivity.FRAGMENT_TYPE_EXPLOREFRAGMENT);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Ensure that there's a camera activity to handle the intent
        return collection;
    }

    public static Intent openFeedMainFragment(Context mContext) {
        Intent collection = new Intent(mContext, FragmentContainerActivity.class);
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_TITLE, "Collection");
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_WHICH, FragmentContainerActivity.FRAGMENT_TYPE_FEEDMAINFRAGMENT);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Ensure that there's a camera activity to handle the intent
        return collection;
    }

    public static Intent openLatestLaunch(Context mContext) {
        Intent collection = new Intent(mContext, FragmentContainerActivity.class);
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_TITLE, "Latest Launch");
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_WHICH, FragmentContainerActivity.FRAGMENT_TYPE_LatestLaunch);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Ensure that there's a camera activity to handle the intent
        return collection;
    }

    public static Intent openExploreFragmentItem(Context mContext, String brand_name, Long brand_id) {
        Intent collection = new Intent(mContext, FragmentContainerActivity.class);
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_TITLE, brand_name);
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_WHICH, FragmentContainerActivity.FRAGMENT_TYPE_EXPLOREFRAGMENT_ITEM);
        collection.putExtra("id", brand_id);
        collection.putExtra("category_id", -1L);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Ensure that there's a camera activity to handle the intent
        return collection;
    }

    public static Intent openFeedFragmentItem(Context mContext, String brand_name, Long brand_id) {
        Intent collection = new Intent(mContext, FragmentContainerActivity.class);
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_TITLE, brand_name);
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_WHICH, FragmentContainerActivity.FRAGMENT_TYPE_FEEDITEMFRAGMENT);
        collection.putExtra(FragmentContainerActivity.SHOPICK_BRAND_ID, brand_id);
        collection.putExtra("category_id", -1L);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Ensure that there's a camera activity to handle the intent
        return collection;
    }

    public static Intent openMetaPostFragmentItem(Context mContext, String brand_name, Long brand_id) {
        Intent collection = new Intent(mContext, FragmentContainerActivity.class);
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_TITLE, brand_name);
        collection.putExtra(FragmentContainerActivity.SHOPICK_FRAGMENT_CONTAINER_GENERIC_WHICH, FragmentContainerActivity.FRAGMENT_TYPE_METAPOSTITEMFRAGMENT);
        collection.putExtra(FragmentContainerActivity.SHOPICK_BRAND_ID, brand_id);
        collection.putExtra("category_id", -1L);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Ensure that there's a camera activity to handle the intent
        return collection;
    }


    public static Intent dispatchPostIntent(Context mContext) {
        Intent collection = new Intent(mContext, PostFeedItem.class);
        return collection;
    }

    public static Intent dispatchPostWithPostCollectionIntent(Context mContext, PostCollection postCollection) {
        Intent collection = new Intent(mContext, PostFeedItem.class);
        return collection;
    }

    public static Intent dispatchLikedBrandntent(Context mContext) {
        Intent collection = new Intent(mContext, LikedBrandActivity.class);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return collection;
    }

    public static Intent dispatchSettingsIntent(Context mContext) {
        Intent collection = new Intent(mContext, SettingActivity.class);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return collection;
    }

    public static Intent dispatchOpenBrandList(Context mContext) {
        Intent collection = new Intent(mContext, BrandPickerActivity.class);
        return collection;
    }


    public static Intent dispatchLoginIntent(Context mContext, boolean first) {
        Intent collection = new Intent(mContext,LoginActivity.class);
        collection.putExtra(LoginActivity.SHOPICK_LOGIN_OPENING, first);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Ensure that there's a camera activity to handle the intent
        return collection;

    }

    public static Intent dispatchSearchIntent(Context mContext, boolean first) {
        Intent collection = new Intent(mContext, SearchableActivity.class);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return collection;

    }

    public static Intent dispatchUpdateMobileIntentPost(Context mContext, Post post) {
        Intent phone = new Intent(mContext,ActivityUpdatePhoneNumber.class);
        phone.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        phone.putExtra("globalID", post.getGlobalID());
        phone.putExtra("post", true);
        phone.putExtra("post_id", post.getId());
        // Ensure that there's a camera activity to handle the intent
        return phone;

    }

    public static Intent dispatchUpdateMobileIntentPostCollection(Context mContext, PostCollection post) {
        Intent phone = new Intent(mContext,ActivityUpdatePhoneNumber.class);
        phone.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        phone.putExtra("globalID", post.getGlobalID());
        phone.putExtra("post_collection", true);
        phone.putExtra("post_collection_id", post.getId());
        return phone;
    }


    public static Intent dispatchUpdateMobileIntentRedeem(Context mContext, RedeemPick redeemPick) {
        Intent phone = new Intent(mContext,ActivityUpdatePhoneNumber.class);
        phone.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        phone.putExtra("globalID", redeemPick.getGlobalID());
        phone.putExtra("redeem", true);
        phone.putExtra("redeem_id", redeemPick.getId());
        phone.putExtra("redeem_title", "Provide us your phone number! We will get in touch shortly with details of redemption.");
        // Ensure that there's a camera activity to handle the intent
        return phone;

    }

    public static void startActivityWithCondition(Context mContext, Intent startIntent) {
        Activity activity = (Activity)mContext;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity.startActivity(startIntent, ActivityOptionsCompat
                    .makeSceneTransitionAnimation((Activity) mContext).toBundle());
        } else  {
            activity.startActivity(startIntent);
        }
    }

    public static void startActivityWithConditionForResult(Context mContext, int resultCode,Intent startIntent) {
        Activity activity = (Activity)mContext;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity.startActivityForResult(startIntent, resultCode, ActivityOptionsCompat
                    .makeSceneTransitionAnimation((Activity) mContext).toBundle());
        } else  {
            activity.startActivityForResult(startIntent, resultCode);
        }
    }

    public static Intent dispatchUpdateMobileIntentProduct(Context mContext, com.acquire.shopick.io.model.Product product) {
        Intent phone = new Intent(mContext,ActivityUpdatePhoneNumber.class);
        phone.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        phone.putExtra("globalID", product.getGlobalID());
        phone.putExtra("post", false);
        phone.putExtra("product_id", product.getId());
        // Ensure that there's a camera activity to handle the intent
        return phone;

    }


    public static Intent dispatchUpdateMobileIntentProduct(Context mContext, Product product) {
        Intent phone = new Intent(mContext,ActivityUpdatePhoneNumber.class);
        phone.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        phone.putExtra("globalID", product.getGlobalID());
        phone.putExtra("post", false);
        phone.putExtra("product_id", product.getId());
        // Ensure that there's a camera activity to handle the intent
        return phone;

    }

    public static Intent dispatchUpdateMobileIntentBanner(Context mContext, AllOffer product) {
        Intent phone = new Intent(mContext,ActivityUpdatePhoneNumber.class);
        phone.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        phone.putExtra("globalID", product.getGlobalID());
        phone.putExtra("banner", true);
        phone.putExtra("banner_id", product.getId());
        phone.putExtra("banner_title", "Ask us to locate this offer at your closest location or get you any other information about this offer.");
        // Ensure that there's a camera activity to handle the intent
        return phone;

    }

    public static Intent dispatchShareAppIntent(int profileId) {
        Answers.getInstance().logShare(new ShareEvent()
                .putMethod("shareDialog")
                .putContentType("AppShare")
                .putContentId(String.valueOf(profileId)));
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out Shopick. You can discover offers at your favorite brands like Levis, Forever 21, Jack & Jones, Only posted by other users. https://goo.gl/jM4kzj");
        sendIntent.setType("text/plain");
        return sendIntent;
    }

    public static Intent dispatchShareAppIntent( String referralCode) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out Shopick. You can discover offers at your favorite brands posted by other users. https://www.shopick.co.in/redeem_referral?code="+referralCode);
        sendIntent.setType("text/plain");
        return sendIntent;
    }


    public static Intent dispatchSharePostIntent( String title, String globalID, String desc, Uri imageUri) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, title + "  " + desc + " https://www.shopick.co.in/collection/" + globalID);
        sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        sendIntent.setType("image/jpeg");
        return sendIntent;
    }

    public static Intent dispatchSharePostCollectionIntent( String title, String globalID, String desc, Uri imageUri) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, title + "  " + desc + " https://www.shopick.co.in/post/" + globalID);
        sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        sendIntent.setType("image/jpeg");
        return sendIntent;
    }

    public static Intent dispatchSharePresentationIntent( String title, String globalID, Uri imageUri) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, title + " https://www.shopick.co.in/presentation/" + globalID);
        sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        sendIntent.setType("image/jpeg");
        return sendIntent;
    }


    public static Intent dispatchShareAllOfferIntent( String title, String intentUrl, Uri imageUri) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, title + " " + intentUrl);
        sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        sendIntent.setType("image/jpeg");
        return sendIntent;
    }

    public static Intent dispatchShareBrandUpdateIntent( String title, String globalID,  Uri imageUri) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, title + " https://www.shopick.co.in/updates/" + globalID);
        sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        sendIntent.setType("image/jpeg");
        return sendIntent;
    }

    public static Intent dispatchShareProductIntent( String title, String globalID,  Uri imageUri) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, title + " https://www.shopick.co.in/product/" + globalID);
        sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        sendIntent.setType("image/jpeg");
        return sendIntent;
    }

}
