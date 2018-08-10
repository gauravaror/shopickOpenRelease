package com.acquire.shopick.job;

import android.content.Context;
import android.provider.ContactsContract;

import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.dao.Store;
import com.acquire.shopick.event.StoresLoadedEvent;
import com.acquire.shopick.util.GenericUtils;
import com.acquire.shopick.util.LogUtils;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;
import com.squareup.otto.Bus;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by gaurav on 2/25/16.
 */
public class OpenShopickWhatsAppJob extends Job {

    @Inject
    transient Bus eventBus;

    Context mContext;


    public OpenShopickWhatsAppJob(Context context) {
        super(new Params(Priority.HIGH).groupBy("OPENSHOPICKWHATSAPPJOB").setDelayMs(7000));
        mContext = context;
    }

    @Override
    public void onAdded() {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount,
                                                     int maxRunCount) {
        // An error occurred in onRun.
        // Return value determines whether this job should retry or cancel. You can further
        // specifcy a backoff strategy or change the job's priority. You can also apply the
        // delay to the whole group to preserve jobs' running order.
        RetryConstraint con;
        if (runCount > maxRunCount) {
            con = new RetryConstraint(false);
        } else {
            con = new RetryConstraint(true);
            con.setNewDelayInMs(5000L);
        }
        return con;
    }


    @Override
    protected int getRetryLimit() {
        return 120;
    }
    @Override
    public void onRun() throws Throwable {
        if (GenericUtils.sendWhatsAppMessage(mContext, null, false) ) {
            LogUtils.LOGD("Whatsapp","Message sent");
        } else {
            throw new Exception();
        }
    }

    @Override
    protected void onCancel() {

    }


    void postEvent(Object new_class) {
        if (eventBus == null) {
            eventBus = BusProvider.getInstance();
        }
        eventBus.post(new_class);
    }

}
