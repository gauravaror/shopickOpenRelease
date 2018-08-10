package com.acquire.shopick.event;

import com.acquire.shopick.io.model.Status;

/**
 * Created by gaurav on 3/29/16.
 */
public class StatusRedeemReferralServiceEvent {
    Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public StatusRedeemReferralServiceEvent(Status status) {

        this.status = status;
    }
}
