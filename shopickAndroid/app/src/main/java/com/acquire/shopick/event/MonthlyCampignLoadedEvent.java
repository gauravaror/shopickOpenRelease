package com.acquire.shopick.event;

import com.acquire.shopick.io.model.Referral;

/**
 * Created by gaurav on 3/28/16.
 */
public class MonthlyCampignLoadedEvent {

    public MonthlyCampignLoadedEvent(Referral monthlyCampign) {
        this.monthlyCampign = monthlyCampign;
    }

    public Referral getMonthlyCampign() {
        return monthlyCampign;
    }

    public void setMonthlyCampign(Referral monthlyCampign) {
        this.monthlyCampign = monthlyCampign;
    }

    Referral monthlyCampign;


}
