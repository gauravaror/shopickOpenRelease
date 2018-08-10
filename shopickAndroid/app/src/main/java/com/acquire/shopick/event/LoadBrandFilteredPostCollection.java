package com.acquire.shopick.event;

/**
 * Created by gaurav on 7/9/16.
 */

public class LoadBrandFilteredPostCollection {
    public Long getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(Long brand_id) {
        this.brand_id = brand_id;
    }

    public LoadBrandFilteredPostCollection(Long brand_id) {
        this.brand_id = brand_id;
    }

    Long brand_id;
}
