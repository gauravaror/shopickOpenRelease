package com.acquire.shopick.event;

import com.acquire.shopick.dao.BrandUpdates;

import java.util.ArrayList;

/**
 * Created by gaurav on 10/20/15.
 */
public class StoredBrandUpdatesLoadedEvent {

    ArrayList<BrandUpdates> updates;

    public Long getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(Long brand_id) {
        this.brand_id = brand_id;
    }

    Long brand_id;

    public StoredBrandUpdatesLoadedEvent(Long brand_id) {
        this.brand_id = brand_id;
    }

    public StoredBrandUpdatesLoadedEvent(ArrayList<BrandUpdates> updatesLoaded, Long brand_id) {
        this.updates = updatesLoaded;
        this.brand_id =  brand_id;
    }

    public ArrayList<BrandUpdates> getUpdates() {
        return updates;
    }

}
