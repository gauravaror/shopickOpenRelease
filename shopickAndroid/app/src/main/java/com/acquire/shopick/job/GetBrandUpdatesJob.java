package com.acquire.shopick.job;

import com.acquire.shopick.Config;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.dao.BrandUpdates;
import com.acquire.shopick.dao.Brands;
import com.acquire.shopick.event.BrandUpdatesLoadedEvent;
import com.acquire.shopick.event.BrandsLoadedEvent;
import com.acquire.shopick.event.StoredBrandUpdatesLoadedEvent;
import com.acquire.shopick.event.StoredBrandsLoadedEvent;
import com.acquire.shopick.io.network.ShopickApi;
import com.acquire.shopick.model.ShopickBrandModel;
import com.acquire.shopick.model.ShopickBrandUpdatesModel;
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
public class GetBrandUpdatesJob extends Job {

    @Inject
    transient Bus eventBus;
    Long brand_id = -1L;
    Long category_id = -1L;


    @Inject
    transient ShopickBrandUpdatesModel model;


    public GetBrandUpdatesJob( Long category_id, Long brand_id) {
        super(new Params(Priority.HIGHEST).groupBy("GETBRANDUPDATES"));
        this.category_id = category_id;
        this.brand_id =  brand_id;

    }
    @Override
    public void onAdded() {
        postEvent(new StoredBrandUpdatesLoadedEvent(getStoredBrandUpdates(), brand_id));
    }

    @Override
    public void onRun() throws Throwable {
        ArrayList<BrandUpdates> updates = getBrandUpdates();
        model.saveUpdates(updates);
        postEvent(new BrandUpdatesLoadedEvent(updates, brand_id));
    }

    private ArrayList<BrandUpdates> getStoredBrandUpdates() {
        List<BrandUpdates> local_list = null;
        if (brand_id == -1L  && category_id == -1L) {
            local_list = null;
        } else if (brand_id != -1L && category_id == -1L) {
            local_list = model.getBrandBrandUpdates(brand_id);
        }  else if (brand_id != -1L && category_id != -1L) {
            local_list = model.getCategoryBrandUpdates(category_id);
        }  else {
            local_list = null;
        }

        return new ArrayList<BrandUpdates>(local_list);
    }


    @Override
    protected void onCancel() {

    }


    private ArrayList<BrandUpdates> getBrandUpdates() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.UpdatesService service = retrofit.create(ShopickApi.UpdatesService.class);
        Map<String, String> option_feed = AccountUtils.getRequestMap(getApplicationContext());
        Call<ArrayList<BrandUpdates>> call = null;

        if (brand_id != -1L && category_id == -1L) {
            call = service.getBrandUpdates(brand_id, option_feed);
        }  else if (brand_id != -1L && category_id != -1L) {
            call = service.getBrandUpdatesCat(brand_id, category_id, option_feed);
        }
        if (call == null ) {
            return null;
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
