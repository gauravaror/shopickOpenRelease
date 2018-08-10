package com.acquire.shopick.model;

import com.acquire.shopick.Config;
import com.acquire.shopick.dao.BrandUpdates;
import com.acquire.shopick.dao.BrandUpdatesDao;
import com.acquire.shopick.dao.DaoSession;
import com.acquire.shopick.dao.Tips;
import com.acquire.shopick.dao.TipsDao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.greenrobot.dao.query.Query;

/**
 * Created by yigit on 2/4/14.
 */
@Singleton
public class ShopickTipsModel {
    private final TipsDao tipsDao;
    private final DaoSession daoSession;
    private final Query<Tips> categoryTipsQuery;


    @Inject
    public ShopickTipsModel(DaoSession daoSession) {
        this.daoSession = daoSession;
        tipsDao = daoSession.getTipsDao();
        categoryTipsQuery = tipsDao.queryBuilder().where(
                TipsDao.Properties.Category_id.eq("x")
        ).orderDesc(TipsDao.Properties.Id).build();
    }

    public List<Tips> getAllTips() {
       return tipsDao.queryBuilder().limit(Config.MAX_POST_RESULTS).orderDesc(TipsDao.Properties.Id).list();
    }

    public List<Tips> getCategoryTips(Long category_id) {
        if (category_id != -1) {
            Query<Tips> categorytipsQuery = categoryTipsQuery.forCurrentThread();
            categorytipsQuery.setParameter(0, category_id);
            return categorytipsQuery.list();
        }
        return getAllTips();
    }




    public void saveTip(Tips brand) {
        if(brand != null) {
            tipsDao.insertOrReplace(brand);
        }
    }

    public void saveTips(final List<Tips> updates) {
        if(updates != null) {
            tipsDao.insertOrReplaceInTx(updates);
        }
    }

    public void delete(Tips updates) {
        tipsDao.delete(updates);
    }

    public void deleteAll() {
        tipsDao.deleteAll();
    }

}
