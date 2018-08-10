package com.acquire.shopick.job;

import com.acquire.shopick.Config;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.event.PostLoadedEvent;
import com.acquire.shopick.event.SimilarPostLoadedEvent;
import com.acquire.shopick.io.network.ShopickApi;
import com.acquire.shopick.model.ShopickPostModel;
import com.acquire.shopick.util.AccountUtils;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.squareup.otto.Bus;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by gaurav on 12/5/15.
 */
public class GetSimilarPostJob extends Job {

    @Inject
    transient Bus eventBus;

    @Inject
    transient ShopickPostModel model;


    public GetSimilarPostJob(Post ref_post) {
        super(new Params(Priority.HIGH).groupBy("get local post"));
        post =  ref_post;
    }


    Post post;

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        ArrayList<Post> similarPosts = getSimilarPosts();
        postEvent(new SimilarPostLoadedEvent(similarPosts));
    }

    @Override
    protected void onCancel() {

    }


    void postEvent(Object new_class) {
        if (eventBus == null) {
            eventBus = BusProvider.getInstance();
        }
        eventBus.post(new_class);
    }

    public ArrayList<Post> getSimilarPosts() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ShopickApi.FeedService service = retrofit.create(ShopickApi.FeedService.class);
        Call<ArrayList<Post>> call = service.getSimilarPost(post.getCategory_id(), post.getStore_id(), post.getPost_type(), AccountUtils.getRequestMap(ShopickApplication.getInstance()));
        return call.execute().body();
    }


}
