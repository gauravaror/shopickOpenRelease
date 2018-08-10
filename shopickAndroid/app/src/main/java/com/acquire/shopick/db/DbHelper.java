package com.acquire.shopick.db;


import android.database.sqlite.SQLiteDatabase;


import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.dao.DaoMaster;
import com.acquire.shopick.dao.DaoSession;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by yigit on 2/4/14.
 */
@Singleton
public class DbHelper {
    private DaoSession daoSession;
    private DaoMaster daoMaster;
    private SQLiteDatabase db;

    @Inject
    public DbHelper() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(ShopickApplication.getInstance(), "twitter", null);
        db = devOpenHelper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }
}