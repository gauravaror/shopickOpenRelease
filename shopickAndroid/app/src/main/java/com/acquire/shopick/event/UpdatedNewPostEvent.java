package com.acquire.shopick.event;

import com.acquire.shopick.dao.Post;

/**
 * Created by gaurav on 12/2/15.
 */
public class UpdatedNewPostEvent {

    Post post;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public UpdatedNewPostEvent(Post post) {
        this.post = post;
    }
}
