package com.acquire.shopick.event;

import com.acquire.shopick.dao.BrandUpdates;

import java.util.ArrayList;

/**
 * Created by gaurav on 6/20/16.
 */

public class FeaturedBrandUpdatesLoadedEvent {

    public FeaturedBrandUpdatesLoadedEvent(ArrayList<BrandUpdates> updates) {
        this.updates = updates;
    }

    public ArrayList<BrandUpdates> getUpdates() {
        return updates;
    }

    public void setUpdates(ArrayList<BrandUpdates> updates) {
        this.updates = updates;
    }

    ArrayList<BrandUpdates> updates;
}
