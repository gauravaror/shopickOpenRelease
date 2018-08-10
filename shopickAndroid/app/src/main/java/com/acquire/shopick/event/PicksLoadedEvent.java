package com.acquire.shopick.event;

import com.acquire.shopick.dao.User;

/**
 * Created by gaurav on 4/4/16.
 */
public class PicksLoadedEvent {

    public PicksLoadedEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    User user;
}
