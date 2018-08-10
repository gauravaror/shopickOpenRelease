package com.acquire.shopick.model;

import com.acquire.shopick.dao.DaoSession;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.dao.PostDao;
import com.acquire.shopick.dao.Store;
import com.acquire.shopick.dao.StoreDao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.Query;

/**
 * Created by yigit on 2/4/14.
 */
@Singleton
public class ShopickStoreModel {
    private final StoreDao storeDao;
    private final DaoSession daoSession;

    @Inject
    public ShopickStoreModel(DaoSession daoSession) {
        this.daoSession = daoSession;
        storeDao = daoSession.getStoreDao();

    }



    public List<Store> getAllStoreNearBy(float lat, float lon) {
        if (lat != -1 && lon != -1) {
            return storeDao.queryRaw("SELECT *, ( 3959 * acos( cos( radians(?) ) * cos( radians( lat ) ) * cos( radians( lon ) - radians(?) ) + sin( radians(?) ) * sin( radians( lat ) ) ) ) AS distance FROM stores ORDER BY distance ;",
                    String.valueOf(lat),String.valueOf(lon),String.valueOf(lat));
        }
        return  null;
    }

    public List<Store> getAllStores() {
        return storeDao.queryBuilder().list();
    }

    public void savePost(Store store) {
        if(store != null) {
            storeDao.insertOrReplace(store);
        }
    }

    public void saveStores(final List<Store> stores) {
        if(stores != null) {
            storeDao.insertOrReplaceInTx(stores);
        }
    }


    public void delete(Store store) {
        storeDao.delete(store);
    }

    public void deleteAll() {
        
        storeDao.deleteAll();
    }
}
