package com.acquire.shopick.io.model;

/**
 * Created by gaurav on 4/5/16.
 */
public class EarnPicks {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title;
    String description;
    String instruction;
    int givenPicks;
    String intentUrl;

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

    public int getGivenPicks() {
        return givenPicks;
    }

    public void setGivenPicks(int givenPicks) {
        this.givenPicks = givenPicks;
    }

    public String getIntentUrl() {
        return intentUrl;
    }

    public void setIntentUrl(String intentUrl) {
        this.intentUrl = intentUrl;
    }
}
