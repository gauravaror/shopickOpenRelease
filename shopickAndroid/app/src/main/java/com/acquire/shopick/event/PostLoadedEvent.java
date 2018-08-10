package com.acquire.shopick.event;

import com.acquire.shopick.dao.Post;

/**
 * Created by gaurav on 12/5/15.
 */
public class PostLoadedEvent {
    public PostLoadedEvent(Post new_post) {
        this.new_post = new_post;
    }

    public Post getNew_post() {
        return new_post;
    }

    public void setNew_post(Post new_post) {
        this.new_post = new_post;
    }

    private Post new_post;
}
