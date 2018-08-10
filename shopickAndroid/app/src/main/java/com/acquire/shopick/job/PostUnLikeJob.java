package com.acquire.shopick.job;

import com.acquire.shopick.Config;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.io.network.ShopickApi;
import com.acquire.shopick.model.ShopickPostModel;
import com.acquire.shopick.util.AccountUtils;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.otto.Bus;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by gaurav on 2/26/16.
 */
public class PostUnLikeJob extends Job {

    @Inject
    transient Bus eventBus;

    @Inject
    transient ShopickPostModel model;



    public String getglobalID() {
        return globalID;
    }

    public void setglobalID(String globalID) {
        this.globalID = globalID;
    }

    public PostUnLikeJob(String globalID_) {
        super(new Params(Priority.MID).groupBy("postunlike").persist().requireNetwork());

        this.globalID = globalID_;

    }

    String globalID;

    @Override
    public void onAdded() {
        Post last_post = model.load(globalID);
        if (last_post == null) {

        } else {
            last_post.setLiked(false);
            last_post.setDirty(true);
            model.delete(last_post);
            model.savePost(last_post);
        }
    }

    @Override
    public void onRun() throws Throwable {
        Post last_post = model.load(globalID);
        if (last_post == null) {
         unlikePost();
        }
        else if  (last_post.getDirty()) {
            unlikePost();
            last_post.setDirty(false);
            model.delete(last_post);
            model.savePost(last_post);
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


    public Response<Post> unlikePost() throws IOException {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(1000, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(1000, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(1000, TimeUnit.SECONDS);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        ShopickApi.FeedService service = retrofit.create(ShopickApi.FeedService.class);

        RequestBody requestGlobalID = RequestBody.create(MediaType.parse("text/*"), globalID);

        Call<Post> call = service.unlikePost(requestGlobalID, AccountUtils.getRequestMap(ShopickApplication.getInstance()));
        return call.execute();
    }


}
