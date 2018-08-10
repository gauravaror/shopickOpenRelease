package com.acquire.shopick.event;

/**
 * Created by gaurav on 10/19/15.
 */
public class LoadUpdateCollectionEvent {
    String collection_id;

    public String getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(String collection_id) {
        this.collection_id = collection_id;
    }

    public LoadUpdateCollectionEvent(String collection_id) {
        this.collection_id = collection_id;
    }
}
