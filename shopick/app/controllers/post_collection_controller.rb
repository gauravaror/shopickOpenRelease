class PostCollectionController < ApplicationController
before_filter :authenticate_user_from_token!, :except => [:get_my_collections, :post_collection]
before_filter :authenticate_user!, :except => [:get_my_collections, :post_collection]



	def post_collection
	    ##getting current user for likes
	    c_user_email = params[:user_email].presence
	    c_user = User.find_by_email(c_user_email)
	    postcol = PostCollection.find_by(globalID: params[:globalID])
	    render :json => postcol.posts.order("top DESC, created_at desc"), each_serializer: FeedSerializer, root: false, c_user: c_user, show: false
	end


	def top_post_collection
	    ##getting current user for likes
	    c_user_email = params[:user_email].presence
	    c_user = User.find_by_email(c_user_email)
	    postcollection = PostCollection.where(featured: true).order("created_at desc").limit(10)
	    render :json => postcollection, root: false, c_user: c_user, each_serializer: PostCollectionSerializer
	end

	def get_my_collections
	    ##getting current user for likes
	    c_user_email = params[:user_email].presence
	    c_user = User.find_by_email(c_user_email)
	    postcollection = PostCollection.get_post_collections(params[:user_id])
	   	postcollection_ = postcollection.process_show_for_gender(postcollection, params[:show_offer_collection_for])
	    render :json => postcollection_, root: false, c_user: c_user, each_serializer: PostCollectionSerializer
	end

	def get_my_collections_brand
		##getting current user for likes
	    c_user_email = params[:user_email].presence
	    c_user = User.find_by_email(c_user_email)
	    postcollection = PostCollection.get_post_collections_brand(params[:brand_id])
	    render :json => postcollection, root: false, c_user: c_user, each_serializer: PostCollectionSerializer
	end



	def like_post_collection
		##getting current user for likes
	  c_user_email = params[:user_email].presence
	  c_user = User.find_by_email(c_user_email)
	  post_collection = PostCollection.where(globalID: params[:globalID]).first
	  user_email = params[:user_email].presence
	  user = User.find_by_email(user_email)
	  if  not user.post_collections.exists?(post_collection)
	  	user.post_collections << post_collection 
	  	AffinityScore.process_post_collection_like(user, post_collection)
	  end
	  render :json => post_collection, root: false, c_user: c_user
      User.send_admin_users_notification(" user   : "+ user.email + " liked PostCollection with globalID : "+ params[:globalID],
        "http://shopick.co.in/admin/post_collections/"+post_collection.id.to_s)
	  
	end

	def unlike_post_collection
		##getting current user for likes
	    c_user_email = params[:user_email].presence
	    c_user = User.find_by_email(c_user_email)

	  	post_collection = PostCollection.where(globalID: params[:globalID]).first
	    user_email = params[:user_email].presence
	    user = User.find_by_email(user_email)
	    if user.post_collections.exists?(post_collection)
	      user.post_collections.delete(post_collection)
	      AffinityScore.process_post_collection_unlike(user, post_collection)
	    end
	    render :json => post_collection, root: false, c_user: c_user
      	User.send_admin_users_notification(" user   : "+ user.email + " unliked PostCollection with globalID : "+ params[:globalID],
        "http://shopick.co.in/admin/post_collections/"+post_collection.id.to_s)
	 
	end

  def find_post_collection
    @findthis =  FindThi.new(findthis_params)
    @findthis.save
    value  = params[:user_id]
    if value.eql?  "-1"
      value = params[:temp_user_id]
    end
    
    if value.eql? "-1"
    else
      queryuser = User.find(value)
      queryuser.update(:phoneno => params[:phoneno])
      post_collection = PostCollection.where(globalID: params[:globalID]).first
      AffinityScore.process_post_collection_like(queryuser, post_collection)      
      FirstNotificationJob.perform_in(10.seconds, 20, queryuser.id)
    end
    User.send_admin_users_findthis_notification(@findthis)
    render :json => @findthis
  end

  def findthis_params
      params.permit(:globalID, :user_id, :post_collection_id, :lat, :lon, :phoneno)
  end


end
