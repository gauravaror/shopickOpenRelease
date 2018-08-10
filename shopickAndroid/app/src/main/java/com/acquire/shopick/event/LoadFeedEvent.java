package com.acquire.shopick.event;

/**
 * Created by gaurav on 10/9/15.
 */
public class LoadFeedEvent {
    int user_id;

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    String filter;

    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    int postType;


    public Long getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(Long brand_id) {
        this.brand_id = brand_id;
    }

    Long brand_id = -1L;

    public LoadFeedEvent(int user_id, String filter, int postType, Long brand_id) {
        this.filter = filter;
        this.user_id =  user_id;
        this.postType = postType;
        this.brand_id =  brand_id;
    }
    public int getId() {
        return user_id;
    }
}
