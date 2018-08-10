package com.acquire.shopick.io.model;

/**
 * Created by gaurav on 4/5/16.
 */
public class RedeemPick {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    Long id;
    String title;
    String description;
    String instruction;
    Long requiredPicks;

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

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Long getRequiredPicks() {
        return requiredPicks;
    }

    public void setRequiredPicks(Long requiredPicks) {
        this.requiredPicks = requiredPicks;
    }

    public String getGlobalID() {
        return globalID;
    }

    public void setGlobalID(String globalID) {
        this.globalID = globalID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    String globalID;
    String imageUrl;
}
