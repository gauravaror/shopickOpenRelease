package com.acquire.shopick.event;

/**
 * Created by gaurav on 12/2/15.
 */
public class NewPostAddedEvent {

    public String getUniqId() {
        return uniqId;
    }

    public void setUniqId(String uniqId) {
        this.uniqId = uniqId;
    }

    public NewPostAddedEvent(String uniqId) {
        this.uniqId = uniqId;
    }

    String uniqId;

}
