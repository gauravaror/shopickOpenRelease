package com.acquire.shopick.job;

import android.util.Log;

import com.acquire.shopick.Config;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.event.FeedLoadedEvent;
import com.acquire.shopick.event.LocalFeedLoadedEvent;
import com.acquire.shopick.event.PostLoadedEvent;
import com.acquire.shopick.io.network.ShopickApi;
import com.acquire.shopick.model.ShopickPostModel;
import com.acquire.shopick.util.AccountUtils;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.squareup.otto.Bus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by gaurav on 12/5/15.
 */
public class GetPostsJob extends Job {

    @Inject
    transient Bus eventBus;

    @Inject
    transient ShopickPostModel model;

    int postType = -1;
    Long brand_id = -1L;
    Long category_id = -1L;
    int user_id;

    public GetPostsJob(int user_id, Long category_id, int postType, Long brand_id) {
        super(new Params(Priority.HIGHEST).groupBy("GETPOSTS"));
        this.category_id = category_id;
        this.user_id =  user_id;
        this.postType = postType;
        this.brand_id =  brand_id;


    }
    @Override
    public void onAdded() {
        postEvent(new LocalFeedLoadedEvent(getLocalPosts()));
    }

    @Override
    public void onRun() throws Throwable {
        ArrayList<Post> posts = getPosts();
        model.savePosts(posts);
        postEvent(new FeedLoadedEvent(posts));
    }



    private ArrayList<Post> getLocalPosts()  {
        List<Post> local_list = null;
        if (brand_id == -1L && postType == -1 && category_id == -1L) {
            local_list = model.getAllPosts();
        } else if (brand_id != -1L) {
            local_list = model.getBrandPosts(brand_id);
        }  else if (category_id != -1L) {
            local_list = model.getCategoryPosts(category_id);
        }  else if (postType != -1) {
            local_list = model.getTypePosts(postType);
        } else {
            local_list = model.getAllPosts();
        }
        return new ArrayList<Post>(local_list);
    }


    @Override
    protected void onCancel() {

    }


    private ArrayList<Post> getPosts() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.FeedService service = retrofit.create(ShopickApi.FeedService.class);
        Map<String, String> option_feed = AccountUtils.getRequestMap(getApplicationContext());
        option_feed.put("post_type", String.valueOf(postType));
        option_feed.put("brand_id", String.valueOf(brand_id));
        option_feed.put("category_id", String.valueOf(category_id));
        Call<ArrayList<Post>> call = service.getFeed(user_id, option_feed);
        return call.execute().body();
    }


    void postEvent(Object new_class) {
        if (eventBus == null) {
            eventBus = BusProvider.getInstance();
        }
        eventBus.post(new_class);
    }

}
