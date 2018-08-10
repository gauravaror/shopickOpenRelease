package com.acquire.shopick.event;

/**
 * Created by gaurav on 10/8/15.
 */
public class LoadStoresEvent {
    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    double lon = -1;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    double lat = -1;

}
