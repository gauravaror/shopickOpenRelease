package com.acquire.shopick.event;

import com.acquire.shopick.dao.Tips;
import com.acquire.shopick.io.model.Presentation;
import com.acquire.shopick.io.model.Store;

import java.util.ArrayList;

/**
 * Created by gaurav on 10/17/15.
 */
public class PresentationsLoadedEvent {
    ArrayList<Tips> presentations;

    public PresentationsLoadedEvent(ArrayList<Tips> presentationsLoaded) {
        this.presentations = presentationsLoaded;
    }

    public ArrayList<Tips> getPresentations() {
        return presentations;
    }
}
