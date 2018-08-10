package com.acquire.shopick.event;

/**
 * Created by gaurav on 12/23/15.
 */
public class LoadPresentationDesc {
    public String getPresentation_id() {
        return presentation_id;
    }

    public void setPresentation_id(String presentation_id) {
        this.presentation_id = presentation_id;
    }

    public LoadPresentationDesc(String presentation_id) {
        this.presentation_id = presentation_id;
    }

    String presentation_id;
}
