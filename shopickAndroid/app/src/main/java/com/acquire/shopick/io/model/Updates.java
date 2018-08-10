package com.acquire.shopick.io.model;

import com.acquire.shopick.util.HashUtils;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gaurav on 9/29/15.
 */
public class Updates {
    public int id;
    public String url;
    public String description;
    public String title;
    public String[] tags;
    public String photoUrl;
    public String mainTag;
    public int brand_id;
    @SerializedName("abstract")
    public String _abstract;
    public int order_in_category;
    public int type;
    public int groupingOrder;


    public String getImportHashCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("id").append( id)
                .append("description").append(description == null ? "" : description)
                .append("title").append(title == null ? "" : title)
                .append("url").append(url == null ? "" : url)
                .append("mainTag").append(mainTag)
                .append("photoUrl").append(photoUrl)
                .append("brand_id").append(brand_id)
                .append("type").append(type)
                .append("order_in_category").append(order_in_category)
                .append("groupingOrder").append(groupingOrder);
        for (String tag : tags) {
            sb.append("tag").append(tag);
        }
        return HashUtils.computeWeakHash(sb.toString());
    }
    public String makeTagsList() {
        int i;
        if (tags.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(tags[0]);
        for (i = 1; i < tags.length; i++) {
            sb.append(",").append(tags[i]);
        }
        return sb.toString();
    }

    public boolean hasTag(String tag) {
        for (String myTag : tags) {
            if (myTag.equals(tag)) {
                return true;
            }
        }
        return false;
    }
}
