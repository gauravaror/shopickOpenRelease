package com.acquire.shopick.job;

import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.event.PostLoadedEvent;
import com.acquire.shopick.model.ShopickPostModel;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.squareup.otto.Bus;

import javax.inject.Inject;

/**
 * Created by gaurav on 12/5/15.
 */
public class GetLocalJob extends Job {

    @Inject
    transient Bus eventBus;

    @Inject
    transient ShopickPostModel model;



    public String getPost_local_id() {
        return post_local_id;
    }

    public void setPost_local_id(String post_local_id) {
        this.post_local_id = post_local_id;
    }

    public GetLocalJob( String post_local_id) {
        super(new Params(Priority.HIGH).groupBy("get local post"));

        this.post_local_id = post_local_id;

    }

    String post_local_id;

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        Post last_post = model.load(post_local_id);
        postEvent(new PostLoadedEvent(last_post));
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
