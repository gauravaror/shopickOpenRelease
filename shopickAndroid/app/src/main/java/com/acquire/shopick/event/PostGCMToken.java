package com.acquire.shopick.event;

/**
 * Created by gaurav on 11/3/15.
 */
public class PostGCMToken {
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    String token;

    public String getInstanceid() {
        return instanceid;
    }

    public void setInstanceid(String instanceid) {
        this.instanceid = instanceid;
    }

    String instanceid;

    public PostGCMToken(String token, String iid) {
        this.token = token;
        this.instanceid =  iid;
    }

}
