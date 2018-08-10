package com.acquire.shopick.event;

import com.acquire.shopick.dao.Tips;

import java.util.ArrayList;

/**
 * Created by gaurav on 10/17/15.
 */
public class StoredPresentationsLoadedEvent {
    ArrayList<Tips> presentations;

    public StoredPresentationsLoadedEvent(ArrayList<Tips> presentationsLoaded) {
        this.presentations = presentationsLoaded;
    }

    public ArrayList<Tips> getPresentations() {
        return presentations;
    }
}
