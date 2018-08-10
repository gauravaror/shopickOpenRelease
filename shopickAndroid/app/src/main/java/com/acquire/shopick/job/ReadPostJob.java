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
 * Created by gaurav on 5/17/16.
 */
public class ReadPostJob extends Job {

    @Inject
    transient Bus eventBus;

    @Inject
    transient ShopickPostModel model;

    public ReadPostJob(String globalID) {
        super(new Params(Priority.LOW).groupBy("READPOST"));
        this.globalID = globalID;
    }

    public String getGlobalID() {
        return globalID;
    }

    public void setGlobalID(String globalID) {
        this.globalID = globalID;
    }

    String globalID;

    @Override
    public void onAdded() {
        Post old = model.load(globalID);
        old.setRead(true);
        model.deleteById(globalID);
        model.savePost(old);

    }

    @Override
    public void onRun() throws Throwable {
        readPost();

    }

    @Override
    protected void onCancel() {

    }


    public Response<Post> readPost() throws IOException {
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

        Call<Post> call = service.readPost(requestGlobalID, AccountUtils.getRequestMap(ShopickApplication.getInstance()));
        return call.execute();
    }


}
