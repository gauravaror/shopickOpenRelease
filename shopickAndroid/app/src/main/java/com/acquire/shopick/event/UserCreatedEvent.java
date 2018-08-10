package com.acquire.shopick.event;

import com.acquire.shopick.io.model.User;

/**
 * Created by gaurav on 11/3/15.
 */
public class UserCreatedEvent {
    public UserCreatedEvent(com.acquire.shopick.dao.User accountName) {
        this.accountName = accountName;
    }

    public com.acquire.shopick.dao.User getUser() {
        return accountName;
    }

    public void setUser(com.acquire.shopick.dao.User accountName) {
        this.accountName = accountName;
    }

    com.acquire.shopick.dao.User accountName;

}
