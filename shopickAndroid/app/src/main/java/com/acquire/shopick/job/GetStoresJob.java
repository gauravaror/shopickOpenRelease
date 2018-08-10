package com.acquire.shopick.job;

import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.dao.Store;
import com.acquire.shopick.event.StoresLoadedEvent;
import com.acquire.shopick.model.ShopickStoreModel;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.squareup.otto.Bus;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by gaurav on 12/5/15.
 */
public class GetStoresJob extends Job {

    @Inject
    transient Bus eventBus;

    @Inject
    transient ShopickStoreModel model;



    public GetStoresJob() {
        super(new Params(Priority.HIGH).groupBy("GETSTORES"));

    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        List<Store> storeList = model.getAllStores();
        postEvent(new StoresLoadedEvent(storeList, false));
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

}
