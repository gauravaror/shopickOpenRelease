package com.acquire.shopick.event;

/**
 * Created by gaurav on 3/29/16.
 */
public class RedeemReferralServiceEvent {
    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String usercode;

    public RedeemReferralServiceEvent(String usercode) {
        this.usercode = usercode;
    }
}
