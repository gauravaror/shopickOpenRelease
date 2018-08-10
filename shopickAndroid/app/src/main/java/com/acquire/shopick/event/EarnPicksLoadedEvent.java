package com.acquire.shopick.event;

import com.acquire.shopick.io.model.EarnPicks;

import java.util.ArrayList;

/**
 * Created by gaurav on 4/5/16.
 */
public class EarnPicksLoadedEvent {
    public EarnPicksLoadedEvent(ArrayList<EarnPicks> earnPickses) {
        this.earnPickses = earnPickses;
    }

    public ArrayList<EarnPicks> getEarnPickses() {
        return earnPickses;
    }

    public void setEarnPickses(ArrayList<EarnPicks> earnPickses) {
        this.earnPickses = earnPickses;
    }

    ArrayList<EarnPicks> earnPickses;
}
