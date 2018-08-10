package com.acquire.shopick.model;


/**
 * Created by gaurav on 9/13/15.
 */
public class PickCollectionItem {

    public PickCollectionItem(int pos) {
        position = pos;
    }
    int position;
    public String id;
    public String url;
    public String description;
    public String title;
    public String[] tags;
    public String photoUrl;
    public String mainTag;
    public int order_in_category;
    public int type;

    public int getGroupingOrder() {
        return groupingOrder;
    }

    public void setGroupingOrder(int groupingOrder) {
        this.groupingOrder = groupingOrder;
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

    public String getMainTag() {
        return mainTag;
    }

    public void setMainTag(String mainTag) {
        this.mainTag = mainTag;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int groupingOrder;


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
