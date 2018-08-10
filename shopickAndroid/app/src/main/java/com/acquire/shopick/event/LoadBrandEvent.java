package com.acquire.shopick.event;

/**
 * Created by gaurav on 10/17/15.
 */
public class LoadBrandEvent {
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    int user_id;

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    String filter;

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    int brand_id;

    public LoadBrandEvent(int user_id,String filter, int brand_id) {
        this.user_id = user_id;
        this.filter = filter;
        this.brand_id = brand_id;
    }

}
