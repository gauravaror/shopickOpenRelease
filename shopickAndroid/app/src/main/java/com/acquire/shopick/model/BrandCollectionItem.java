package com.acquire.shopick.model;

/**
 * Created by gaurav on 9/29/15.
 */
public class BrandCollectionItem {
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int position;

    public int id;
    public String name;
    public String logo_url;
    public String tag_line;

    public int getOrder_in_category() {
        return order_in_category;
    }

    public void setOrder_in_category(int order_in_category) {
        this.order_in_category = order_in_category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getTag_line() {
        return tag_line;
    }

    public void setTag_line(String tag_line) {
        this.tag_line = tag_line;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int order_in_category;
    public int type;
}
