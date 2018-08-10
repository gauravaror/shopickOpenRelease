package com.acquire.shopick.model;

/**
 * Created by gaurav on 9/29/15.
 */
public class FeedCollectionItem {
    public String id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOrder_in_category() {
        return order_in_category;
    }

    public void setOrder_in_category(int order_in_category) {
        this.order_in_category = order_in_category;
    }
    public FeedCollectionItem(int pos) {
        position = pos;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    int position;

    public String username;
    public int user_id;
    public String image_url;
    public String title;
    public String description;
    public String[] tags;
    public int likes;
    public int type;
    public int order_in_category;
}
