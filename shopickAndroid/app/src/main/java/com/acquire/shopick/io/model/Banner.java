package com.acquire.shopick.io.model;

/**
 * Created by gaurav on 4/6/16.
 */
public class Banner {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getGlobalID() {
        return globalID;
    }

    public void setGlobalID(String globalID) {
        this.globalID = globalID;
    }

    public String getIntentUrl() {
        return intentUrl;
    }

    public void setIntentUrl(String intentUrl) {
        this.intentUrl = intentUrl;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    Long id;
    String title;
    String description;
    String explanation;
    String globalID;
    String intentUrl;
    String visible;
    String imageUrl;

    public Banner(Long id, String title, String description, String explanation, String globalID, String intentUrl, String visible, String imageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.explanation = explanation;
        this.globalID = globalID;
        this.intentUrl = intentUrl;
        this.visible = visible;
        this.imageUrl = imageUrl;
    }
}
