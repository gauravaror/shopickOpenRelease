package com.acquire.shopick.job;

import android.content.Context;
import android.graphics.Bitmap;

import com.acquire.shopick.Config;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.io.model.FindThis;
import com.acquire.shopick.io.model.RedeemPick;
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
import retrofit.Retrofit;

/**
 * Created by gaurav on 3/3/16.
 */
public class UpdatePhoneNumberJob extends Job {

    @Inject
    transient Bus eventBus;

    String globalID;
    Long post_id;
    Long product_id;
    Long redeem_id;
    Long banner_id;
    Long post_collection_id;
    boolean post_;
    boolean redeem_;
    boolean banner_;
    boolean post_collection_;
    double lat;
    double lon;

    public UpdatePhoneNumberJob(String globalID, Long post_id, Long product_id, Long redeem_id_, Long banner_id_, Long post_collection_id_, boolean banner, boolean redeem, boolean post_, boolean post_collection_, int user_id, String phoneno_, double lat, double lon) {
        super(new Params(Priority.MID).groupBy("tiplike").persist().requireNetwork());
        this.eventBus = eventBus;
        this.globalID = globalID;
        this.post_id = post_id;
        this.product_id = product_id;
        this.banner_id = banner_id_;
        this.post_collection_id = post_collection_id_;
        this.banner_ = banner;
        this.post_ = post_;
        this.post_collection_ = post_collection_;
        this.redeem_ = redeem;
        this.user_id = user_id;
        this.phoneno_ = phoneno_;
        this.redeem_id = redeem_id_;
        this.lat = lat;
        this.lon = lon;
    }

    int user_id;
    String phoneno_;


    @Override
    public void onAdded() {


    }

    @Override
    public void onRun() throws Throwable {
        send_find_post();

    }

    @Override
    protected void onCancel() {

    }

    private void send_find_post() throws IOException {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(1000, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(1000, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(1000, TimeUnit.SECONDS);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        ShopickApi.FindService service = retrofit.create(ShopickApi.FindService.class);

        RequestBody phoneno = RequestBody.create(MediaType.parse("text/*"), phoneno_);
        RequestBody globalID_ = RequestBody.create(MediaType.parse("text/*"), globalID);
        Call<FindThis> call;
        if (redeem_) {
            Call<RedeemPick> call_;
            ShopickApi.PicksService service_ = retrofit.create(ShopickApi.PicksService.class);

            call_ = service_.userRedeemProduct(globalID_, redeem_id, user_id, phoneno_, lat, lon
                    , AccountUtils.getRequestMap(ShopickApplication.getInstance()));
            call_.execute();
        } else if (banner_) {
            call = service.findBanner(globalID_, banner_id, user_id, phoneno_, lat, lon
                    , AccountUtils.getRequestMap(ShopickApplication.getInstance()));
            call.execute();

        } else if (post_collection_) {
            call = service.findPostCollection(globalID_, post_collection_id, user_id, phoneno_, lat, lon
                    , AccountUtils.getRequestMap(ShopickApplication.getInstance()));
            call.execute();
        }else  if (post_) {
            call = service.findPost(globalID_, post_id, user_id, phoneno_, lat, lon
                    , AccountUtils.getRequestMap(ShopickApplication.getInstance()));
            call.execute();

        } else {
         call = service.findProduct(globalID_, product_id, user_id, phoneno_, lat, lon
                    , AccountUtils.getRequestMap(ShopickApplication.getInstance()));
            call.execute();

        }
        return;
    }

}
