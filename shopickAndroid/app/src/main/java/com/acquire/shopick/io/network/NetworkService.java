package com.acquire.shopick.io.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.acquire.shopick.Config;
import com.acquire.shopick.dao.BrandUpdates;
import com.acquire.shopick.dao.Brands;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.dao.PostCollection;
import com.acquire.shopick.dao.Store;
import com.acquire.shopick.dao.Tips;
import com.acquire.shopick.event.BannersLoadedEvent;
import com.acquire.shopick.event.BrandUpdatesLoadedEvent;
import com.acquire.shopick.event.BrandsLoadedEvent;
import com.acquire.shopick.event.CreatePostEvent;
import com.acquire.shopick.event.CreateUserEvent;
import com.acquire.shopick.event.EarnPicksLoadedEvent;
import com.acquire.shopick.event.FeaturedBrandUpdatesLoadedEvent;
import com.acquire.shopick.event.FeedLoadedEvent;
import com.acquire.shopick.event.LeaderboardLoadedEvent;
import com.acquire.shopick.event.LoadBannerEvent;
import com.acquire.shopick.event.LoadBrandEvent;
import com.acquire.shopick.event.LoadBrandFilteredPostCollection;
import com.acquire.shopick.event.LoadBrandUpdatesEvent;
import com.acquire.shopick.event.LoadEarnPicks;
import com.acquire.shopick.event.LoadFeaturedBrandUpdatesEvent;
import com.acquire.shopick.event.LoadLeaderboard;
import com.acquire.shopick.event.LoadMonthlyCampign;
import com.acquire.shopick.event.LoadPicks;
import com.acquire.shopick.event.LoadPostCollectionEvent;
import com.acquire.shopick.event.LoadPresentationCollectionEvent;
import com.acquire.shopick.event.LoadPresentationDesc;
import com.acquire.shopick.event.LoadProductEvent;
import com.acquire.shopick.event.LoadRedeemPicks;
import com.acquire.shopick.event.LoadReferralCode;
import com.acquire.shopick.event.LoadTTopBrandUpdatesEvent;
import com.acquire.shopick.event.LoadTopPostCollectionEvent;
import com.acquire.shopick.event.LoadUpdateCollectionEvent;
import com.acquire.shopick.event.LoadFeedEvent;
import com.acquire.shopick.event.LoadPresentationsEvent;
import com.acquire.shopick.event.LoadStoresEvent;
import com.acquire.shopick.event.LoadUpdateDesc;
import com.acquire.shopick.event.LoadUserPostCollectionEvent;
import com.acquire.shopick.event.MonthlyCampignLoadedEvent;
import com.acquire.shopick.event.PicksLoadedEvent;
import com.acquire.shopick.event.PostCollectionLoaded;
import com.acquire.shopick.event.PostCollectionLoadedEvent;
import com.acquire.shopick.event.PostGCMToken;
import com.acquire.shopick.event.PresentationCollectionLoadedEvent;
import com.acquire.shopick.event.PresentationDescLoaded;
import com.acquire.shopick.event.PresentationsLoadedEvent;
import com.acquire.shopick.event.ProductLoadedEvent;
import com.acquire.shopick.event.RedeemPicksLoadedEvent;
import com.acquire.shopick.event.RedeemReferralServiceEvent;
import com.acquire.shopick.event.ReferralLoadedEvent;
import com.acquire.shopick.event.StatusRedeemReferralServiceEvent;
import com.acquire.shopick.event.StoresLoadedEvent;
import com.acquire.shopick.event.TopBrandUpdatesLoadedEvent;
import com.acquire.shopick.event.TopPostCollectionLoaded;
import com.acquire.shopick.event.UpdateCollectionLoadedEvent;
import com.acquire.shopick.event.UpdateDescLoaded;
import com.acquire.shopick.event.UpdatedNewPostEvent;
import com.acquire.shopick.event.UserCreatedEvent;
import com.acquire.shopick.io.model.Banner;
import com.acquire.shopick.io.model.Brand;
import com.acquire.shopick.io.model.EarnPicks;
import com.acquire.shopick.io.model.Feed;
import com.acquire.shopick.io.model.Presentation;

import com.acquire.shopick.io.model.Product;
import com.acquire.shopick.io.model.RedeemPick;
import com.acquire.shopick.io.model.Referral;
import com.acquire.shopick.io.model.Status;
import com.acquire.shopick.io.model.Updates;
import com.acquire.shopick.io.model.User;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsManager;
import com.google.gson.JsonElement;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by gaurav on 10/8/15.
 */
public class NetworkService {
    private static final String TAG = NetworkService.class.toString();
    private ShopickApi mApi;
    private Bus mBus;
    private Context mAppContext;

    public NetworkService( Bus bus, Context appContext)  {
        mBus = bus;
        mAppContext = appContext;
    }


    @Subscribe
    public void onCreateUserEvent(final CreateUserEvent event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ShopickApi.UserService service = retrofit.create(ShopickApi.UserService.class);
        Callback<com.acquire.shopick.dao.User> callBack = new Callback<com.acquire.shopick.dao.User>() {

            @Override
            public void onResponse(Response<com.acquire.shopick.dao.User> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                Log.d(TAG, response.raw() == null ? "null" : response.raw().toString());

                com.acquire.shopick.dao.User user = (com.acquire.shopick.dao.User)response.body();
                if (user != null) {
                    if (!TextUtils.isEmpty(user.getAuthentication_token())) {
                        AccountUtils.setShopickAuthToken(mAppContext, user.getEmail(), user.getAuthentication_token());
                    }
                    AccountUtils.setShopickTempProfileId(mAppContext, AccountUtils.getActiveAccountName(mAppContext), user.getId());
                    if (user.getId() != -1) {
                        AccountUtils.setActiveAccount(mAppContext, user.getEmail());
                        AccountUtils.setShopickProfileId(mAppContext, user.getEmail(), user.getId());
                    }
                    AccountUtils.setLoginDone(mAppContext, true);
                    mBus.post(new UserCreatedEvent((com.acquire.shopick.dao.User) response.body()));
                } else {
                    AnalyticsManager.sendEvent("NOTLOGIN", "Error","createuser");
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        // String encodedImage = Base64.encodeToString(getBytesFromBitmap(event.getImage()), Base64.DEFAULT);
        RequestBody authToken;
        if (event.getAuthToken() != null) {
             authToken = RequestBody.create(MediaType.parse("text/*"), event.getAuthToken());
        } else {
            authToken = RequestBody.create(MediaType.parse("text/*"), random());
        }
        RequestBody email = RequestBody.create(MediaType.parse("text/*"), event.getEmail());
        RequestBody name = RequestBody.create(MediaType.parse("text/*"), event.getName() != null ? event.getName() : "");
        RequestBody profileImage = RequestBody.create(MediaType.parse("text/*"), event.getProfileImageUrl() != null ? event.getProfileImageUrl(): "");
        RequestBody coverImage = RequestBody.create(MediaType.parse("text/*"), event.getProfileCoverUrl() != null ? event.getProfileCoverUrl() : "");
        RequestBody id = RequestBody.create(MediaType.parse("text/*"), event.getFacebookId() != null ? event.getFacebookId() : "");
        RequestBody type = RequestBody.create(MediaType.parse("text/*"), event.getLoginType() != null ? event.getLoginType() : "");
        String pass = random();
        RequestBody password = RequestBody.create(MediaType.parse("text/*"), pass);
        RequestBody iid;
        if (TextUtils.isEmpty(AccountUtils.getInstanceId(mAppContext))) {
            iid = RequestBody.create(MediaType.parse("text/*"), "");
        } else {
            iid = RequestBody.create(MediaType.parse("text/*"), AccountUtils.getInstanceId(mAppContext));
        }
        Call<com.acquire.shopick.dao.User> call = service.createUser(authToken, email, name, profileImage,
                coverImage, event.getGender(), event.getAgeMax(), event.getAge_min(), password,
                password, iid,id, type, AccountUtils.getRequestMap(mAppContext));
        call.enqueue(callBack);
    }


    @Subscribe
    public void onPostGCMTokenEvent(final PostGCMToken event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ShopickApi.GCMService service = retrofit.create(ShopickApi.GCMService.class);
        Callback<com.acquire.shopick.dao.User> callBack = new Callback<com.acquire.shopick.dao.User>() {

            @Override
            public void onResponse(Response<com.acquire.shopick.dao.User> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                Log.d(TAG, response.raw() == null ? "null" : response.raw().toString());

                com.acquire.shopick.dao.User user = (com.acquire.shopick.dao.User)response.body();

                if (user != null) {
                    AccountUtils.setActiveAccount(mAppContext, response.body().getEmail());
                    if (!TextUtils.isEmpty(user.getAuthentication_token())) {
                        AccountUtils.setShopickAuthToken(mAppContext, user.getEmail(), user.getAuthentication_token());
                    }
                    if (user.getId() != -1) {
                        AccountUtils.setActiveAccount(mAppContext, user.getEmail());
                        AccountUtils.setShopickProfileId(mAppContext, user.getEmail(), user.getId());
                    }

                    AccountUtils.setShopickTempProfileId(mAppContext, AccountUtils.getActiveAccountName(mAppContext), user.getId());
                } else {
                    AnalyticsManager.sendEvent("NOTLOGIN", "Error","createuser");
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        // String encodedImage = Base64.encodeToString(getBytesFromBitmap(event.getImage()), Base64.DEFAULT);

        RequestBody gcm_token = RequestBody.create(MediaType.parse("text/*"), event.getToken());
        RequestBody iid = RequestBody.create(MediaType.parse("text/*"), event.getInstanceid());

        String profile;
        if(AccountUtils.getShopickProfileId(mAppContext) == -1 ) {
            profile = "-1";
        } else {
            profile = String.valueOf(AccountUtils.getShopickProfileId(mAppContext));
        }
        RequestBody profile_id = RequestBody.create(MediaType.parse("text/*"), profile);
        RequestBody email;
        if (AccountUtils.getActiveAccountName(mAppContext) != null) {
            email = RequestBody.create(MediaType.parse("text/*"), AccountUtils.getActiveAccountName(mAppContext));
        } else {
            email = RequestBody.create(MediaType.parse("text/*"), "");
        }

        Call<com.acquire.shopick.dao.User> call = service.postToken(email, gcm_token, profile_id, iid, AccountUtils.getRequestMap(mAppContext));
        call.enqueue(callBack);
    }


    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = 10;
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
    @Subscribe
    public void onLoadBrandUpdates(final LoadBrandUpdatesEvent event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.UpdatesService service = retrofit.create(ShopickApi.UpdatesService.class);
        Callback<ArrayList<BrandUpdates>> callBack = new Callback<ArrayList<BrandUpdates>>() {

            @Override
            public void onResponse(Response<ArrayList<BrandUpdates>> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                Log.d(TAG, response.raw() == null ? "null" : response.raw().toString());
                mBus.post(new BrandUpdatesLoadedEvent((ArrayList<BrandUpdates>)response.body(), event.getBrand_id()));

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        Map<String,String> new_option = AccountUtils.getRequestMap(mAppContext);
        if(event.getFilter() != null) {
            new_option.put("lat",String.valueOf(event.getFilter()));
        }

        Call<ArrayList<BrandUpdates>> call = service.getBrandUpdates(event.getBrand_id(), new_option);
        call.enqueue(callBack);
    }

    @Subscribe
    public void onLoadTopBrandUpdates(final LoadTTopBrandUpdatesEvent event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.UpdatesService service = retrofit.create(ShopickApi.UpdatesService.class);
        Callback<ArrayList<BrandUpdates>> callBack = new Callback<ArrayList<BrandUpdates>>() {

            @Override
            public void onResponse(Response<ArrayList<BrandUpdates>> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                Log.d(TAG, response.raw() == null ? "null" : response.raw().toString());
                mBus.post(new TopBrandUpdatesLoadedEvent((ArrayList<BrandUpdates>)response.body()));

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        Map<String,String> new_option = AccountUtils.getRequestMap(mAppContext);
        Call<ArrayList<BrandUpdates>> call = service.getTopBrandUpdates(new_option);
        call.enqueue(callBack);
    }

    @Subscribe
    public void onLoadFeaturedBrandUpdates(final LoadFeaturedBrandUpdatesEvent event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.UpdatesService service = retrofit.create(ShopickApi.UpdatesService.class);
        Callback<ArrayList<BrandUpdates>> callBack = new Callback<ArrayList<BrandUpdates>>() {

            @Override
            public void onResponse(Response<ArrayList<BrandUpdates>> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                Log.d(TAG, response.raw() == null ? "null" : response.raw().toString());
                mBus.post(new FeaturedBrandUpdatesLoadedEvent((ArrayList<BrandUpdates>)response.body()));

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        Map<String,String> new_option = AccountUtils.getRequestMap(mAppContext);
        Call<ArrayList<BrandUpdates>> call = service.getFeaturedBrandUpdates(new_option);
        call.enqueue(callBack);
    }



    @Subscribe
    public void onLoadTopPostCollection(final LoadTopPostCollectionEvent event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.FeedService service = retrofit.create(ShopickApi.FeedService.class);
        Callback<ArrayList<PostCollection>> callBack = new Callback<ArrayList<PostCollection>>() {

            @Override
            public void onResponse(Response<ArrayList<PostCollection>> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                Log.d(TAG, response.raw() == null ? "null" : response.raw().toString());
                mBus.post(new TopPostCollectionLoaded((ArrayList<PostCollection>)response.body()));

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        Map<String,String> new_option = AccountUtils.getRequestMap(mAppContext);
        Call<ArrayList<PostCollection>> call = service.getTopPostCollections(new_option);
        call.enqueue(callBack);
    }


    @Subscribe
    public void onLoadUserPostCollection(final LoadUserPostCollectionEvent event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.FeedService service = retrofit.create(ShopickApi.FeedService.class);
        Callback<ArrayList<PostCollection>> callBack = new Callback<ArrayList<PostCollection>>() {

            @Override
            public void onResponse(Response<ArrayList<PostCollection>> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                Log.d(TAG, response.raw() == null ? "null" : response.raw().toString());
                mBus.post(new PostCollectionLoaded((ArrayList<PostCollection>)response.body()));

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        Map<String,String> new_option = AccountUtils.getRequestMap(mAppContext);
        Call<ArrayList<PostCollection>> call = service.getostCollections(AccountUtils.getShopickProfileId(mAppContext), new_option);
        call.enqueue(callBack);
    }

    @Subscribe
    public void onLoadBrandPostCollection(final LoadBrandFilteredPostCollection event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.FeedService service = retrofit.create(ShopickApi.FeedService.class);
        Callback<ArrayList<PostCollection>> callBack = new Callback<ArrayList<PostCollection>>() {

            @Override
            public void onResponse(Response<ArrayList<PostCollection>> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                Log.d(TAG, response.raw() == null ? "null" : response.raw().toString());
                mBus.post(new PostCollectionLoaded((ArrayList<PostCollection>)response.body()));

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        Map<String,String> new_option = AccountUtils.getRequestMap(mAppContext);
        Call<ArrayList<PostCollection>> call = service.getBrandPostCollections(event.getBrand_id(), new_option);
        call.enqueue(callBack);
    }



    @Subscribe
    public void onLoadProduct(LoadProductEvent event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.ProductService service = retrofit.create(ShopickApi.ProductService.class);
        Callback<Product> callBack = new Callback<Product>() {

            @Override
            public void onResponse(Response<Product> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                Log.d(TAG, response.raw() == null ? "null" : response.raw().toString());
                mBus.post(new ProductLoadedEvent((Product)response.body()));

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        Call<Product> call = service.getProduct(event.getProduct_id(), AccountUtils.getRequestMap(mAppContext));
        call.enqueue(callBack);
    }


    @Subscribe
    public void onLoadReferralCode(LoadReferralCode event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.ReferralService service = retrofit.create(ShopickApi.ReferralService.class);
        Callback<Referral> callBack = new Callback<Referral>() {

            @Override
            public void onResponse(Response<Referral> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                Log.d(TAG, response.raw() == null ? "null" : response.raw().toString());
                mBus.post(new ReferralLoadedEvent((Referral)response.body()));
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        Call<Referral> call = service.getMyCode(AccountUtils.getRequestMap(mAppContext));
        call.enqueue(callBack);
    }


    @Subscribe
    public void onLoadMonthlyCampign(LoadMonthlyCampign event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.PicksService service = retrofit.create(ShopickApi.PicksService.class);
        Callback<Referral> callBack = new Callback<Referral>() {

            @Override
            public void onResponse(Response<Referral> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                Log.d(TAG, response.raw() == null ? "null" : response.raw().toString());
                mBus.post(new MonthlyCampignLoadedEvent((Referral)response.body()));
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        Call<Referral> call = service.getMonthlyCampign(AccountUtils.getRequestMap(mAppContext));
        call.enqueue(callBack);
    }




    @Subscribe
    public void onLoadPicks(LoadPicks event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.PicksService service = retrofit.create(ShopickApi.PicksService.class);
        Callback<com.acquire.shopick.dao.User> callBack = new Callback<com.acquire.shopick.dao.User>() {

            @Override
            public void onResponse(Response<com.acquire.shopick.dao.User> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                Log.d(TAG, response.raw() == null ? "null" : response.raw().toString());
                mBus.post(new PicksLoadedEvent((com.acquire.shopick.dao.User)response.body()));
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        Call<com.acquire.shopick.dao.User> call = service.getMyPicks(AccountUtils.getRequestMap(mAppContext));
        call.enqueue(callBack);
    }

    @Subscribe
    public void onLoadLeaderboard(LoadLeaderboard event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.PicksService service = retrofit.create(ShopickApi.PicksService.class);
        Callback<ArrayList<com.acquire.shopick.dao.User>> callBack = new Callback<ArrayList<com.acquire.shopick.dao.User>>() {

            @Override
            public void onResponse(Response<ArrayList<com.acquire.shopick.dao.User>> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                Log.d(TAG, response.raw() == null ? "null" : response.raw().toString());
                mBus.post(new LeaderboardLoadedEvent((ArrayList<com.acquire.shopick.dao.User>)response.body()));
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        Call<ArrayList<com.acquire.shopick.dao.User>> call = service.getLeaderboard(AccountUtils.getRequestMap(mAppContext));
        call.enqueue(callBack);
    }

    @Subscribe
    public void onLoadEarnPicks(LoadEarnPicks event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.PicksService service = retrofit.create(ShopickApi.PicksService.class);
        Callback<ArrayList<EarnPicks>> callBack = new Callback<ArrayList<EarnPicks>>() {

            @Override
            public void onResponse(Response<ArrayList<EarnPicks>> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                Log.d(TAG, response.raw() == null ? "null" : response.raw().toString());
                mBus.post(new EarnPicksLoadedEvent((ArrayList<EarnPicks>)response.body()));
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        Call<ArrayList<EarnPicks>> call = service.getEarnPicks(AccountUtils.getRequestMap(mAppContext));
        call.enqueue(callBack);
    }


    @Subscribe
    public void onLoadRedeemPicks(LoadRedeemPicks event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.PicksService service = retrofit.create(ShopickApi.PicksService.class);
        Callback<ArrayList<RedeemPick>> callBack = new Callback<ArrayList<RedeemPick>>() {

            @Override
            public void onResponse(Response<ArrayList<RedeemPick>> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                Log.d(TAG, response.raw() == null ? "null" : response.raw().toString());
                mBus.post(new RedeemPicksLoadedEvent((ArrayList<RedeemPick>)response.body()));
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        Call<ArrayList<RedeemPick>> call = service.getRedeemPicks(AccountUtils.getRequestMap(mAppContext));
        call.enqueue(callBack);
    }


    @Subscribe
    public void onLoadRedeemReferralServiceCode(RedeemReferralServiceEvent event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.ReferralService service = retrofit.create(ShopickApi.ReferralService.class);
        Callback<Status> callBack = new Callback<Status>() {

            @Override
            public void onResponse(Response<Status> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                Log.d(TAG, response.raw() == null ? "null" : response.raw().toString());
                mBus.post(new StatusRedeemReferralServiceEvent((Status)response.body()));
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        Call<Status> call = service.redeemReferralService(event.getUsercode(), AccountUtils.getRequestMap(mAppContext));
        call.enqueue(callBack);
    }


    @Subscribe
    public void onLoadCollection(LoadUpdateCollectionEvent event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.UpdatesService service = retrofit.create(ShopickApi.UpdatesService.class);
        Callback<ArrayList<Product>> callBack = new Callback<ArrayList<Product>>() {

            @Override
            public void onResponse(Response<ArrayList<Product>> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                Log.d(TAG, response.raw() == null ? "null" : response.raw().toString());
                mBus.post(new UpdateCollectionLoadedEvent((ArrayList<Product>)response.body()));

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
            Call<ArrayList<Product>> call = service.getUpdateItem(event.getCollection_id(), AccountUtils.getRequestMap(mAppContext));
            call.enqueue(callBack);
    }

    @Subscribe
    public void onLoadPresentationCollection(LoadPresentationCollectionEvent event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.PresentationsService service = retrofit.create(ShopickApi.PresentationsService.class);
        Callback<ArrayList<Product>> callBack = new Callback<ArrayList<Product>>() {

            @Override
            public void onResponse(Response<ArrayList<Product>> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                Log.d(TAG, response.raw() == null ? "null" : response.raw().toString());
                mBus.post(new PresentationCollectionLoadedEvent((ArrayList<Product>)response.body()));

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        Call<ArrayList<Product>> call = service.getPresentationItem(event.getCollection_id(), AccountUtils.getRequestMap(mAppContext));
        call.enqueue(callBack);
    }

    @Subscribe
    public void onLoadCollectionDesc(LoadUpdateDesc event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.UpdatesService service = retrofit.create(ShopickApi.UpdatesService.class);
        Callback<Updates> callBack = new Callback<Updates>() {

            @Override
            public void onResponse(Response<Updates> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                Log.d(TAG, response.raw() == null ? "null" : response.raw().toString());
                mBus.post(new UpdateDescLoaded((Updates)response.body()));

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        Call<Updates> call = service.getUpdateDesc(event.getUpdate_id(), AccountUtils.getRequestMap(mAppContext));
        call.enqueue(callBack);
    }

    @Subscribe
    public void onLoadPresentationCollectionDesc(LoadPresentationDesc event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.PresentationsService service = retrofit.create(ShopickApi.PresentationsService.class);
        Callback<Presentation> callBack = new Callback<Presentation>() {

            @Override
            public void onResponse(Response<Presentation> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                Log.d(TAG, response.raw() == null ? "null" : response.raw().toString());
                mBus.post(new PresentationDescLoaded((Presentation)response.body()));

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        Call<Presentation> call = service.getPresentationDesc(event.getPresentation_id(), AccountUtils.getRequestMap(mAppContext));
        call.enqueue(callBack);
    }

    @Subscribe
    public void onLoadBrands(LoadBrandEvent event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        ShopickApi.BrandsService service = retrofit.create(ShopickApi.BrandsService.class);
        Callback<ArrayList<Brands>> callBack = new Callback<ArrayList<Brands>>() {

            @Override
            public void onResponse(Response<ArrayList<Brands>> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                Log.d(TAG, response.raw() == null ? "null" : response.raw().toString());
                mBus.post(new BrandsLoadedEvent((ArrayList<Brands>)response.body()));

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        if (TextUtils.isEmpty(event.getFilter())) {
            if (event.getBrand_id() != -1) {
                Call<ArrayList<Brands>> call = service.getBrands(event.getUser_id(), event.getBrand_id(), AccountUtils.getRequestMap(mAppContext));
                call.enqueue(callBack);

            } else {
                Call<ArrayList<Brands>> call = service.getBrands(event.getUser_id(), AccountUtils.getRequestMap(mAppContext));
                call.enqueue(callBack);
            }
        } else {
            Call<ArrayList<Brands>> call = service.getBrands(event.getUser_id(), event.getFilter(), AccountUtils.getRequestMap(mAppContext));
            call.enqueue(callBack);
        }
    }



    @Subscribe
    public void onLoadPresentations(LoadPresentationsEvent event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.PresentationsService service = retrofit.create(ShopickApi.PresentationsService.class);
        Callback<ArrayList<Tips>> callBack = new Callback<ArrayList<Tips>>() {

            @Override
            public void onResponse(Response<ArrayList<Tips>> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                Log.d(TAG, response.raw() == null ? "null" : response.raw().toString());
                mBus.post(new PresentationsLoadedEvent((ArrayList<Tips>)response.body()));

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        if (TextUtils.isEmpty(event.getFilter())) {
            Call<ArrayList<Tips>> call = service.getPresentations(event.getUser_id(), AccountUtils.getRequestMap(mAppContext));
            call.enqueue(callBack);
        } else {
            Call<ArrayList<Tips>> call = service.getPresentations(event.getUser_id(), event.getFilter(), AccountUtils.getRequestMap(mAppContext));
            call.enqueue(callBack);
        }
    }

    @Subscribe
    public void onLoadStores(final LoadStoresEvent event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.StoresService service = retrofit.create(ShopickApi.StoresService.class);
        Callback<ArrayList<Store>> callBack = new Callback<ArrayList<Store>>() {

            @Override
            public void onResponse(Response<ArrayList<Store>> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                Log.d(TAG, response.raw() == null ? "null" : response.raw().toString());
                mBus.post(new StoresLoadedEvent((ArrayList<Store>)response.body(), event.getLat() != -1 ));

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        Map<String,String> new_option = AccountUtils.getRequestMap(mAppContext);
        if(event.getLat() != -1) {
            new_option.put("lat",String.valueOf(event.getLat()));
        }
        if(event.getLon() != -1) {
            new_option.put("lon",String.valueOf(event.getLon()));
        }

        Call<ArrayList<Store>> call = service.getStores(new_option);
        call.enqueue(callBack);
    }

    @Subscribe
    public void onLoadFeed(LoadFeedEvent event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.FeedService service = retrofit.create(ShopickApi.FeedService.class);
        Callback<ArrayList<Post>> callBack = new Callback<ArrayList<Post>>() {

            @Override
            public void onResponse(Response<ArrayList<Post>> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                mBus.post(new FeedLoadedEvent((ArrayList<Post>)response.body()));

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        Map<String, String> option_feed = AccountUtils.getRequestMap(mAppContext);
        option_feed.put("post_type", String.valueOf(event.getPostType()));
        option_feed.put("brand_id", String.valueOf(event.getBrand_id()));
        option_feed.put("category_id", event.getFilter());
        Call<ArrayList<Post>> call = service.getOldFeed(event.getId(), option_feed);
        call.enqueue(callBack);
    }


    @Subscribe
    public void onLoaPostCollection(LoadPostCollectionEvent event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.FeedService service = retrofit.create(ShopickApi.FeedService.class);
        Callback<ArrayList<Post>> callBack = new Callback<ArrayList<Post>>() {

            @Override
            public void onResponse(Response<ArrayList<Post>> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                mBus.post(new PostCollectionLoadedEvent((ArrayList<Post>)response.body()));

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        Map<String, String> option_feed = AccountUtils.getRequestMap(mAppContext);
        Call<ArrayList<Post>> call = service.getPostCollection(event.getCollection_id(), option_feed);
        call.enqueue(callBack);
    }


    @Subscribe
    public void onLoadBannerEvent(LoadBannerEvent event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopickApi.FeedService service = retrofit.create(ShopickApi.FeedService.class);
        Callback<ArrayList<Banner>> callBack = new Callback<ArrayList<Banner>>() {

            @Override
            public void onResponse(Response<ArrayList<Banner>> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                mBus.post(new BannersLoadedEvent((ArrayList<Banner>)response.body()));

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
        Map<String, String> option_feed = AccountUtils.getRequestMap(mAppContext);
        Call<ArrayList<Banner>> call = service.getBanners(option_feed);
        call.enqueue(callBack);
    }


    @Subscribe
    public boolean onCreatePost(CreatePostEvent event) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.PROD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ShopickApi.FeedService service = retrofit.create(ShopickApi.FeedService.class);
        Callback<Post> callBack = new Callback<Post>() {

            @Override
            public void onResponse(Response<Post> response, Retrofit retrofit) {
                Log.d(TAG, response.body() == null ? "null": response.body().toString());
                Log.d(TAG, response.raw() == null ? "null" : response.raw().toString());
                mBus.post(new UpdatedNewPostEvent((Post)response.body()));

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());

            }
        };
       // String encodedImage = Base64.encodeToString(getBytesFromBitmap(event.getImage()), Base64.DEFAULT);

        RequestBody image = null;
        if (event.getImage() != null) {
           image = RequestBody.create(MediaType.parse("image/*"), event.getImage());
        }
        RequestBody description = RequestBody.create(MediaType.parse("text/*"), event.getDescription());

       // Call<ArrayList<Feed>> call = service.uploadPost(event.getUser_id(), image, event.getStore_id(),event.getType() ,event.getCategory_id(), description, AccountUtils.getRequestMap(mAppContext));
        //call.enqueue(callBack);
        return true;
    }

    // convert from bitmap to byte array
    public byte[] getBytesFromBitmap(Bitmap bitmap) {


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

}
