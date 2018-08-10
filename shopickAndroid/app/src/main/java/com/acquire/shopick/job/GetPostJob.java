package com.acquire.shopick.job;

import com.acquire.shopick.Config;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.event.PostLoadedEvent;
import com.acquire.shopick.io.network.ShopickApi;
import com.acquire.shopick.model.ShopickPostModel;
import com.acquire.shopick.util.AccountUtils;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
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
public class GetPostJob extends Job {

    @Inject
    transient Bus eventBus;

    @Inject
    transient ShopickPostModel model;



    public String getPost_local_id() {
        return post_id;
    }

    public void setPost_local_id(String post_id) {
        this.post_id = post_id;
    }

    public GetPostJob(String post_local_id) {
        super(new Params(Priority.MID).groupBy("get local post"));

        this.post_id = post_local_id;

    }

    String post_id;

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        Post last_post = model.load(post_id);
        if (last_post != null) {
            postEvent(new PostLoadedEvent(last_post));
        } else {
            last_post = getPostExist(post_id);
            postEvent(new PostLoadedEvent(last_post));
        }
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

    public Post getPostExist(String globalID) throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ShopickApi.FeedService service = retrofit.create(ShopickApi.FeedService.class);


       // RequestBody globalIDRequest = RequestBody.create(MediaType.parse("text/*"), globalID);

        Call<Post> call = service.getPostExist(globalID, AccountUtils.getRequestMap(ShopickApplication.getInstance()));
        return call.execute().body();
    }


}
