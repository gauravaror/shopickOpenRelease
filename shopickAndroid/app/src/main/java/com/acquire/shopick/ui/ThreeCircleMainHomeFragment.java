package com.acquire.shopick.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.acquire.shopick.R;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.dao.BrandUpdates;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.dao.PostCollection;
import com.acquire.shopick.event.FeedLoadedEvent;
import com.acquire.shopick.event.LoadTTopBrandUpdatesEvent;
import com.acquire.shopick.event.LoadTopPostCollectionEvent;
import com.acquire.shopick.event.TopBrandUpdatesLoadedEvent;
import com.acquire.shopick.event.TopPostCollectionLoaded;
import com.acquire.shopick.job.GetPostsJob;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.DispatchIntentUtils;
import com.acquire.shopick.util.FeedUtils;
import com.acquire.shopick.util.ImageLoader;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.acquire.shopick.util.LogUtils.makeLogTag;

/**
 * Created by gaurav on 9/24/15.
 */
public class ThreeCircleMainHomeFragment extends Fragment {

    private static String TAG = makeLogTag(ThreeCircleMainHomeFragment.class);
    String title;
    private Context mContext;


    @Bind(R.id.mywidget)
    public TextView offer;

    @Bind(R.id.mywidget_collection)
    public TextView collection;

    @Bind(R.id.mywidget_launch)
    public TextView launch;

    @Bind(R.id.post_button)
    public Button post_button;

    @Inject
    JobManager jobManager;

    private ArrayList<Post> posts;
    private ArrayList<BrandUpdates> brandUpdates;
    private ArrayList<PostCollection> postCollections;

    private ImageLoader imageLoader;

    private Bus mBus;


    Timer[] timeroffer =  new Timer[3];
    int banners = 0;
    int current = 0;
    int num_banners = 6;
    private boolean sent = false;

    public void pageSwitcher(int seconds, int type) {
        if(timeroffer[type] != null) {
            timeroffer[type].cancel();
        }
        timeroffer[type] = new Timer(); // At this line a new Thread will be created
        Context activity = getActivity();
        timeroffer[type].scheduleAtFixedRate(new RemindTask(type), 0, seconds * 1000); // delay
    }

    // this is an inner class...
    class RemindTask extends TimerTask {
        int type = 0;

        RemindTask(int ty) {
            type = ty;
        }

        @Override
        public void run() {

            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        if (current < num_banners - 1) {
                            current++;
                        } else {
                            current = 0;
                        }
                        if (type == 0) {
                            if (postCollections != null && postCollections.size() > 5) {
                                offer.setText(FeedUtils.getPostCollectionTitle(mContext, postCollections.get(current)));

                                //imageLoader.loadImage(postCollections.get(current).getPost_banner(), layout_offer);
                            }

                        } else if (type == 1) {
                            if (posts != null && posts.size() > 5) {
                                collection.setText(FeedUtils.getFeedTitle(mContext, posts.get(current)));
                            }

                        } else if (type == 2) {
                            if (brandUpdates != null && brandUpdates.size() > 5) {
                                launch.setText(FeedUtils.getBrandUpdateTitle(mContext, brandUpdates.get(current)));
                            }
                        }
                    }
                });

            }
        }
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        imageLoader =  new ImageLoader(mContext);
    }


    @OnClick(R.id.post_button)
    public void PostClick() {
        startActivity(DispatchIntentUtils.dispatchPostIntent(mContext),
                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity()).toBundle());
        AnalyticsManager.sendEvent("THREECIRCLEBSCREEN","CLICKED","POST");
    }


    @OnClick({R.id.fab_main_offer, R.id.card_view_offer})
    public void offerClick() {
        startActivity(DispatchIntentUtils.dispatchMetaPostsIntent(mContext),
                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity()).toBundle());
        AnalyticsManager.sendEvent("THREECIRCLEBSCREEN","CLICKED","OFFER");
    }

    @OnClick({R.id.fab_main_collection, R.id.card_view_collection})
    public void collectionClick() {
        startActivity(DispatchIntentUtils.openFeedMainFragment(mContext),
                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity()).toBundle());
        AnalyticsManager.sendEvent("THREECIRCLEBSCREEN","CLICKED","COLLECTION");
    }

    @OnClick({R.id.fab_main_latest_launch,R.id.card_view_launch})
    public void latestLaunchClick() {
        startActivity(DispatchIntentUtils.openLatestLaunch(mContext),
                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity()).toBundle());
        AnalyticsManager.sendEvent("THREECIRCLEBSCREEN","CLICKED","LATEST_LAUNCH");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRoot = inflater.inflate(R.layout.three_circle_main_fragment, container, false);
        ButterKnife.bind(this,mRoot);
        ShopickApplication.injectMembers(this);
        offer = (TextView) mRoot.findViewById(R.id.mywidget);
        collection = (TextView) mRoot.findViewById(R.id.mywidget_collection);
        launch = (TextView) mRoot.findViewById(R.id.mywidget_launch);
        mBus = BusProvider.getInstance();
        sendEvents();
        return mRoot;
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
        sendEvents();
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
        pauseTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        pauseTimer();
    }

    private void pauseTimer() {
        if (timeroffer != null && timeroffer[0] != null && timeroffer[1] != null && timeroffer[2] != null) {
            timeroffer[0].cancel();
            timeroffer[1].cancel();
            timeroffer[2].cancel();
        }
    }
    @Subscribe
    public void OnFeedLoaded(FeedLoadedEvent event) {
        if ( event.getFeed() != null && event.getFeed().size() > 0) {
            posts = (ArrayList<Post>) event.getFeed();
            pageSwitcher(5, 1);
        }

    }

    @Subscribe
    public void OnTopBrandUpdatesLoaded(TopBrandUpdatesLoadedEvent event) {
        if ( event.getUpdates() != null && event.getUpdates().size() > 0) {
            brandUpdates = (ArrayList<BrandUpdates>) event.getUpdates();
            pageSwitcher(5, 2);
        }

    }

    @Subscribe
    public void OnTopPostCollectionLoaded(TopPostCollectionLoaded event) {
        if ( event.getPostCollections() != null && event.getPostCollections().size() > 0) {
            postCollections = (ArrayList<PostCollection>) event.getPostCollections();
            pageSwitcher(5, 0);
        }

    }

    void sendEvents() {
        jobManager.addJob(new GetPostsJob(AccountUtils.getShopickProfileId(mContext), -1L, -1, -1L));
        mBus.post(new LoadTopPostCollectionEvent());
        mBus.post(new LoadTTopBrandUpdatesEvent());
    }

}

