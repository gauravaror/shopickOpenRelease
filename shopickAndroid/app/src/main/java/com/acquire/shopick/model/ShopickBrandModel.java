package com.acquire.shopick.model;

import com.acquire.shopick.Config;
import com.acquire.shopick.dao.Brands;
import com.acquire.shopick.dao.BrandsDao;
import com.acquire.shopick.dao.DaoSession;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.dao.PostDao;
import com.acquire.shopick.io.model.Brand;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.Query;

/**
 * Created by yigit on 2/4/14.
 */
@Singleton
public class ShopickBrandModel {
    private final BrandsDao brandDao;
    private final DaoSession daoSession;
    private final Query<Brands> categorybrandsQuery;
    private final Query<Brands> brandDrandsQuery;


    @Inject
    public ShopickBrandModel(DaoSession daoSession) {
        this.daoSession = daoSession;
        brandDao = daoSession.getBrandsDao();
        categorybrandsQuery = brandDao.queryBuilder().where(
                BrandsDao.Properties.Category_id.eq("x")
        ).orderDesc(BrandsDao.Properties.Id).build();
        brandDrandsQuery = brandDao.queryBuilder().where(
                BrandsDao.Properties.Id.eq("x")
        ).orderDesc(BrandsDao.Properties.Id).build();

    }

    public List<Brands> getAllBrands() {
       return brandDao.queryBuilder().limit(Config.MAX_POST_RESULTS).orderDesc(BrandsDao.Properties.Id).list();
    }

    public List<Brands> getCategoryBrands(Long category_id) {
        if (category_id != -1) {
            Query<Brands> categoryBrandsQuery = categorybrandsQuery.forCurrentThread();
            categoryBrandsQuery.setParameter(0, category_id);
            return categoryBrandsQuery.list();
        }
        return getAllBrands();
    }


    public List<Brands> getBrandBrands(Long brand_id) {
        if (brand_id != -1) {
            Query<Brands> brandPostQuery = brandDrandsQuery.forCurrentThread();
            brandPostQuery.setParameter(0, brand_id);
            return brandPostQuery.list();
        }
        return getAllBrands();
    }


    public void saveBrand(Brands brand) {
        if(brand != null) {
            brandDao.insertOrReplace(brand);
        }
    }

    public void saveBrands(final List<Brands> brands) {
        if(brands != null) {
            brandDao.insertOrReplaceInTx(brands);
        }
    }

    public void delete(Brands brands) {
        brandDao.delete(brands);
    }

    public void deleteAll() {
        brandDao.deleteAll();
    }

}
