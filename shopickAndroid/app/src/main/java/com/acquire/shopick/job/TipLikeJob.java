package com.acquire.shopick.job;

import com.acquire.shopick.Config;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.io.network.ShopickApi;
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
public class TipLikeJob extends Job {

    @Inject
    transient Bus eventBus;

    public String getglobalID() {
        return globalID;
    }

    public void setglobalID(String globalID) {
        this.globalID = globalID;
    }

    public TipLikeJob(String globalID) {
        super(new Params(Priority.MID).groupBy("tiplike").persist().requireNetwork());
        this.globalID = globalID;
    }

    String globalID;

    @Override
    public void onAdded() {
    }

    @Override
    public void onRun() throws Throwable {
            likeTip();
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


    public Response<Post> likeTip() throws IOException {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(1000, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(1000, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(1000, TimeUnit.SECONDS);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        ShopickApi.PresentationsService service = retrofit.create(ShopickApi.PresentationsService.class);

        RequestBody requestGlobalID = RequestBody.create(MediaType.parse("text/*"), globalID);

        Call<Post> call = service.likeTip(requestGlobalID, AccountUtils.getRequestMap(ShopickApplication.getInstance()));
        return call.execute();
    }


}
