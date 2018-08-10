package com.acquire.shopick.ui;

import android.animation.Animator;
import android.app.Fragment;
import android.media.Image;
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

public class IntoScreenFirstFragment extends Fragment {

    @Bind(R.id.image_view_intro)
    public ImageView introImage;

    @Bind(R.id.text_view_intro)
    public TextView introText;

    @Bind(R.id.fab_main_offer)
    public Button introFabOffer;

    @Bind(R.id.fab_main_collection)
    public Button introFabCollection;

    @Bind(R.id.fab_main_latest_launch)
    public Button introFabLatestLaunch;

    @Bind(R.id.example_collection)
    public LinearLayout exampleLayoutCollection;

    @Bind(R.id.example_offer)
    public LinearLayout exampleLayoutOffer;

    @Bind(R.id.example_latest_launch)
    public LinearLayout exampleLayoutLatestLaunch;

    @Bind(R.id.meta_post_title)
    TextView title;


    Timer timer;
    int banners = 0;
    int num_banners;
    private boolean sent = false;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRoot = inflater.inflate(R.layout.intro_screen_first_fragment, container, false);
        ButterKnife.bind(this, mRoot);

        introFabOffer.animate().rotationBy(360).setDuration(3000).setListener(animationListener).start();
        introFabCollection.animate().setStartDelay(3000).setDuration(3000).setListener(animationListener).rotationBy(360).start();
        introFabLatestLaunch.animate().setStartDelay(6000).rotationBy(360).setListener(animationListener).setDuration(3000).start();
        AnalyticsManager.sendEvent("INTRO_SCREEN","OPENED","FIRST");
        title.setText(FeedUtils.getFeedTitle(getActivity().getApplicationContext(),"Abhinav ",1L," Shirt","Zara Pacific Mall",1L,"",1));
        return mRoot;
    }

    @OnClick(R.id.fab_main_offer)
    public void OnClickOffer(View view) {
        if (getActivity() != null) {
            exampleLayoutOffer.setVisibility(View.VISIBLE);
            exampleLayoutCollection.setVisibility(View.GONE);
            exampleLayoutLatestLaunch.setVisibility(View.GONE);
            introText.setText(R.string.offfer_desc);
        }
        AnalyticsManager.sendEvent("INTRO_SCREEN","CLICKED","OFFER_INTRO");

    }

    @OnClick(R.id.fab_main_latest_launch)
    public void OnClickLaunch(View view) {
        if (getActivity() != null) {
            exampleLayoutOffer.setVisibility(View.GONE);
            exampleLayoutCollection.setVisibility(View.GONE);
            exampleLayoutLatestLaunch.setVisibility(View.VISIBLE);
         //   introImage.setImageDrawable(getResources().getDrawable(R.drawable.latest_launch_image_tab));
            introText.setText(R.string.latest_launch_desc);
        }
        AnalyticsManager.sendEvent("INTRO_SCREEN","CLICKED","LATEST_LAUNCH_INTRO");
    }

    @OnClick(R.id.fab_main_collection)
    public void OnClickCollection(View view) {
        if (getActivity() != null) {
            exampleLayoutOffer.setVisibility(View.GONE);
            exampleLayoutCollection.setVisibility(View.VISIBLE);
            exampleLayoutLatestLaunch.setVisibility(View.GONE);
       //     introImage.setImageDrawable(getResources().getDrawable(R.drawable.collection_tab_image));
            introText.setText(R.string.collection_desc);
        }
        AnalyticsManager.sendEvent("INTRO_SCREEN","CLICKED","COLLECTION_INTRO");
    }


    Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            if (introFabOffer.getVisibility() == View.INVISIBLE) {
                introFabOffer.setVisibility(View.VISIBLE);
                OnClickOffer(introFabOffer);
            } else if (introFabCollection.getVisibility() == View.INVISIBLE ) {
                introFabCollection.setVisibility(View.VISIBLE);
                OnClickCollection(introFabCollection);
            } else {
                introFabOffer.setVisibility(View.VISIBLE);
                introFabCollection.setVisibility(View.VISIBLE);
                introFabLatestLaunch.setVisibility(View.VISIBLE);
                OnClickLaunch(introFabLatestLaunch);
            }

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }

    };

}
