package com.acquire.shopick.event;

import com.acquire.shopick.dao.Brands;
import com.acquire.shopick.io.model.Brand;
import com.acquire.shopick.io.model.Presentation;

import java.util.ArrayList;

/**
 * Created by gaurav on 10/17/15.
 */
public class BrandsLoadedEvent {
    ArrayList<Brands> presentations;

    public BrandsLoadedEvent(ArrayList<Brands> brandsLoaded) {
        this.presentations = brandsLoaded;
    }

    public ArrayList<Brands> getBrands() {
        return presentations;
    }
}
