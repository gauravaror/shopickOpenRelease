package com.acquire.shopick.job;

import com.acquire.shopick.Config;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.dao.Brands;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.event.BrandsLoadedEvent;
import com.acquire.shopick.event.FeedLoadedEvent;
import com.acquire.shopick.event.LocalFeedLoadedEvent;
import com.acquire.shopick.event.StoredBrandsLoadedEvent;
import com.acquire.shopick.io.network.ShopickApi;
import com.acquire.shopick.model.ShopickBrandModel;
import com.acquire.shopick.model.ShopickPostModel;
import com.acquire.shopick.util.AccountUtils;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Bus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by gaurav on 12/5/15.
 */
public class GetBrandsJob extends Job {

    @Inject
    transient Bus eventBus;
    Long brand_id = -1L;
    Long category_id = -1L;
    int user_id;


    @Inject
    transient ShopickBrandModel model;


    public GetBrandsJob(int user_id, Long category_id, Long brand_id) {
        super(new Params(Priority.HIGHEST).groupBy("GETBRANDS"));
        this.category_id = category_id;
        this.user_id =  user_id;
        this.brand_id =  brand_id;

    }
    @Override
    public void onAdded() {
        postEvent(new StoredBrandsLoadedEvent(getStoredBrands()));
    }

    @Override
    public void onRun() throws Throwable {
        ArrayList<Brands> brands = getBrands();
        model.saveBrands(brands);
        postEvent(new BrandsLoadedEvent(brands));
    }

    private ArrayList<Brands> getStoredBrands() {
        List<Brands> local_list = null;
        if (brand_id == -1L  && category_id == -1L) {
            local_list = model.getAllBrands();
        } else if (brand_id != -1L) {
            local_list = model.getBrandBrands(brand_id);
        }  else if (category_id != -1L) {
            local_list = model.getCategoryBrands(category_id);
        }  else {
            local_list = model.getAllBrands();
        }

        return new ArrayList<Brands>(local_list);
    }


    @Override
    protected void onCancel() {

    }


    private ArrayList<Brands> getBrands() throws IOException {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(1000, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(1000, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(1000, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        ShopickApi.BrandsService service = retrofit.create(ShopickApi.BrandsService.class);
        Map<String, String> option_feed = AccountUtils.getRequestMap(getApplicationContext());
        Call<ArrayList<Brands>> call;
        if (user_id == -10) {
            call = service.getAllBrands( option_feed);
        } else if (brand_id == -1L  && category_id == -1L) {
            call = service.getBrands(user_id, option_feed);
        } else if (brand_id != -1L) {
            call = service.getBrands(user_id, brand_id, option_feed);
        }  else if (category_id != -1L) {
            call = service.getBrandsCat(user_id, category_id, option_feed);
        }  else {
            call = service.getBrands(user_id, option_feed);
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
