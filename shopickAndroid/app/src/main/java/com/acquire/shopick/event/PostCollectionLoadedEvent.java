package com.acquire.shopick.event;

import com.acquire.shopick.dao.Post;

import java.util.ArrayList;

/**
 * Created by gaurav on 4/5/16.
 */
public class PostCollectionLoadedEvent {
    public ArrayList<Post> getPostArrayList() {
        return postArrayList;
    }

    public void setPostArrayList(ArrayList<Post> postArrayList) {
        this.postArrayList = postArrayList;
    }

    public PostCollectionLoadedEvent(ArrayList<Post> postArrayList) {
        this.postArrayList = postArrayList;
    }

    ArrayList<Post> postArrayList;
}
