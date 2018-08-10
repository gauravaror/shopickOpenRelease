package com.acquire.shopick.io.network;

import com.acquire.shopick.dao.BrandUpdates;
import com.acquire.shopick.dao.Brands;
import com.acquire.shopick.dao.Categories;
import com.acquire.shopick.dao.Post;
import com.acquire.shopick.dao.PostCollection;
import com.acquire.shopick.dao.Store;
import com.acquire.shopick.dao.Tips;
import com.acquire.shopick.io.model.AllOffer;
import com.acquire.shopick.io.model.Banner;
import com.acquire.shopick.io.model.Brand;
import com.acquire.shopick.io.model.EarnPicks;
import com.acquire.shopick.io.model.Feed;
import com.acquire.shopick.io.model.FindThis;
import com.acquire.shopick.io.model.Presentation;
import com.acquire.shopick.io.model.Product;
import com.acquire.shopick.io.model.RedeemPick;
import com.acquire.shopick.io.model.Referral;
import com.acquire.shopick.io.model.SearchResult;
import com.acquire.shopick.io.model.Status;
import com.acquire.shopick.io.model.Updates;
import com.acquire.shopick.io.model.User;
import com.acquire.shopick.util.AccountUtils;
import com.google.gson.JsonElement;
import com.squareup.okhttp.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

/**
 * Created by gaurav on 10/8/15.
 */
public class ShopickApi {


    public interface UserService {
        @Multipart
        @POST("api/v1/user/")
        Call<com.acquire.shopick.dao.User> createUser(@Part("service_token")RequestBody auth,
                                     @Part("email")RequestBody authid,
                                     @Part("name")RequestBody name,
                                     @Part("profileImage")RequestBody profileImage,
                                     @Part("coverImage")RequestBody coverImage,
                                     @Part("gender")int gender,
                                     @Part("age_max") int age_max,
                                     @Part("age_min") int age_min,
                                     @Part("password")RequestBody pass,
                                     @Part("password_confirmation")RequestBody pass_conf,
                                     @Part("instanceID") RequestBody iid,
                                     @Part("service_id") RequestBody id,
                                     @Part("loginType") RequestBody loginType,
                                     @QueryMap Map<String, String> options);

    }

    public interface GCMService {
        @Multipart
        @POST("api/v1/gcm/token")
        Call<com.acquire.shopick.dao.User> postToken(@Part("email")RequestBody authid,
                             @Part("gcm_token")RequestBody gcm_token,
                             @Part("id")RequestBody profile_id,
                             @Part("instanceID") RequestBody iid,
                             @QueryMap Map<String, String> options);

    }

    public interface StoresService {
        @GET("api/v1/stores")
        Call<ArrayList<Store>> getStores(@QueryMap Map<String, String> lat_lang);

    }

    public interface ProductService {
        @GET("api/v1/product_global/{product_id}")
        Call<Product> getProduct(@Path("product_id") String product_id, @QueryMap Map<String, String> option);

        @Multipart
        @POST("api/v1/like_product/")
        Call<Post> likeProduct(@Part("globalID") RequestBody globalID,
                            @QueryMap Map<String,String> option
        );

        @Multipart
        @POST("api/v1/unlike_product/")
        Call<Post> unlikeProduct(@Part("globalID") RequestBody globalID,
                              @QueryMap Map<String,String> option
        );
    }

    public interface UpdatesService {
        @GET("api/v1/updates_global/{update_id}")
        Call<ArrayList<Product>> getUpdateItem(@Path("update_id") String update_id, @QueryMap Map<String, String> option);

        @GET("api/v1/updates_desc_global/{update_id}")
        Call<Updates> getUpdateDesc(@Path("update_id") String update_id, @QueryMap Map<String, String> option);



        @GET("api/v1/updates/{update_id}")
        Call<ArrayList<Product>> getUpdateItem(@Path("update_id") int update_id, @QueryMap Map<String, String> option);

        @GET("api/v1/updates_desc/{update_id}")
        Call<Updates> getUpdateDesc(@Path("update_id") int update_id, @QueryMap Map<String, String> option);


        @GET("api/v1/brand_updates/{brand_id}/")
        Call<ArrayList<Updates>> getBrandUpdates(@Path("brand_id") int brand_id,  @QueryMap Map<String, String> option);

        @GET("api/v1/brand_updates/{brand_id}/")
        Call<ArrayList<BrandUpdates>> getBrandUpdates(@Path("brand_id") Long brand_id,  @QueryMap Map<String, String> option);


        @GET("api/v1/top_brand_updates/")
        Call<ArrayList<BrandUpdates>> getTopBrandUpdates( @QueryMap Map<String, String> option);

        @GET("api/v1/featured_brand_updates/")
        Call<ArrayList<BrandUpdates>> getFeaturedBrandUpdates( @QueryMap Map<String, String> option);


        @GET("api/v1/brand_updates_cat/{brand_id}/{category_id}")
        Call<ArrayList<BrandUpdates>> getBrandUpdatesCat(@Path("brand_id") Long brand_id, @Path("category_id") Long category_id, @QueryMap Map<String, String> option);

        @Multipart
        @POST("api/v1/like_brand_update/")
        Call<Post> likeBrandUpdate(@Part("globalID") RequestBody globalID,
                           @QueryMap Map<String,String> option
        );

        @Multipart
        @POST("api/v1/unlike_brand_update/")
        Call<Post> unlikeBrandUpdate(@Part("globalID") RequestBody globalID,
                             @QueryMap Map<String,String> option
        );
    }


    public interface CategoryService {
        @GET("api/v1/category/")
        Call<ArrayList<Categories>> getCategories(@QueryMap Map<String, String> option);


    }



    public interface FindService {
        @Multipart
        @POST("api/v1/find_post/")
        Call<FindThis> findPost(@Part("globalID") RequestBody globalID,
                            @Part("post_id") Long post_id,
                            @Part("user_id") int user_id,
                            @Part("phoneno") String phoneno,
                                @Part("lat") double lat,
                                @Part("lon") double lon,
                                @QueryMap Map<String,String> option
        );

        @Multipart
        @POST("api/v1/find_post_collection/")
        Call<FindThis> findPostCollection(@Part("globalID") RequestBody globalID,
                                @Part("post_collection_id") Long post_collection_id,
                                @Part("user_id") int user_id,
                                @Part("phoneno") String phoneno,
                                @Part("lat") double lat,
                                @Part("lon") double lon,
                                @QueryMap Map<String,String> option
        );

        @Multipart
        @POST("api/v1/find_banner/")
        Call<FindThis> findBanner(@Part("globalID") RequestBody globalID,
                                @Part("banner_id") Long post_id,
                                @Part("user_id") int user_id,
                                @Part("phoneno") String phoneno,
                                @Part("lat") double lat,
                                @Part("lon") double lon,
                                @QueryMap Map<String,String> option
        );


        @Multipart
        @POST("api/v1/find_product/")
        Call<FindThis> findProduct(@Part("globalID") RequestBody globalID,
                               @Part("product_id") Long product_id,
                               @Part("user_id") int user_id,
                               @Part("phoneno") String phoneno,
                                   @Part("lat") double lat,
                                   @Part("lon") double lon,
                            @QueryMap Map<String,String> option
        );


    }


    public interface SearchService {

        @Multipart
        @POST("api/v1/search_shopick/")
        rx.Observable<ArrayList<SearchResult>> searchShopick(@Part("query") String query,
                                @QueryMap Map<String,String> option
        );

        @Multipart
        @POST("api/v1/search_type/")
        Call<ArrayList<SearchResult>> searchShopickType(@Part("query") String query,
                                                             @QueryMap Map<String,String> option
        );

        @Multipart
        @POST("api/v1/search_location/")
        Call<ArrayList<SearchResult>> searchShopickLocation(@Part("query") String query,
                                                        @QueryMap Map<String,String> option
        );

        @Multipart
        @POST("api/v1/search_postCollection/")
        Call<ArrayList<SearchResult>> searchShopickPostCollection(@Part("query") String query,
                                                            @QueryMap Map<String,String> option
        );

        @Multipart
        @POST("api/v1/search_shopick/")
        Call<ArrayList<SearchResult>> searchShopickPa(@Part("query") String query,
                                                             @QueryMap Map<String,String> option
        );

    }

    public interface ReferralService {
        @GET("api/v1/my_referral_code")
        Call<Referral> getMyCode(@QueryMap Map<String, String> option);

        @Multipart
        @POST("api/v1/redeem_referral_service/")
        Call<Status> redeemReferralService(@Part("usercode") String referralCode,
                                                      @QueryMap Map<String,String> option);
    }


    public interface PicksService {
        @GET("api/v1/get_my_picks")
        Call<com.acquire.shopick.dao.User> getMyPicks(@QueryMap Map<String, String> option);

        @GET("api/v1/get_current_monthly_campign")
        Call<Referral> getMonthlyCampign(@QueryMap Map<String, String> option);


        @GET("api/v1/get_leaderboard")
        Call<ArrayList<com.acquire.shopick.dao.User>> getLeaderboard(@QueryMap Map<String, String> option);

        @GET("api/v1/earn_pick")
        Call<ArrayList<EarnPicks>> getEarnPicks(@QueryMap Map<String, String> option);

        @GET("api/v1/redeem_pick")
        Call<ArrayList<RedeemPick>> getRedeemPicks(@QueryMap Map<String, String> option);

        @Multipart
        @POST("api/v1/user_redeem_pick/")
        Call<RedeemPick> userRedeemProduct(@Part("globalID") RequestBody globalID,
                                   @Part("redeem_pick_id") Long product_id,
                                   @Part("user_id") int user_id,
                                   @Part("phoneno") String phoneno,
                                           @Part("lat") double lat,
                                           @Part("lon") double lon,
                                   @QueryMap Map<String,String> option
        );

    }


    public interface BrandsService {
        @GET("api/v1/brands/{user_id}/{filter}")
        Call<ArrayList<Brands>> getBrands(@Path("user_id") int user_id, @Path("filter") String filter, @QueryMap Map<String, String> option);

        @GET("api/v1/brands/{user_id}/{filter}")
        Call<ArrayList<Brands>> getBrandsCat(@Path("user_id") int user_id, @Path("filter") Long filter, @QueryMap Map<String, String> option);


        @GET("api/v1/brands_withid/{user_id}/{brand_id}")
        Call<ArrayList<Brands>> getBrands(@Path("user_id") int user_id, @Path("brand_id") int brand_id, @QueryMap Map<String, String> option);


        @GET("api/v1/brands_withid/{user_id}/{brand_id}")
        Call<ArrayList<Brands>> getBrands(@Path("user_id") int user_id, @Path("brand_id") Long brand_id, @QueryMap Map<String, String> option);


        @GET("api/v1/brands/{user_id}/")
        Call<ArrayList<Brands>> getBrands(@Path("user_id") int user_id,  @QueryMap Map<String, String> option);

        @GET("api/v1/brands_all/")
        Call<ArrayList<Brands>> getAllBrands( @QueryMap Map<String, String> option);

        @Multipart
        @POST("api/v1/like_brand/")
        Call<Post> likeBrand(@Part("id") Long globalID,
                           @QueryMap Map<String,String> option
        );

        @Multipart
        @POST("api/v1/unlike_brand/")
        Call<Post> unlikeBrand(@Part("id") Long globalID,
                             @QueryMap Map<String,String> option
        );
    }

    public interface PostCollectionService {

        @Multipart
        @POST("api/v1/like_post_collection/")
        Call<Post> likePostCollection(@Part("globalID") RequestBody globalID,
                             @QueryMap Map<String,String> option
        );

        @Multipart
        @POST("api/v1/unlike_post_collection/")
        Call<Post> unlikePostCollection(@Part("globalID") RequestBody globalID,
                               @QueryMap Map<String,String> option
        );
    }


    public interface PresentationsService {
        @GET("api/v1/presentations/{user_id}/{filter}")
        Call<ArrayList<Tips>> getPresentations(@Path("user_id") int user_id, @Path("filter") String filter, @QueryMap Map<String, String> option);

        @GET("api/v1/presentations/{user_id}/")
        Call<ArrayList<Tips>> getPresentations(@Path("user_id") int user_id,  @QueryMap Map<String, String> option);

        @GET("api/v1/presentations/{user_id}/{filter}")
        Call<ArrayList<Tips>> getPresentations(@Path("user_id") int user_id, @Path("filter") Long filter, @QueryMap Map<String, String> option);


        @GET("api/v1/presentation_items_global/{presentation_id}")
        Call<ArrayList<Product>> getPresentationItem(@Path("presentation_id") String presentaton_id, @QueryMap Map<String, String> option);

        @GET("api/v1/presentation_desc_global/{presentation_id}")
        Call<Presentation> getPresentationDesc(@Path("presentation_id") String presentaton_id, @QueryMap Map<String, String> option);



        @GET("api/v1/presentation_items/{presentation_id}")
        Call<ArrayList<Product>> getPresentationItem(@Path("presentation_id") int presentaton_id, @QueryMap Map<String, String> option);

        @GET("api/v1/presentation_desc/{presentation_id}")
        Call<Presentation> getPresentationDesc(@Path("presentation_id") int presentaton_id, @QueryMap Map<String, String> option);

        @Multipart
        @POST("api/v1/like_tip/")
        Call<Post> likeTip(@Part("globalID") RequestBody globalID,
                               @QueryMap Map<String,String> option
        );

        @Multipart
        @POST("api/v1/unlike_tip/")
        Call<Post> unlikeTip(@Part("globalID") RequestBody globalID,
                                 @QueryMap Map<String,String> option
        );

    }

    public interface BannerService {

        @GET("api/v1/getAllOffers/{user_id}")
        Call<ArrayList<AllOffer>> getAllOffers(@Path("user_id") int user_id, @QueryMap Map<String,String> option);

        @Multipart
        @POST("api/v1/like_banner/")
        Call<Post> likeBanner(@Part("globalID") RequestBody globalID,
                           @QueryMap Map<String,String> option
        );

        @Multipart
        @POST("api/v1/unlike_banner/")
        Call<Post> unlikeBanner(@Part("globalID") RequestBody globalID,
                             @QueryMap Map<String,String> option
        );
    }

    public interface FeedService {

        @GET("api/v1/getBanners")
        Call<ArrayList<Banner>> getBanners(@QueryMap Map<String,String> option);


        @GET("api/v1/feed/{user_id}")
        Call<ArrayList<Post>> getOldFeed(@Path("user_id") int user_id, @QueryMap Map<String,String> option);


        @GET("api/v1/get_user_post/{user_id}")
        Call<ArrayList<Post>> getFeed(@Path("user_id") int user_id, @QueryMap Map<String,String> option);


        @GET("api/v1/post_collection/{globalID}")
        Call<ArrayList<Post>> getPostCollection(@Path("globalID") String globalID, @QueryMap Map<String,String> option);



        @GET("api/v1/post_exist/{globalID}")
        Call<Post> checkPostExist(@Path("globalID") String globalID, @QueryMap Map<String,String> option);

        @GET("api/v1/post_get/{globalID}")
        Call<Post> getPostExist(@Path("globalID") String globalID, @QueryMap Map<String,String> option);

        @GET("api/v1/post_similar/")
        Call<ArrayList<Post>> getSimilarPost(@Query("category_id") Long category_id,
                                  @Query("store_id") Long store_id,
                                  @Query("post_type") int post_type,
                                  @QueryMap Map<String,String> option);

        @GET("api/v1/top_post_collection/")
        Call<ArrayList<PostCollection>> getTopPostCollections(@QueryMap Map<String, String> option);


        @GET("api/v1/get_my_collections/{user_id}")
        Call<ArrayList<PostCollection>> getostCollections( @Path("user_id") int user_id,
                                                           @QueryMap Map<String, String> option);

        @GET("api/v1/get_my_collections_brand/{brand_id}")
        Call<ArrayList<PostCollection>> getBrandPostCollections(@Path("brand_id") Long brand_id,
                                                           @QueryMap Map<String, String> option);

        @Multipart
        @POST("api/v1/post/")
        Call<Post> uploadPost(@Part("user_id") Long user_id,
                          @Part("image")RequestBody img,
                          @Part("place_id") Long place_id,
                          @Part("place_index") RequestBody place_index,
                          @Part("post_type") int post_type,
                          @Part("category_id") Long category_id,
                              @Part("post_collection_id") Long post_collection_id,
                              @Part("price_range_discount_title") RequestBody post_collection_title,
                              @Part("description") RequestBody description,
                          @Part("globalID") RequestBody globalID,
                          @Part("epoch") long epoch,
                          @QueryMap Map<String,String> option
        );


        @Multipart
        @POST("api/v1/like_post/")
        Call<Post> likePost(@Part("globalID") RequestBody globalID,
                              @QueryMap Map<String,String> option
        );

        @Multipart
        @POST("api/v1/unlike_post/")
        Call<Post> unlikePost(@Part("globalID") RequestBody globalID,
                            @QueryMap Map<String,String> option
        );

        @Multipart
        @POST("api/v1/read_post/")
        Call<Post> readPost(@Part("globalID") RequestBody globalID,
                            @QueryMap Map<String,String> option
        );
    }



    public static Map<String, String> options = new HashMap<String, String>();

    static {
        options.put("format", "json");
        options.put("nojsoncallback", "1");
        options.put("user_email", "dummy@gmail.com");
        options.put("user_token","CzwSm5ZQ94xqqCS_StTc");
        options.put("distance","5");

    }

}
