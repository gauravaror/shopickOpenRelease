package com.acquire.shopick.ui;

import android.animation.Animator;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acquire.shopick.R;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.ui.widget.BezelImageView;
import com.acquire.shopick.ui.widget.SimpleDividerItemDecoration;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.FeedUtils;
import com.acquire.shopick.util.ImageLoader;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaurav on 7/9/16.
 */

public class IntroScreenSecondFragment extends Fragment {

    @Bind(R.id.poster_image)
    BezelImageView person_image;

    @Bind(R.id.meta_post_title)
    TextView title;

    @Bind(R.id.poster_photo_colored)
    ImageView main_image;

    @Bind(R.id.intro_screen_title)
    TextView desc_title;

    @Bind(R.id.intro_screen_desc)
    TextView desc_desc;

    @Bind(R.id.find_post)
    Button find_this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRoot = inflater.inflate(R.layout.intro_screen_second_fragment, container, false);
        ButterKnife.bind(this, mRoot);
        title.setText(FeedUtils.getFeedTitle(getActivity().getApplicationContext(),"Abhinav ",1L," Shirt","Zara Pacific Mall",1L,"",1));
        AnalyticsManager.sendEvent("INTRO_SCREEN","OPENED","SECOND");
        return mRoot;
    }


}
