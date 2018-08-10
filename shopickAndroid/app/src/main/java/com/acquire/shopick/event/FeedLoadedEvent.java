package com.acquire.shopick.event;

import com.acquire.shopick.dao.Post;
import com.acquire.shopick.io.model.Feed;
import com.acquire.shopick.io.model.Store;

import java.util.ArrayList;

/**
 * Created by gaurav on 10/9/15.
 */
public class FeedLoadedEvent {
    ArrayList<Post> feeds;

    public FeedLoadedEvent(ArrayList<Post> feedLoaded) {
        this.feeds = feedLoaded;
    }

    public ArrayList<Post> getFeed() {
        return feeds;
    }

}
