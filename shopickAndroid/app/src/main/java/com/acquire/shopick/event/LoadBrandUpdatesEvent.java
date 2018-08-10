package com.acquire.shopick.event;

/**
 * Created by gaurav on 10/20/15.
 */
public class LoadBrandUpdatesEvent {
    public Long getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(Long brand_id) {
        this.brand_id = brand_id;
    }

    Long brand_id;

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    String filter;



    public LoadBrandUpdatesEvent(Long brand_id, String filter) {
        this.brand_id = brand_id;
        this.filter = filter;
    }
}
