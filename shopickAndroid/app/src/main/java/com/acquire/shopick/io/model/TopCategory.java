package com.acquire.shopick.io.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gaurav on 9/22/15.
 */
public class TopCategory {
    public int id;
    public String tag;
    public String name;
    public String category;
    public String color;
    public String image_url;
    @SerializedName("abstract")
    public String _abstract;
    public int order_in_category;
}
