package com.acquire.shopick.ui;

/**
 * Created by gaurav on 11/28/15.
 */

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.acquire.shopick.Config;
import com.acquire.shopick.R;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.dao.Tips;
import com.acquire.shopick.job.TipLikeJob;
import com.acquire.shopick.job.TipUnLikeJob;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.acquire.shopick.util.FileUtils;
import com.acquire.shopick.util.ImageLoader;
import com.acquire.shopick.util.ShareDialogUtils;
import com.acquire.shopick.util.SnackbarUtil;
import com.path.android.jobqueue.JobManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import static com.acquire.shopick.util.LogUtils.LOGD;
import static com.acquire.shopick.util.LogUtils.makeLogTag;

public class PresentationFlipAdapter extends BaseAdapter implements OnClickListener {

    private static final String TAG = makeLogTag(PresentationFlipAdapter.class);

    @Inject
    JobManager jobManager;


    public interface Callback{
        public void onPageRequested(int page);
    }


    private LayoutInflater inflater;
    private Callback callback;
    ArrayList<Tips> mItems = new ArrayList<Tips>();
    private ImageLoader mNoPlaceholderImageLoader;
    private Context mContext;


    public PresentationFlipAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.mContext = context;
        ShopickApplication.injectMembers(this);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;

    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
         final ViewHolder holder;

        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.presentation_view_flipper, parent, false);
            holder.mPhotoViewContainer = convertView.findViewById(R.id.session_photo_container);
            holder.mPhotoView = (ImageView) convertView.findViewById(R.id.session_photo);
            holder.mDetailsContainer = convertView.findViewById(R.id.details_container);
            holder.title = (TextView) convertView.findViewById(R.id.presentation_header);
            holder.description = (TextView) convertView.findViewById(R.id.presentation_abstract);
            holder.firstPage = (ImageButton) convertView.findViewById(R.id.first_page);
            holder.lastPage = (ImageButton) convertView.findViewById(R.id.last_page);
            holder.nextPage = (Button) convertView.findViewById(R.id.goto_like_presentation);
            holder.share_pres = (Button) convertView.findViewById(R.id.goto_share);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(mItems.get(position).getTitle());
        holder.description.setText(mItems.get(position).getDescription());
        LOGD(TAG, mItems.get(position).getImage_url());
        if (mNoPlaceholderImageLoader == null) {
            mNoPlaceholderImageLoader = new ImageLoader(mContext, R.drawable.placeholder);
        }
        boolean liked = mItems.get(position).getLiked().booleanValue();
        if (liked) {
            holder.nextPage.setTextColor(mContext.getResources().getColor(R.color.theme_primary));
            holder.nextPage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_orange_50_24dp, 0, 0, 0);
        } else {
            holder.nextPage.setTextColor(mContext.getResources().getColor(R.color.map_info_2));
            holder.nextPage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_black_24dp, 0, 0, 0);
        }
        // mAbstract.setText(description);
        //mNoPlaceholderImageLoader.loadImage(Config.PROD_BASE_URL + mItems.get(position).getImage_url(), holder.mPhotoView);
        Picasso.with(mContext).load(Config.PROD_BASE_URL + mItems.get(position).getImage_url()).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.mPhotoView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                holder.mHasPhoto = true;
                recomputePhotoAndScrollingMetrics(holder);

            }

            @Override
            public void onError() {

            }
        });

        holder.nextPage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!AccountUtils.getLoginDone(mContext)) {
                    SnackbarUtil.createStartingLogin(holder.mDetailsContainer);
                    AnalyticsManager.sendEvent("LikeWithoutLoging", "Errors", "Tips", AccountUtils.getShopickTempProfileId(mContext));
                    mContext.startActivity(DispatchIntentUtils.dispatchLoginIntent(mContext, true));
                    return;
                }
                boolean liked = mItems.get(position).getLiked().booleanValue();
                if (liked) {
                    jobManager.addJob(new TipUnLikeJob(mItems.get(position).getGlobalID()));
                    holder.nextPage.setTextColor(mContext.getResources().getColor(R.color.map_info_2));
                    holder.nextPage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_black_24dp, 0, 0, 0);
                    mItems.get(position).setLiked(false);
                } else {
                    jobManager.addJob(new TipLikeJob(mItems.get(position).getGlobalID()));
                    holder.nextPage.setTextColor(mContext.getResources().getColor(R.color.theme_primary));
                    holder.nextPage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_orange_50_24dp, 0, 0, 0);
                    mItems.get(position).setLiked(true);
                }

                /*
                if (callback != null) {
                    if ((position + 1) < getCount()) {
                        callback.onPageRequested(position + 1);
                    }
                }*/
            }
        });
        holder.share_pres.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ShareDialogUtils.sharePresentationDialog(mContext, holder.title.getText().toString(), mItems.get(position).getGlobalID() , FileUtils.getLocalBitmapUri(holder.mPhotoView), holder.mPhotoView);
            }
        });
        holder.firstPage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (callback != null) {
                        callback.onPageRequested(0);
                }
            }
        });
        holder.lastPage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (callback != null) {
                        callback.onPageRequested(getCount() - 1);
                }
            }
        });
        View.OnClickListener onclick = new View.OnClickListener() {
            public void onClick(View v) {
                dispatchPresentationCollectionIntent(mItems.get(position).getGlobalID() , mItems.get(position).getImage_url(), mItems.get(position).getTitle());

            }
        };

        holder.title.setOnClickListener(onclick);
        holder.description.setOnClickListener(onclick);
        holder.mPhotoView.setOnClickListener(onclick);

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dispatchPresentationCollectionIntent(mItems.get(position).getGlobalID(), mItems.get(position).getImage_url(), mItems.get(position).getTitle());
                return false;
            }
        });


        return convertView;
    }

    static class ViewHolder{
        TextView title;
        TextView description;
        ImageButton firstPage;
        ImageButton lastPage;
        Button share_pres;
        Button nextPage;
        private boolean mHasPhoto;
        private View mPhotoViewContainer;
        private ImageView mPhotoView;
        private View mHeaderBox;
        private View mDetailsContainer;
        private int mPhotoHeightPixels;
        private int mHeaderHeightPixels;
        private static final float PHOTO_ASPECT_RATIO = 1.7777777f;


    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.first_page:
                if(callback != null){
                    callback.onPageRequested(0);
                }
                break;
            case R.id.last_page:
                if(callback != null){
                    callback.onPageRequested(getCount()-1);
                }
                break;
        }
    }

    public void addItems(Tips presentation) {
        mItems.add(presentation);
        notifyDataSetChanged();
    }


    private void dispatchPresentationCollectionIntent(String globalId, String photoUrl, String title) {
        Intent collection = new Intent(mContext,ProductCollectionActivity.class);
        collection.putExtra(ProductCollectionActivity.SHOPICK_COLLECTION_ID, globalId);
        collection.putExtra(ProductCollectionActivity.SHOPICK_COLLECTION_PHOTO, photoUrl);
        collection.putExtra(ProductCollectionActivity.SHOPICK_COLLECTION_TITLE, title);
        collection.putExtra(ProductCollectionActivity.SHOPICK_COLLECTION_TIP, true);
        collection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Ensure that there's a camera activity to handle the intent
        mContext.startActivity(collection);

    }
    private void recomputePhotoAndScrollingMetrics(ViewHolder holder) {

        holder.mPhotoHeightPixels = 0;
        if (holder.mHasPhoto) {
            holder.mPhotoHeightPixels = (int) (holder.mPhotoView.getWidth() / holder.PHOTO_ASPECT_RATIO);
            holder.mPhotoHeightPixels = holder.mPhotoHeightPixels;
        }

        ViewGroup.LayoutParams lp;
        lp = holder.mPhotoViewContainer.getLayoutParams();
        if (lp.height != holder.mPhotoHeightPixels) {
            lp.height = holder.mPhotoHeightPixels;
            holder.mPhotoViewContainer.setLayoutParams(lp);
        }

        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)
                holder.mDetailsContainer.getLayoutParams();
        if (mlp.topMargin != holder.mHeaderHeightPixels + holder.mPhotoHeightPixels) {
            mlp.topMargin = holder.mHeaderHeightPixels + holder.mPhotoHeightPixels;
            holder.mDetailsContainer.setLayoutParams(mlp);
        }

    }

}