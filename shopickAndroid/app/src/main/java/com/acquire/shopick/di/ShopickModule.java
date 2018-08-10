package com.acquire.shopick.di;

import com.acquire.shopick.ShopickApplication;
import com.acquire.shopick.bus.BusProvider;
import com.acquire.shopick.dao.DaoSession;
import com.acquire.shopick.db.DbHelper;
import com.acquire.shopick.job.BrandLikeJob;
import com.acquire.shopick.job.BrandUnLikeJob;
import com.acquire.shopick.job.BrandUpdateLikeJob;
import com.acquire.shopick.job.BrandUpdateUnLikeJob;
import com.acquire.shopick.job.GetAllOffersJob;
import com.acquire.shopick.job.GetBrandUpdatesJob;
import com.acquire.shopick.job.GetBrandsJob;
import com.acquire.shopick.job.GetCategoriesJob;
import com.acquire.shopick.job.GetLocalJob;
import com.acquire.shopick.job.GetPostJob;
import com.acquire.shopick.job.GetPostsJob;
import com.acquire.shopick.job.GetSimilarPostJob;
import com.acquire.shopick.job.GetStoresJob;
import com.acquire.shopick.job.GetTipsJob;
import com.acquire.shopick.job.OfferBannerLikeJob;
import com.acquire.shopick.job.OfferBannerUnLikeJob;
import com.acquire.shopick.job.OpenShopickWhatsAppJob;
import com.acquire.shopick.job.PostCollectionLikeJob;
import com.acquire.shopick.job.PostCollectionUnLikeJob;
import com.acquire.shopick.job.PostLikeJob;
import com.acquire.shopick.job.PostUnLikeJob;
import com.acquire.shopick.job.ProductLikeJob;
import com.acquire.shopick.job.ProductUnLikeJob;
import com.acquire.shopick.job.ReadPostJob;
import com.acquire.shopick.job.ShopickJobManager;
import com.acquire.shopick.job.ShopickPostJob;
import com.acquire.shopick.job.TipLikeJob;
import com.acquire.shopick.job.TipUnLikeJob;
import com.acquire.shopick.job.UpdatePhoneNumberJob;
import com.acquire.shopick.model.ShopickPostModel;
import com.acquire.shopick.model.ShopickStoreModel;
import com.acquire.shopick.ui.ActivityUpdatePhoneNumber;
import com.acquire.shopick.ui.AllOfferFlipAdapter;
import com.acquire.shopick.ui.AllOfferFragment;
import com.acquire.shopick.ui.BrandPickerActivity;
import com.acquire.shopick.ui.CategoryPickerActivity;
import com.acquire.shopick.ui.ChoiceBrowserActivity;
import com.acquire.shopick.ui.EarnPicks;
import com.acquire.shopick.ui.ExploreFragment;
import com.acquire.shopick.ui.ExploreItemFragment;
import com.acquire.shopick.ui.ExploreViewHandler;
import com.acquire.shopick.ui.FeedItemFragment;
import com.acquire.shopick.ui.MainFeedFragment;
import com.acquire.shopick.ui.FeedViewHolder;
import com.acquire.shopick.ui.FindAnythingViewHolder;
import com.acquire.shopick.ui.FragmentContainerActivity;
import com.acquire.shopick.ui.LikedBrandActivity;
import com.acquire.shopick.ui.LikedBrandViewHolder;
import com.acquire.shopick.ui.LocalPostActivity;
import com.acquire.shopick.ui.LocationPickerActivity;
import com.acquire.shopick.ui.LoginActivity;
import com.acquire.shopick.ui.MetaPostItemFragment;
import com.acquire.shopick.ui.MetaPostsViewHolder;
import com.acquire.shopick.ui.PostCollectionActivity;
import com.acquire.shopick.ui.PostFeedItem;
import com.acquire.shopick.ui.PresentationFlipAdapter;
import com.acquire.shopick.ui.PresentationFragment;
import com.acquire.shopick.ui.ProductActivity;
import com.acquire.shopick.ui.ProductCollectionActivity;
import com.acquire.shopick.ui.RedeemPicks;
import com.acquire.shopick.ui.ReedemReferralActivity;
import com.acquire.shopick.ui.ReferAndWinPicksActivity;
import com.acquire.shopick.ui.SearchableActivity;
import com.acquire.shopick.ui.SettingActivity;
import com.acquire.shopick.ui.Shopick;
import com.acquire.shopick.ui.ShowMetaPostsFragment;
import com.acquire.shopick.ui.ThreeCircleMainHomeFragment;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yigit on 2/4/14.
 */
@Module(
        injects = {
                PostFeedItem.class,
                ShopickPostModel.class,
                ShopickPostJob.class,
                Shopick.class,
                LocalPostActivity.class,
                GetLocalJob.class,
                ShopickStoreModel.class,
                GetStoresJob.class,
                GetPostsJob.class,
                LocationPickerActivity.class,
                GetPostJob.class,
                GetSimilarPostJob.class,
                MainFeedFragment.class,
                GetBrandsJob.class,
                BrandPickerActivity.class,
                ExploreFragment.class,
                CategoryPickerActivity.class,
                GetCategoriesJob.class,
                GetBrandUpdatesJob.class,
                ExploreItemFragment.class,
                PresentationFragment.class,
                GetTipsJob.class,
                OpenShopickWhatsAppJob.class,
                FindAnythingViewHolder.class,
                FeedViewHolder.class,
                PostLikeJob.class,
                PostUnLikeJob.class,
                ProductLikeJob.class,
                ProductUnLikeJob.class,
                ProductActivity.class,
                TipLikeJob.class,
                TipUnLikeJob.class,
                PresentationFlipAdapter.class,
                LoginActivity.class,
                ActivityUpdatePhoneNumber.class,
                UpdatePhoneNumberJob.class,
                LikedBrandActivity.class,
                ReedemReferralActivity.class,
                ReferAndWinPicksActivity.class,
                GetAllOffersJob.class,
                AllOfferFragment.class,
                AllOfferFlipAdapter.class,
                OfferBannerLikeJob.class,
                OfferBannerUnLikeJob.class,
                BrandLikeJob.class,
                BrandUnLikeJob.class,
                LikedBrandViewHolder.class,
                ReadPostJob.class,
                ChoiceBrowserActivity.class,
                EarnPicks.class,
                FragmentContainerActivity.class,
                ProductCollectionActivity.class,
                RedeemPicks.class, SearchableActivity.class,
                ThreeCircleMainHomeFragment.class,
                ShowMetaPostsFragment.class,
                MetaPostsViewHolder.class,
                PostCollectionActivity.class,
                SettingActivity.class,
                PostCollectionLikeJob.class,
                PostCollectionUnLikeJob.class,
                ExploreViewHandler.class,
                BrandUpdateLikeJob.class,
                BrandUpdateUnLikeJob.class,
                FeedItemFragment.class,
                MetaPostItemFragment.class

        }
)
public class ShopickModule {


    @Provides
    Bus providesEventBus() {
        return BusProvider.getInstance();
    }

    @Provides
    DaoSession provideDaoSession(DbHelper dbHelper) {
        return dbHelper.getDaoSession();
    }


    @Provides
    @Singleton
    JobManager provideJobManager() {
        return new ShopickJobManager(ShopickApplication.getInstance());
    }


}