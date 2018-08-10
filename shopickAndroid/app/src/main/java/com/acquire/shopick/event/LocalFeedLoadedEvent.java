package com.acquire.shopick.event;

import com.acquire.shopick.dao.Post;

import java.util.ArrayList;

/**
 * Created by gaurav on 10/9/15.
 */
public class LocalFeedLoadedEvent {
    ArrayList<Post> feeds;

    public LocalFeedLoadedEvent(ArrayList<Post> feedLoaded) {
        this.feeds = feedLoaded;
    }

    public ArrayList<Post> getFeed() {
        return feeds;
    }

}
