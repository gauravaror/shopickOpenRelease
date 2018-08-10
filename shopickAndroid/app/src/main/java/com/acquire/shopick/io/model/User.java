package com.acquire.shopick.io.model;

/**
 * Created by gaurav on 10/27/15.
 */
public class User {
    public String getAuthToken() {
        return authentication_token;
    }

    public void setAuthToken(String authToken) {
        this.authentication_token = authToken;
    }

    public User(String authToken, int user_id) {
        this.authentication_token = authToken;
        this.id = user_id;
    }

    public String authentication_token;
    public int id = -1;
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUser_id() {
        return id;
    }

    public void setUser_id(int user_id) {
        this.id = user_id;
    }
}
