package com.acquire.shopick;

import android.app.Application;

import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.di.ShopickModule;
import com.acquire.shopick.io.network.NetworkService;
import com.acquire.shopick.io.network.ShopickApi;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.squareup.otto.Bus;

import java.net.URISyntaxException;

import dagger.ObjectGraph;
import io.fabric.sdk.android.Fabric;

/**
 * Created by gaurav on 10/8/15.
 */
public class ShopickApplication extends Application {

    private NetworkService networkService;
    private Bus mBus = BusProvider.getInstance();
    private static ShopickApplication instance;
    private ObjectGraph objectGraph;

    public ShopickApplication() {
        instance =  this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        networkService = new NetworkService(mBus,getApplicationContext());
        mBus.register(networkService);
        mBus.register(this); //listen for "global" events
        objectGraph = ObjectGraph.create(new ShopickModule());
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    public static void injectMembers(Object object) {
        getInstance().objectGraph.inject(object);
    }

    public static <T>T get(Class<T> klass) {
        return getInstance().objectGraph.get(klass);
    }


    public static ShopickApplication getInstance() {
        return instance;
    }
}
