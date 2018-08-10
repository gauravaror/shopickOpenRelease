package com.acquire.shopick.model;

import com.acquire.shopick.Config;
import com.acquire.shopick.dao.Brands;
import com.acquire.shopick.dao.BrandsDao;
import com.acquire.shopick.dao.Categories;
import com.acquire.shopick.dao.CategoriesDao;
import com.acquire.shopick.dao.DaoSession;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.greenrobot.dao.query.Query;

@Singleton
public class ShopickCategoryModel {
    private final CategoriesDao categoryDao;
    private final DaoSession daoSession;

    @Inject
    public ShopickCategoryModel(DaoSession daoSession) {
        this.daoSession = daoSession;
        categoryDao = daoSession.getCategoriesDao();
    }

    public List<Categories> getAllCategories() {
       return categoryDao.queryBuilder().limit(Config.MAX_POST_RESULTS).orderDesc(CategoriesDao.Properties.Id).list();
    }



    public void saveCategory(Categories categories) {
        if(categories != null) {
            categoryDao.insertOrReplace(categories);
        }
    }

    public void saveCategories(final List<Categories> categories) {
        if(categories != null) {
            categoryDao.insertOrReplaceInTx(categories);
        }
    }

    public void delete(Categories categories) {
        categoryDao.delete(categories);
    }

    public void deleteAll() {
        categoryDao.deleteAll();
    }

}
