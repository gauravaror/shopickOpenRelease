package com.acquire.shopick.job;
import android.content.Context;

import com.acquire.shopick.ShopickApplication;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.di.DependencyInjector;

import javax.inject.Singleton;

/**
 * Created by yigit on 2/5/14.
 */
@Singleton
public class ShopickJobManager extends JobManager {
    public ShopickJobManager(Context context) {
        super(context, new Configuration.Builder(context)
                .injector(new DependencyInjector() {
                    @Override
                    public void inject(Job baseJob) {
                        ShopickApplication.injectMembers(baseJob);
                    }
                })
                .build());
    }
}