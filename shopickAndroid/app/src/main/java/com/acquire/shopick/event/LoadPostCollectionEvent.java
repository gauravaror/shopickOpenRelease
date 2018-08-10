package com.acquire.shopick.event;

/**
 * Created by gaurav on 4/5/16.
 */
public class LoadPostCollectionEvent {

    public LoadPostCollectionEvent(String collection_id) {
        this.collection_id = collection_id;
    }

    public String getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(String collection_id) {
        this.collection_id = collection_id;
    }

    String collection_id;
}
