Shopick::Application.routes.draw do
  devise_for :users, ActiveAdmin::Devise.config
  begin
      ActiveAdmin.routes(self)
  rescue Exception => e
      puts "ActiveAdmin: #{e.class}: #{e}"
  end

  # The priority is based upon order of creation: first created -> highest priority.
  # See how all your routes lay out with "rake routes".

  # You can have the root of your site routed with "root"
  # root 'welcome#index'

  # Example of regular route:
  #   get 'products/:id' => 'catalog#view'

  # Example of named route that can be invoked with purchase_url(id: product.id)
  #   get 'products/:id/purchase' => 'catalog#purchase', as: :purchase

  # Example resource route (maps HTTP verbs to controller actions automatically):
  #   resources :products
  root :to => 'post#browser_post' 
  post 'api/v1/user/', to: 'user#create_auth'
  get '/api/v1/feed/:user_id/', to: 'post#index'
  get '/api/v1/new_feed/:user_id/', to: 'post#new_feed'
  get '/api/v1/post_similar/', to: 'post#similar_posts'
  post '/api/v1/post/', to: 'post#create'
  get '/api/v1/post_exist/:globalID/', to: 'post#post_exist'
  get '/api/v1/post_get/:globalID/', to: 'post#post_get'
  get '/api/v1/post_get_with_similar/:globalID/', to: 'post#post_get_with_similar'
  get '/apps', to: 'post#get_app'
  get '/api/v1/feed_meta/:user_id', to: 'post#metaindex'
  get '/api/v1/stores_meta/', to: 'store#metaindex'
  get '/api/v1/topcategory/', to: 'category#top_category'
  get '/api/v1/tagcategory/', to: 'category#tag_category'
  get '/api/v1/category/', to: 'category#category'
  get '/api/v1/brands/', to: 'brand#index'
  get '/api/v1/tips/', to: 'tip#index'
  get '/api/v1/featured_brand_updates/', to: 'brand_update#featured_brand_updates'

  get '/browse_post', to: 'post#browser_post'
  get '/offer', to: 'post#browser_post'
  get '/liked_collection', to: 'post#browser_post'
  get '/latest_launch', to: 'post#browser_post'


################ API FOR TOP USED IN THE NEW COLLECTION OF USER ######################

  get '/api/v1/top_post_collection/', to: 'post_collection#top_post_collection'
  get '/api/v1/top_brand_updates/', to: 'brand_update#top_brand_updates'


################ API FOR TOP USED IN THE NEW COLLECTION OF USER ENDSSS ######################


################ API FOR TOP USED Getting Post Collection(Offer) sorted by affinity scores ######################

  
  get '/api/v1/get_my_collections/:user_id', to: 'post_collection#get_my_collections'
  get '/api/v1/get_my_collections_brand/:brand_id', to: 'post_collection#get_my_collections_brand'

  ################ API FOR TOP USED Getting User Post of collection at store sorted by affinity scores ######################
  get '/api/v1/get_user_post/:user_id', to: 'post#get_user_post'



  get '/api/v1/updates/', to: 'brand_update#index'
  get '/api/v1/updates_global/', to: 'brand_update#index'
  get '/api/v1/send_email/', to: 'user#send_email'

  get '/api/v1/search_shopick', to: 'notify#search'
  post '/api/v1/search_shopick', to: 'notify#search'
  get '/api/v1/search_location', to: 'notify#search_location'
  get '/api/v1/search_type', to: 'notify#search_type'
  get '/api/v1/search_postCollection', to: 'notify#search_postCollection'
  post '/api/v1/search_location', to: 'notify#search_location'
  post '/api/v1/search_type', to: 'notify#search_type'
  post '/api/v1/search_postCollection', to: 'notify#search_postCollection'


  ### Referral
  get '/redeem_referral/', to: 'notify#redeem_referral'
  get '/api/v1/my_referral_code', to: 'user#my_referral_code'
  post '/api/v1/redeem_referral_service/', to: 'user#redeem_referral_service'


  ####Picks

  get 'api/v1/get_my_picks', to: 'user#get_my_picks'
  get 'api/v1/get_leaderboard', to: 'user#get_leaderboard'
  get 'api/v1/earn_pick', to: 'pick#earn_pick'
  get 'api/v1/redeem_pick', to: 'pick#redeem_pick'
  post 'api/v1/user_redeem_pick', to: 'pick#user_redeem_pick'
  get 'api/v1/get_current_monthly_campign', to: 'pick#get_current_monthly_campign'

  get 'api/v1/approve_picks_history/:id', to: 'pick#approve_picks_history', as: 'approve_picks_history'
  get 'api/v1/reject_picks_history/:id', to: 'pick#reject_picks_history', as: 'reject_picks_history'

  ### Likes 
  ### Post
  post '/api/v1/like_post', to: 'post#like_post'
  post '/api/v1/unlike_post', to: 'post#unlike_post'
  ### Product
  post '/api/v1/like_product', to: 'products#like_product'
  post '/api/v1/unlike_product', to: 'products#unlike_product'
  ### Tips
  post '/api/v1/like_tip', to: 'tip#like_tip'
  post '/api/v1/unlike_tip', to: 'tip#unlike_tip'
  ### Banners
  post '/api/v1/like_banner', to: 'banner#like_banner'
  post '/api/v1/unlike_banner', to: 'banner#unlike_banner'
  ### Brands
  post '/api/v1/like_brand', to: 'brand#like_brand'
  post '/api/v1/unlike_brand', to: 'brand#unlike_brand'
  ### Post Collection
  post '/api/v1/like_post_collection', to: 'post_collection#like_post_collection'
  post '/api/v1/unlike_post_collection', to: 'post_collection#unlike_post_collection'
  ### Brand Updates
  post '/api/v1/like_brand_update', to: 'brand_update#like_brand_update'
  post '/api/v1/unlike_brand_update', to: 'brand_update#unlike_brand_update'


  #Read
  post '/api/v1/read_post', to: 'post#read_post'


  get 'api/v1/post_collection/:globalID', to: 'post_collection#post_collection'
  get 'api/v1/getBanners', to: 'banner#get_banner'
  get 'api/v1/getAllOffers/:id', to: 'banner#get_all_offers'


  ### FindThis
  post '/api/v1/find_product', to: 'products#find_product'
  post '/api/v1/find_post', to: 'post#find_post'
  post '/api/v1/find_banner', to: 'banner#find_banner'
  post '/api/v1/find_post_collection', to: 'post_collection#find_post_collection'
  


  get '/api/v1/updates_desc_global/:update_id', to: 'brand_update#updates_desc_global'
  get '/api/v1/presentation_desc_global/:presentation_id', to: 'tip#presentation_desc_global'


  get '/api/v1/updates_desc/:update_id', to: 'brand_update#updates_desc'
  get '/api/v1/presentation_desc/:presentation_id', to: 'tip#presentation_desc'
  get '/api/v1/updates/:update_id', to: 'products#updates_products'
  get '/api/v1/updates_global/:update_id', to: 'products#updates_products_global'


  get '/api/v1/presentation_items/:presentation_id', to: 'products#presentation_products'
  get '/api/v1/presentation_items_global/:presentation_id', to: 'products#presentation_products_global'

  post '/api/v1/gcm/token/', to: 'user#update_token'
  get '/api/v1/gcm/notify/:id', to: 'notify#gcm_notification', :as => 'notify'
  get '/api/v1/gcm/notify_all/:id', to: 'notify#gcm_notification_all', :as => 'notify_all'
  get '/api/v1/gcm/notify_male/:id', to: 'notify#gcm_notification_male', :as => 'notify_male'
  get '/api/v1/gcm/notify_female/:id', to: 'notify#gcm_notification_female', :as => 'notify_female'

  get '/api/v1/product/:product_id', to: 'products#product'
  get '/api/v1/product_global/:product_id', to: 'products#product_global'

  get 'privacy', to: 'notify#privacy'

  get '/api/v1/brand_updates/:brand_id/', to: 'brand_update#updates'
  get '/api/v1/brand_updates_cat/:brand_id/:filter_id', to: 'brand_update#updates_cat'
  get '/api/v1/stores/', to: 'store#stores'
  get '/api/v1/closest/:lat/:lon', to: 'store#closest_stores'

  get '/api/v1/presentations/:user_id/:filter', to: 'tip#presentations'
  get '/api/v1/presentations/:user_id', to: 'tip#presentations'
  get '/api/v1/brands/:user_id/:filter', to: 'brand#updates'
  get '/api/v1/brands_withid/:user_id/:brand_id', to: 'brand#updates'
  get '/api/v1/brands/:user_id', to: 'brand#updates'
  get '/api/v1/brands_all/', to: 'brand#brands_all'
  
  authenticate :user do

    mount Resque::Server, :at => "/resque"
    require 'sidekiq/web'
    mount Sidekiq::Web => '/sidekiq'
  end

  get 'findanything', to: 'notify#findanything'
  post 'findpath', to: 'notify#findpath', as: 'findanythings'

  get 'findpath', to: 'notify#findpath'
  post 'finishfindanything/', to: 'notify#completedfindanything', as: 'completefindanything'
  get 'finishfindanything/:id', to: 'notify#completedfindanything'
  get 'collection/:id', to: 'post#browser_post'
  get 'updates/:id', to: 'post#browser_post'
  get 'product/:product_id', to: 'post#browser_post'
  get 'post/:post_id', to: 'post#browser_post'

unauthenticated do
  
  #get 'findanything', to: 'notify#findanything'
  #=post 'findpath', to: 'notify#findpath', as: 'findanythings'
  get 'presentation/:presentation_id', to: redirect("https://play.google.com/store/apps/details?id=com.acquire.shopick")
end

end
