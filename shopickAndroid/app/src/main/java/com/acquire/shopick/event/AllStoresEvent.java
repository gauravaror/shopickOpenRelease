package com.acquire.shopick.event;

import com.acquire.shopick.dao.Store;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaurav on 12/8/15.
 */
public class AllStoresEvent {
    public List<Store> getStoreList() {
        return storeList;
    }

    public void setStoreList(ArrayList<Store> storeList) {
        this.storeList = storeList;
    }

    public AllStoresEvent(List<Store> storeList) {
        this.storeList = storeList;
    }

    List<Store> storeList;
}
