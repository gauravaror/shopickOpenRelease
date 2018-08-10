package com.acquire.shopick.event;

import com.acquire.shopick.io.model.RedeemPick;

import java.util.ArrayList;

/**
 * Created by gaurav on 4/5/16.
 */
public class RedeemPicksLoadedEvent {
    ArrayList<RedeemPick> redeemPicks;

    public ArrayList<RedeemPick> getRedeemPicks() {
        return redeemPicks;
    }

    public void setRedeemPicks(ArrayList<RedeemPick> redeemPicks) {
        this.redeemPicks = redeemPicks;
    }

    public RedeemPicksLoadedEvent(ArrayList<RedeemPick> redeemPicks) {
        this.redeemPicks = redeemPicks;
    }
}
