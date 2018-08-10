package com.acquire.shopick.ui;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.acquire.shopick.Config;
import com.acquire.shopick.R;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.job.GetBrandsJob;
import com.acquire.shopick.job.OpenShopickWhatsAppJob;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.FeedUtils;
import com.acquire.shopick.util.FileUtils;
import com.acquire.shopick.util.GenericUtils;
import com.acquire.shopick.util.ImageLoader;
import com.acquire.shopick.util.ShareDialogUtils;
import com.acquire.shopick.util.SimpleCustomChromeTabsHelper;
import com.acquire.shopick.util.SnackbarUtil;
import com.acquire.shopick.util.WebviewFallback;
import com.path.android.jobqueue.JobManager;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.acquire.shopick.util.LogUtils.LOGD;

/**
 * Created by gaurav on 2/24/16.
 */
public class FindAnythingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private View mView;
    private Context mContext;
    @Bind(R.id.findanythingwhatsapp)
    public Button whatsapp_button;
    @Bind(R.id.findanythingnormal)
    public Button findanything_button;

    private  SimpleCustomChromeTabsHelper mCustomTabHelper;

    @Inject
    JobManager jobManager;


    public FindAnythingViewHolder(View view, Context con) {
        super(view);
        mView = view;
        mContext = con;
        ButterKnife.bind(this, view);
        whatsapp_button.setOnClickListener(this);
        whatsapp_button.setTag(0);
        findanything_button.setOnClickListener(this);
        findanything_button.setTag(1);
        mView.setOnClickListener(this);
        mView.setTag(2);
        ShopickApplication.injectMembers(this);
        mCustomTabHelper = new SimpleCustomChromeTabsHelper((Activity)mContext);
        mCustomTabHelper.prepareUrl("http://shopick.co.in/findanything");
        mCustomTabHelper.setFallback(new WebviewFallback());

    }

    void sendFindAnythingEvent() {
        // BusProvider.getInstance().post(new  LoadBrandEvent(AccountUtils.getShopickProfileId(getActivity().getApplicationContext()), filter, brand_id));
        jobManager.addJob(new OpenShopickWhatsAppJob(mContext));
    }

    public void bindExploreItem(final Post update, final Context mContext) {

    }

    @Override
    public void onClick(View v) {
        if ((int)v.getTag() ==  0) {
            if (GenericUtils.sendMessageShopick(mContext, mView)) {

            } else {
                sendFindAnythingEvent();
            }
        }else{
            mCustomTabHelper.openUrl("http://shopick.co.in/findanything");
        }

    }

    private void dispatchPostOpenIntent(String uniqID) {
        Intent local_post = new Intent(mContext,LocalPostActivity.class);
        local_post.putExtra(LocalPostActivity.SHOPICK_POST_UNIQ_ID, uniqID);
        local_post.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(local_post);

    }


}
