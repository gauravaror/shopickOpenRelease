package com.acquire.shopick.event;

import com.acquire.shopick.io.model.Banner;

import java.util.ArrayList;

/**
 * Created by gaurav on 4/6/16.
 */
public class BannersLoadedEvent {
    public BannersLoadedEvent(ArrayList<Banner> banners) {
        this.banners = banners;
    }

    public ArrayList<Banner> getBanners() {
        return banners;
    }

    public void setBanners(ArrayList<Banner> banners) {
        this.banners = banners;
    }

    ArrayList<Banner> banners;
}
