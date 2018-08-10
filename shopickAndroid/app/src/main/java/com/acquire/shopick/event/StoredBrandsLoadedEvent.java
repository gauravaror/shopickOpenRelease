package com.acquire.shopick.event;

import com.acquire.shopick.dao.Brands;

import java.util.ArrayList;

/**
 * Created by gaurav on 10/17/15.
 */
public class StoredBrandsLoadedEvent {
    ArrayList<Brands> presentations;

    public StoredBrandsLoadedEvent(ArrayList<Brands> brandsLoaded) {
        this.presentations = brandsLoaded;
    }

    public ArrayList<Brands> getBrands() {
        return presentations;
    }
}
