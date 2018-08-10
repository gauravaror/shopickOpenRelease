package com.acquire.shopick.bus;

/**
 * Created by gaurav on 10/8/15.
 */

import com.squareup.otto.Bus;

public final class BusProvider {

    private static final Bus BUS = new AndroidBus();

    public static Bus getInstance(){
        return BUS;
    }

    private BusProvider(){}
}