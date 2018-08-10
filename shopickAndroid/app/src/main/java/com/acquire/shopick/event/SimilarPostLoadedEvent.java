package com.acquire.shopick.event;

import com.acquire.shopick.dao.Post;

import java.util.ArrayList;

/**
 * Created by gaurav on 12/9/15.
 */
public class SimilarPostLoadedEvent {
    public SimilarPostLoadedEvent(ArrayList<Post> similarPost) {
        this.similarPost = similarPost;
    }

    public ArrayList<Post> getSimilarPost() {
        return similarPost;
    }

    public void setSimilarPost(ArrayList<Post> similarPost) {
        this.similarPost = similarPost;
    }

    ArrayList<Post> similarPost;
}
