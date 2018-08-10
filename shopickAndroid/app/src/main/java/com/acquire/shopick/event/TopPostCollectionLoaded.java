package com.acquire.shopick.event;

import com.acquire.shopick.dao.PostCollection;

import java.util.ArrayList;

/**
 * Created by gaurav on 6/20/16.
 */

public class TopPostCollectionLoaded {
    public TopPostCollectionLoaded(ArrayList<PostCollection> postCollections) {
        this.postCollections = postCollections;
    }

    public ArrayList<PostCollection> getPostCollections() {
        return postCollections;
    }

    public void setPostCollections(ArrayList<PostCollection> postCollections) {
        this.postCollections = postCollections;
    }

    ArrayList<PostCollection> postCollections;

}
