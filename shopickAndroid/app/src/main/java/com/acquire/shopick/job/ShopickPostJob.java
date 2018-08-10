package com.acquire.shopick.job;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.acquire.shopick.Config;
import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.event.DeletedNewlyAddedPostEvent;
import com.acquire.shopick.event.NewPostAddedEvent;
import com.acquire.shopick.event.UpdatedNewPostEvent;
import com.acquire.shopick.io.network.ShopickApi;
import com.acquire.shopick.model.ShopickPostModel;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.ImageUtil;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.otto.Bus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static com.acquire.shopick.util.LogUtils.makeLogTag;


public class ShopickPostJob extends Job {
    private static final String TAG = makeLogTag(ShopickPostJob.class);
    @Inject
    transient ShopickPostModel model;
    @Inject transient Bus eventBus;

    private String text;
    private String uniqId;
    private Long place_id;
    private String place_index;
    private Long category_id;
    private String imageUri;
    private File imageFile;
    private Long user_id;
    private int post_type;
    private Long postCollectionId;
    private String postCollectionTitle;
    private long time_since = System.currentTimeMillis();

    public ShopickPostJob(String text, Long place_id,String place_index, Long category_id, Long user_id, int post_type, Long postCollectionId, String postCollectionTitle, String imageUri,  File imageFile) {
        super(new Params(Priority.MID).groupBy("send_post")
                .persist()
                .requireNetwork());
        this.text = text;
        this.place_id = place_id;
        this.category_id = category_id;
        this.imageUri = imageUri;
        this.user_id = user_id;
        this.post_type = post_type;
        this.imageFile = imageFile;
        this.place_index = place_index;
        this.postCollectionId = postCollectionId;
        this.postCollectionTitle = postCollectionTitle;

        uniqId = UUID.randomUUID().toString();
        eventBus = BusProvider.getInstance();
       // eventBus.register(this);
    }

    @Override
    public void onAdded() {
        Post post = new Post();
        post.setCategory_id(category_id);
        post.setStore_id(place_id);
        post.setDescription(text);
        post.setLocalFileUri(imageUri);
        post.setUser_id(user_id);
        post.setPost_type(post_type);
        post.setGlobalID(uniqId);
        post.setIsLocal(true);
        post.setLiked(false);
        post.setUsername(AccountUtils.getPlusName(getApplicationContext()));
        model.savePost(post);
        postEvent(new NewPostAddedEvent(uniqId));
    }

    @Override
    public void onRun() throws Throwable {
        Post localPost = model.load(uniqId);
        Post checkPosts = checkPostExist(uniqId);
        boolean local = true;
        local = localPost.isLocal();
        Response<Post> post = null;
        if (local || checkPosts == null || checkPosts == null) {
            post = actualPost();
        }

        if (checkPosts != null && checkPosts != null ) {
            Post new_post  = checkPosts;
            new_post.setIsLocal(false);
            model.deleteById(uniqId);
            model.savePost(new_post);
            postEvent(new UpdatedNewPostEvent(new_post));
        } else if( post != null && post.isSuccess()) {
            Post new_post = post.body();
            new_post.setIsLocal(false);
            model.deleteById(uniqId);
            model.savePost(new_post);
            postEvent(new UpdatedNewPostEvent(new_post));
        } else {
            throw new Exception();

        }

    }


    public Response<Post> actualPost() throws IOException {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(1000, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(1000, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(1000, TimeUnit.SECONDS);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        ShopickApi.FeedService service = retrofit.create(ShopickApi.FeedService.class);

        RequestBody image = null;
        // When we know that's it's not going to successed as it has already failed 19 times!! try to send only the text, skip sending image to server.
        if (imageUri != null && imageFile != null && getCurrentRunCount() < 5 ) {
            image = RequestBody.create(MediaType.parse("image/*"), imageFile);
        } else if (imageUri != null && imageFile != null && getCurrentRunCount() < 19 ) {
            Bitmap newbit = decodeFile(imageFile, 100, 100);
            writeFile(newbit,imageFile);
            image = RequestBody.create(MediaType.parse("image/*"), imageFile);
        }
        if (this.text == null) {
            this.text = " ";
        }
        RequestBody description = RequestBody.create(MediaType.parse("text/*"), text);
        if (this.place_index == null) {
            this.place_index = " ";
        }
        RequestBody placeIndex = RequestBody.create(MediaType.parse("text/*"), place_index);
        if (this.postCollectionTitle == null) {
            this.postCollectionTitle = " ";
        }
        RequestBody discount_title = RequestBody.create(MediaType.parse("text/*"), postCollectionTitle);

        RequestBody globalID = RequestBody.create(MediaType.parse("text/*"), uniqId);

        Call<Post> call = service.uploadPost(user_id, image, place_id, placeIndex, post_type ,
                    category_id, postCollectionId, discount_title, description, globalID, time_since, AccountUtils.getRequestMap(ShopickApplication.getInstance()));
        return call.execute();
    }

    public  Bitmap decodeFile(File file, int reqWidth, int reqHeight) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(),o);
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = ImageUtil.calculateInSampleSize(o,reqWidth,reqHeight);
        return BitmapFactory.decodeFile(file.getPath(), o2);
    }

    public void writeFile(Bitmap bitmapImage, File mypath) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    public Post checkPostExist(String globalID) throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ShopickApi.FeedService service = retrofit.create(ShopickApi.FeedService.class);


       // RequestBody globalIDRequest = RequestBody.create(MediaType.parse("text/*"), globalID);

        Call<Post> call = service.checkPostExist(globalID, AccountUtils.getRequestMap(ShopickApplication.getInstance()));
        return call.execute().body();
    }

    @Override
    protected void onCancel() {
        Post localPost = model.load(uniqId);
        if(localPost != null) {
            model.deleteById(localPost.getGlobalID());
            postEvent(new DeletedNewlyAddedPostEvent());
        }
    }

    void postEvent(Object new_class) {
        if (eventBus == null) {
            eventBus = BusProvider.getInstance();
        }
        eventBus.post(new_class);
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount,
                                                     int maxRunCount) {
        // An error occurred in onRun.
        // Return value determines whether this job should retry or cancel. You can further
        // specifcy a backoff strategy or change the job's priority. You can also apply the
        // delay to the whole group to preserve jobs' running order.
        return RetryConstraint.createExponentialBackoff(runCount, 1500);
    }

}
