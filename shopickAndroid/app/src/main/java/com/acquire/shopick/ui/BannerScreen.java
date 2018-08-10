package com.acquire.shopick.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.acquire.shopick.Config;
import com.acquire.shopick.R;
import com.acquire.shopick.io.model.Banner;
import com.acquire.shopick.io.model.Intro;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.acquire.shopick.util.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.acquire.shopick.util.LogUtils.makeLogTag;

/**
 * Created by gaurav on 10/31/15.
 */
public class BannerScreen extends Fragment {


    private static String TAG = makeLogTag(BannerScreen.class);


    private String desc;
    private String title;
    private String intentUrl;
    private String globalID;
    private String imageUrl;
    private int drawable;

    private ImageLoader mPlaceholderImageLoader;


    @Bind(R.id.image_banner)
    ImageView imageView;

    @Bind(R.id.banner_background)
    FrameLayout bannerBackground;



    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    static BannerScreen newInstance(int num, Banner item,String filter) {
        BannerScreen f = new BannerScreen();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putLong("id", item.getId());
        args.putString("title", item.getTitle());
        args.putString("desc", item.getDescription());
        args.putString("imageUrl", item.getImageUrl());
        args.putString("globalID",item.getGlobalID());
        args.putString("intentUrl",item.getIntentUrl());
        f.setArguments(args);

        return f;
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments() != null ? getArguments().getString("title") : "title";
        imageUrl = getArguments() != null ? getArguments().getString("imageUrl") : "";
        intentUrl = getArguments() != null ? getArguments().getString("intentUrl") : "";
        globalID = getArguments() != null ? getArguments().getString("globalID") : "";
        desc = getArguments() != null ? getArguments().getString("desc") : "";

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRoot = inflater.inflate(R.layout.banner_item, container, false);
        ButterKnife.bind(this, mRoot);


        if (mPlaceholderImageLoader == null) {
            mPlaceholderImageLoader = new ImageLoader(getActivity().getApplicationContext());
        }
        if (!TextUtils.isEmpty(imageUrl) ) {
            mPlaceholderImageLoader.loadImage(Config.PROD_BASE_URL+imageUrl, imageView);
            imageView.setVisibility(View.VISIBLE);
            bannerBackground.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
            bannerBackground.setVisibility(View.GONE);
        }
        mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(intentUrl)) {
                    getActivity().getApplicationContext().
                            startActivity(DispatchIntentUtils.dispatchDeepLinkIntent(getActivity().getApplicationContext(), intentUrl).
                                    putExtra("collection_title", title));
                }
                AnalyticsManager.sendEvent(title,"BannerClicked",AccountUtils.getShopickTempProfileId(getActivity().getApplicationContext())+ "");
            }
        });
        return mRoot;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }




}
