package com.acquire.shopick.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.acquire.shopick.R;
import com.acquire.shopick.io.model.Brand;
import com.acquire.shopick.io.model.Intro;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.acquire.shopick.util.ImageUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.acquire.shopick.util.LogUtils.makeLogTag;

/**
 * Created by gaurav on 10/31/15.
 */
public class IntroScreen extends Fragment {


    private static String TAG = makeLogTag(IntroScreen.class);


    private String desc;
    private String title;
    private int drawable;


    @Bind(R.id.intro_screen_background)  FrameLayout mBackground;
    @Bind(R.id.intro_screen_title)  TextView mTitle;
    @Bind(R.id.intro_screen_desc)  TextView mDesc;
    @Bind(R.id.image_banner_intro) ImageView imageView;



    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    static IntroScreen newInstance(int num, Intro item,String filter) {
        IntroScreen f = new IntroScreen();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("id", item.id);
        args.putString("title", item.title);
        args.putString("desc", item.desc);
        args.putInt("photoUrl",item.drawable);
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
        drawable = getArguments() != null ? getArguments().getInt("photoUrl") : -1;
        desc = getArguments() != null ? getArguments().getString("desc") : "";

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRoot = inflater.inflate(R.layout.intro_screens_item, container, false);
        ButterKnife.bind(this, mRoot);
        imageView.setImageBitmap(ImageUtil.decodeSampledBitmapFromResource(getResources(), drawable, 150, 100));
        mTitle.setText(title);
        mDesc.setText(desc);
        if (TextUtils.isEmpty(title))
            mTitle.setVisibility(View.GONE);
        if (TextUtils.isEmpty(desc))
            mTitle.setVisibility(View.GONE);

        return mRoot;
    }



    @OnClick(R.id.skip_button)
    public void skipButtonClick(View click) {
        startActivity(DispatchIntentUtils.dispatchLikedBrandntent(getActivity().getApplicationContext()));
        getActivity().finish();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }




}
