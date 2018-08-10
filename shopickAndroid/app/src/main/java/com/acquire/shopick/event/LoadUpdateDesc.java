package com.acquire.shopick.event;

/**
 * Created by gaurav on 12/23/15.
 */
public class LoadUpdateDesc {
    public String getUpdate_id() {
        return update_id;
    }

    public void setUpdate_id(String update_id) {
        this.update_id = update_id;
    }

    public LoadUpdateDesc(String update_id) {
        this.update_id = update_id;
    }

    String update_id;

}
