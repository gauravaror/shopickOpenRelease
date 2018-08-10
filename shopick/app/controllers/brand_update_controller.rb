class BrandUpdateController < ApplicationController
	before_filter :authenticate_user_from_token!, :except => [:featured_brand_updates]
	before_filter :authenticate_user!, :except => [:featured_brand_updates]


	def index
		@update =  BrandUpdate.all.order("created_at desc")
		render :json =>  { "updates" => @update.as_json(:root => false)}.to_json

	end

	def updates_desc
		@update =  BrandUpdate.find( params[:update_id])
		render :json =>  @update, root: false
	end


	def updates_desc_global
		@update =  BrandUpdate.find_by( :globalID => params[:update_id])
		render :json =>  @update, root: false
	end


	def updates
		##getting current user for likes
	    c_user_email = params[:user_email].presence
	    c_user = User.find_by_email(c_user_email)
		@update =  BrandUpdate.where(:brand_id => params[:brand_id]).order("created_at desc")
		render :json =>  @update, root: false, c_user: c_user, each_serializer:  BrandUpdateSerializer
		
	end

	def updates_cat
		##getting current user for likes
	    c_user_email = params[:user_email].presence
	    c_user = User.find_by_email(c_user_email)
		@update =  BrandUpdate.get_brand_updates(params[:filter_id], params[:brand_id]).order("created_at desc")
		render :json =>  @update, root: false, c_user: c_user, each_serializer:  BrandUpdateSerializer
	end


	def top_brand_updates
		##getting current user for likes
	    c_user_email = params[:user_email].presence
	    c_user = User.find_by_email(c_user_email)
	    brand_updates = BrandUpdate.order("created_at desc").limit(10)
	    render :json => brand_updates, root: false, c_user: c_user, each_serializer:  BrandUpdateSerializer
  	end

	def featured_brand_updates
		##getting current user for likes
	    c_user_email = params[:user_email].presence
	    c_user = User.find_by_email(c_user_email)
	    brand_updates = BrandUpdate.where(featured: true).order("created_at desc").limit(20)
	    brand_updates_ = brand_updates.process_show_for_gender(brand_updates, params[:show_offer_collection_for])
	    render :json => brand_updates_, root: false, c_user: c_user, each_serializer:  BrandUpdateSerializer
  	end


  	def like_brand_update
		##getting current user for likes
	  c_user_email = params[:user_email].presence
	  c_user = User.find_by_email(c_user_email)
	  brand_update = BrandUpdate.where(globalID: params[:globalID]).first
	  user_email = params[:user_email].presence
	  user = User.find_by_email(user_email)
	  if  not user.brand_updates.exists?(brand_update)
	  	user.brand_updates << brand_update 
	  	AffinityScore.process_brand_update_like(user, brand_update)
	  end
	  render :json => brand_update, root: false, c_user: c_user
      User.send_admin_users_notification(" user   : "+ user.email + " liked Brand Update with globalID : "+ params[:globalID],
        "http://shopick.co.in/admin/brand_updates/"+brand_update.id.to_s)
	  
	end

	def unlike_brand_update
		##getting current user for likes
	    c_user_email = params[:user_email].presence
	    c_user = User.find_by_email(c_user_email)

	  	brand_update = BrandUpdate.where(globalID: params[:globalID]).first
	    user_email = params[:user_email].presence
	    user = User.find_by_email(user_email)
	    if user.brand_updates.exists?(brand_update)
	      user.brand_updates.delete(brand_update)
	      AffinityScore.process_brand_update_unlike(user, brand_update)
	    end
	    render :json => brand_update, root: false, c_user: c_user
      	User.send_admin_users_notification(" user   : "+ user.email + " unliked BrandUpdate with globalID : "+ params[:globalID],
        "http://shopick.co.in/admin/brand_updates/"+brand_update.id.to_s)
	 
	end

end
