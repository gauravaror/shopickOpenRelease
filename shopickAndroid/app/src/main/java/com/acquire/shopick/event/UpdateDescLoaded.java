package com.acquire.shopick.event;

import com.acquire.shopick.io.model.Updates;

/**
 * Created by gaurav on 12/23/15.
 */
public class UpdateDescLoaded {
    Updates update;

    public Updates getUpdate() {
        return update;
    }

    public void setUpdate(Updates update) {
        this.update = update;
    }

    public UpdateDescLoaded(Updates update) {
        this.update = update;
    }
}
