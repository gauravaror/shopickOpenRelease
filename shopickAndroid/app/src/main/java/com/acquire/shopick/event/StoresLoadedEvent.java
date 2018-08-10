package com.acquire.shopick.event;

import com.acquire.shopick.dao.Store;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaurav on 10/8/15.
 */
public class StoresLoadedEvent {

    List<Store> stores;

    public boolean isLatAvailable() {
        return latAvailable;
    }

    boolean latAvailable;

    public StoresLoadedEvent(List<Store> storesLoaded, boolean latAvailable) {
        this.stores = storesLoaded;
        this.latAvailable = latAvailable;
    }

    public List<Store> getStores() {
        return stores;
    }
}
