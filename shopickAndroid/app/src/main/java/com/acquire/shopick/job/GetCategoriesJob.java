package com.acquire.shopick.job;

import com.acquire.shopick.Config;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.dao.Brands;
import com.acquire.shopick.dao.Categories;
import com.acquire.shopick.event.BrandsLoadedEvent;
import com.acquire.shopick.event.CategoriesLoadedEvent;
import com.acquire.shopick.event.StoredBrandsLoadedEvent;
import com.acquire.shopick.io.network.ShopickApi;
import com.acquire.shopick.model.ShopickBrandModel;
import com.acquire.shopick.model.ShopickCategoryModel;
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
public class GetCategoriesJob extends Job {

    @Inject
    transient Bus eventBus;



    @Inject
    transient ShopickCategoryModel model;


    public GetCategoriesJob() {
        super(new Params(Priority.HIGHEST).groupBy("GETBRANDS"));

    }
    @Override
    public void onAdded() {
        postEvent(new CategoriesLoadedEvent(getStoredCategories()));
    }

    @Override
    public void onRun() throws Throwable {
        ArrayList<Categories> categories_ = getCategories();
        model.saveCategories(categories_);
        postEvent(new CategoriesLoadedEvent(categories_));
    }

    private ArrayList<Categories> getStoredCategories() {

        return new ArrayList<Categories>(model.getAllCategories());
    }


    @Override
    protected void onCancel() {

    }


    private ArrayList<Categories> getCategories() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.CategoryService service = retrofit.create(ShopickApi.CategoryService.class);
        Map<String, String> option_feed = AccountUtils.getRequestMap(getApplicationContext());
        Call<ArrayList<Categories>> call;

        call = service.getCategories(option_feed);

        return call.execute().body();
    }


    void postEvent(Object new_class) {
        if (eventBus == null) {
            eventBus = BusProvider.getInstance();
        }
        eventBus.post(new_class);
    }

}
