package com.acquire.shopick.job;

import com.acquire.shopick.Config;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.event.AllOffersLoadedEvent;
import com.acquire.shopick.io.model.AllOffer;
import com.acquire.shopick.io.network.ShopickApi;
import com.acquire.shopick.util.AccountUtils;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.squareup.otto.Bus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by gaurav on 4/17/16.
 */
public class GetAllOffersJob  extends Job {

    @Inject
    transient Bus eventBus;
    Long category_id = -1L;
    int user_id;


    public GetAllOffersJob(int user_id, Long category_id) {
        super(new Params(Priority.HIGHEST).groupBy("GETALLOFFERS"));
        this.category_id = category_id;
        this.user_id = user_id;

    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        ArrayList<AllOffer> updates = getBrandUpdates();
        postEvent(new AllOffersLoadedEvent(updates));
    }

    @Override
    protected void onCancel() {

    }


    private ArrayList<AllOffer> getBrandUpdates() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.BannerService service = retrofit.create(ShopickApi.BannerService.class);
        Map<String, String> option_feed = AccountUtils.getRequestMap(getApplicationContext());
        Call<ArrayList<AllOffer>> call = service.getAllOffers(user_id, option_feed);
        return call.execute().body();
    }


    void postEvent(Object new_class) {
        if (eventBus == null) {
            eventBus = BusProvider.getInstance();
        }
        eventBus.post(new_class);
    }

}
