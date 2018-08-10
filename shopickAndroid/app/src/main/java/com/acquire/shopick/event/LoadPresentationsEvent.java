package com.acquire.shopick.event;

/**
 * Created by gaurav on 10/17/15.
 */
public class LoadPresentationsEvent {
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


    public LoadPresentationsEvent(int user_id,String filter) {
        this.user_id = user_id;
        this.filter = filter;
    }


}
