package com.acquire.shopick.event;

import com.acquire.shopick.io.model.Referral;

/**
 * Created by gaurav on 3/28/16.
 */
public class ReferralLoadedEvent {
    public Referral getReferral() {
        return referral;
    }

    public void setReferral(Referral referral) {
        this.referral = referral;
    }

    Referral referral;

    public ReferralLoadedEvent(Referral referral1) {
        referral =  referral1;
    }

}
