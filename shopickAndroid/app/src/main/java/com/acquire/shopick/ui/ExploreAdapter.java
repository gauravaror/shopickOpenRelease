package com.acquire.shopick.ui;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.acquire.shopick.Config;
import com.acquire.shopick.R;
import com.acquire.shopick.dao.Brands;
import com.acquire.shopick.util.ImageLoader;
import com.acquire.shopick.util.UIUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.acquire.shopick.util.LogUtils.LOGV;
import static com.acquire.shopick.util.LogUtils.makeLogTag;

/**
 * Created by gaurav on 9/24/15.
 */
public class ExploreAdapter extends FragmentStatePagerAdapter  {

    private static final String TAG = makeLogTag(ExploreAdapter.class);
    // additional top padding to add to first item of list
    int mContentTopClearance = 0;
    private final Context mContext;
    private ImageLoader mNoPlaceholderImageLoader;
    // list of items served by this adapter
    String filter = "";
    ArrayList<Brands> mItems = new ArrayList<Brands>();
    Long category_id = -1L;

    // observers to notify about changes in the data
    ArrayList<DataSetObserver> mObservers = new ArrayList<DataSetObserver>();


    public ExploreAdapter(Context context,FragmentManager mgr) {
        super(mgr);
        mContext = context;
    }


    public void setContentTopClearance(int padding) {
        mContentTopClearance = padding;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Fragment getItem(int position) {
        LOGV(TAG," with filter :"+filter);
        int pos =  position >= 0 && position < mItems.size() ? (position) : null;
        return ExploreItemFragment.newInstance(pos, mItems.get(position), filter, category_id);
    }


    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    @Override
    public String getPageTitle(int position) {
        return mItems.get(position).getName();
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    public static Drawable drawableFromUrl(String url) throws IOException {
        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(x);
    }
    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    public View setTabView(int position, boolean selected) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.tab_display_item, null);
        TextView tv = (TextView) v.findViewById(R.id.text_tab_item);
        ImageView im = (ImageView) v.findViewById(R.id.image_tab_item);
        Brands currentBrand = mItems.get(position);
        int mDefaultSessionColor = mContext.getResources().getColor(R.color.theme_primary_dark);
        int darkSessionColor = UIUtils.scaleSessionColorToDefaultBG(mDefaultSessionColor);
        if(mNoPlaceholderImageLoader == null)
            mNoPlaceholderImageLoader = new ImageLoader(mContext);
        //tv.setText(currentBrand.name);
        mNoPlaceholderImageLoader.loadImage(Config.PROD_BASE_URL+currentBrand.getLogo_url(),im);
        //tv.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
        //tv.getBackground().setColorFilter(UIUtils.makeSessionImageScrimColorFilter(darkSessionColor));

        return v;
    }
}
