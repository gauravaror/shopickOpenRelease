package com.acquire.shopick.model;

import com.acquire.shopick.Config;
import com.acquire.shopick.dao.BrandUpdates;
import com.acquire.shopick.dao.BrandUpdatesDao;
import com.acquire.shopick.dao.Brands;
import com.acquire.shopick.dao.BrandsDao;
import com.acquire.shopick.dao.DaoSession;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.greenrobot.dao.query.Query;

/**
 * Created by yigit on 2/4/14.
 */
@Singleton
public class ShopickBrandUpdatesModel {
    private final BrandUpdatesDao brandDao;
    private final DaoSession daoSession;
    private final Query<BrandUpdates> categorybrandsQuery;
    private final Query<BrandUpdates> brandDrandsQuery;


    @Inject
    public ShopickBrandUpdatesModel(DaoSession daoSession) {
        this.daoSession = daoSession;
        brandDao = daoSession.getBrandUpdatesDao();
        categorybrandsQuery = brandDao.queryBuilder().where(
                BrandUpdatesDao.Properties.Category_id.eq("x")
        ).orderDesc(BrandUpdatesDao.Properties.Id).build();
        brandDrandsQuery = brandDao.queryBuilder().where(
                BrandUpdatesDao.Properties.Brand_id.eq("x")
        ).orderDesc(BrandUpdatesDao.Properties.Id).build();

    }

    public List<BrandUpdates> getAllBrandUpdates() {
       return brandDao.queryBuilder().limit(Config.MAX_POST_RESULTS).orderDesc(BrandUpdatesDao.Properties.Id).list();
    }

    public List<BrandUpdates> getCategoryBrandUpdates(Long category_id) {
        if (category_id != -1) {
            Query<BrandUpdates> categoryBrandsQuery = categorybrandsQuery.forCurrentThread();
            categoryBrandsQuery.setParameter(0, category_id);
            return categoryBrandsQuery.list();
        }
        return getAllBrandUpdates();
    }


    public List<BrandUpdates> getBrandBrandUpdates(Long brand_id) {
        if (brand_id != -1) {
            Query<BrandUpdates> brandPostQuery = brandDrandsQuery.forCurrentThread();
            brandPostQuery.setParameter(0, brand_id);
            return brandPostQuery.list();
        }
        return getAllBrandUpdates();
    }


    public void saveUpdate(BrandUpdates brand) {
        if(brand != null) {
            brandDao.insertOrReplace(brand);
        }
    }

    public void saveUpdates(final List<BrandUpdates> updates) {
        if(updates != null) {
            brandDao.insertOrReplaceInTx(updates);
        }
    }

    public void delete(BrandUpdates updates) {
        brandDao.delete(updates);
    }

    public void deleteAll() {
        brandDao.deleteAll();
    }

}
