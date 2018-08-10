package com.acquire.shopick.event;

import com.acquire.shopick.io.model.AllOffer;

import java.util.ArrayList;

/**
 * Created by gaurav on 4/17/16.
 */
public class AllOffersLoadedEvent {
    public AllOffersLoadedEvent(ArrayList<AllOffer> allOffers) {
        this.allOffers = allOffers;
    }

    public ArrayList<AllOffer> getAllOffers() {
        return allOffers;
    }

    public void setAllOffers(ArrayList<AllOffer> allOffers) {
        this.allOffers = allOffers;
    }

    ArrayList<AllOffer> allOffers;

}
