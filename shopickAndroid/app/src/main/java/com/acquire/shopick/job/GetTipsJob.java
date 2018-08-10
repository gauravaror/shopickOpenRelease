package com.acquire.shopick.job;

import com.acquire.shopick.Config;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.dao.BrandUpdates;
import com.acquire.shopick.dao.Tips;
import com.acquire.shopick.event.BrandUpdatesLoadedEvent;
import com.acquire.shopick.event.PresentationsLoadedEvent;
import com.acquire.shopick.event.StoredBrandUpdatesLoadedEvent;
import com.acquire.shopick.event.StoredPresentationsLoadedEvent;
import com.acquire.shopick.io.network.ShopickApi;
import com.acquire.shopick.model.ShopickBrandUpdatesModel;
import com.acquire.shopick.model.ShopickTipsModel;
import com.acquire.shopick.util.AccountUtils;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.squareup.otto.Bus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by gaurav on 12/5/15.
 */
public class GetTipsJob extends Job {

    @Inject
    transient Bus eventBus;
    Long category_id = -1L;
    int user_id;

    @Inject
    transient ShopickTipsModel model;


    public GetTipsJob(int user_id, Long category_id) {
        super(new Params(Priority.HIGHEST).groupBy("GETTIPS"));
        this.category_id = category_id;
        this.user_id = user_id;

    }
    @Override
    public void onAdded() {
        postEvent(new StoredPresentationsLoadedEvent(getStoredBrandUpdates()));
    }

    @Override
    public void onRun() throws Throwable {
        ArrayList<Tips> updates = getBrandUpdates();
        model.saveTips(updates);
        postEvent(new PresentationsLoadedEvent(updates));
    }

    private ArrayList<Tips> getStoredBrandUpdates() {
        List<Tips> local_list = model.getAllTips();
        if ( category_id != -1L) {
            local_list = model.getCategoryTips(category_id);
        }

        return new ArrayList<Tips>(local_list);
    }


    @Override
    protected void onCancel() {

    }


    private ArrayList<Tips> getBrandUpdates() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.PresentationsService service = retrofit.create(ShopickApi.PresentationsService.class);
        Map<String, String> option_feed = AccountUtils.getRequestMap(getApplicationContext());
        Call<ArrayList<Tips>> call = service.getPresentations(user_id, option_feed);

        if ( category_id != -1L) {
            call = service.getPresentations(user_id, category_id, option_feed);
        }
        return call.execute().body();
    }


    void postEvent(Object new_class) {
        if (eventBus == null) {
            eventBus = BusProvider.getInstance();
        }
        eventBus.post(new_class);
    }

}
