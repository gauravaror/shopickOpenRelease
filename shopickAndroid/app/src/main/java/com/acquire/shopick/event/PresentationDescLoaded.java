package com.acquire.shopick.event;

import com.acquire.shopick.io.model.Presentation;

/**
 * Created by gaurav on 12/23/15.
 */
public class PresentationDescLoaded {
    public PresentationDescLoaded(Presentation presentation) {
        this.presentation = presentation;
    }

    public Presentation getPresentation() {
        return presentation;
    }

    public void setPresentation(Presentation presentation) {
        this.presentation = presentation;
    }

    Presentation presentation;

}
