class PostController < ApplicationController
before_filter :authenticate_user_from_token!, :except => [:get_app, :browser_post, :index, :post_get]
before_filter :authenticate_user!, :except => [:get_app, :browser_post, :index, :post_get]

	def index 
    params[:post_type] = -1 if params[:post_type].blank?
    params[:category_id] = -1 if params[:category_id].blank?
    params[:brand_id] = -1 if params[:brand_id].blank?

    ##getting current user for likes
    c_user_email = params[:user_email].presence
    c_user = User.find_by_email(c_user_email)

		@posts = Post.get_posts(params[:post_type], params[:category_id], params[:user_id], params[:brand_id])
		render :json => @posts.order("top DESC, created_at desc").limit(80), each_serializer: FeedSerializer, root: false, c_user: c_user, show: true
	end

  def new_feed
    ##getting current user for likes
    if params[:post_type].blank? ||  params[:post_type] == "-1"
      params[:post_type] = -1
    end
    c_user_email = params[:user_email].presence
    c_user = User.find_by_email(c_user_email)
    @posts = Post.get_new_posts(params[:user_id], params[:post_type])
    render :json => @posts, each_serializer: FeedSerializer, root: false, c_user: c_user, show: true
  end


  def get_user_post
    c_user_email = params[:user_email].presence
    c_user = User.find_by_email(c_user_email)
    posts =Post.get_collection_tab(params[:user_id])
    posts_ = posts.process_show_for_gender(posts, params[:show_offer_collection_for])
    render :json => posts_, each_serializer: FeedSerializer, root: false, c_user: c_user, show: true
  end


  def get_app
    redirect_to ""
  end

  def similar_posts 
    params[:post_type] = -1 if params[:post_type].blank?
    params[:category_id] = -1 if params[:category_id].blank?
    if params[:store_id].blank? ||  params[:store_id] == "-1"
      params[:brand_id] = -1
    else 
      @store = Store.find(params[:store_id])
      if @store
        params[:brand_id] = @store.brand.id
      end
    end 

    ##getting current user for likes
    c_user_email = params[:user_email].presence
    c_user = User.find_by_email(c_user_email)

    @posts = Post.get_posts(params[:post_type], params[:category_id], params[:user_id], params[:brand_id])
    render :json => @posts.order("top DESC, created_at desc").limit(15), each_serializer: FeedSerializer, root: false, c_user: c_user, show: false
  end

  def metaindex
    @posts = Post.all
    render :json =>  { "feeds" => @posts.as_json(:root => false)}.to_json
  end

	def create
		process_image
    params.permit(:globalID)
    params[:test_user_id] = params[:user_id]
    @post = Post.where(globalID: params[:globalID]).first
    if  @post
      @post.update(photo_params)
      Post.update_test_user(@post)
      @post.save
    else
		  @post =  Post.new(photo_params)
      Post.update_test_user(@post)
		  @post.save
    end
    pc_id = params[:post_collection_id].to_i
    if  pc_id != -1
      Post.update_post_collection(@post, pc_id)
    end
    Post.update_picks_histories(params[:test_user_id])
		render :json => @post, root: false
	end

  def photo_params
    params.permit(:image, :store_id, :category_id, :price_range_discount_title, :brand_id, :description, :user_id, :top, :post_type, :globalID, :test_user_id)
  end

def post_exist
  @posts = Post.where(globalID: params[:globalID]).first
  render :json => @posts, root: false
end

def post_get
  @post = Post.where(globalID: params[:globalID]).first
      ##getting current user for likes
    c_user_email = params[:user_email].presence
    c_user = User.find_by_email(c_user_email)
  render :json => @post, root: false, each_serializer: FeedSerializer, root: false, c_user: c_user, show: false
end

def post_get_with_similar
  @post = Post.where(globalID: params[:globalID]).first
      ##getting current user for likes
    c_user_email = params[:user_email].presence
    c_user = User.find_by_email(c_user_email)
  render :json => @post, root: false, each_serializer: PostSerializer, root: false, c_user: c_user, show: false
end

def process_image
  if params[:image]
    data = StringIO.new((params[:image]))
    data.class.class_eval { attr_accessor :original_filename, :content_type }
    data.original_filename = "notrequired.jpeg"
    data.content_type = "image/jpeg"
    params[:image] = data
  end
  if params[:post_type].eql?("-1")
    params[:post_type] = "1"
  end
  if params[:post_collection_id].blank?
    params[:post_collection_id] = "-1"
  end
  if params[:place_index].eql?("stores")
    params[:store_id] = params[:place_id]
  elsif params[:place_index].eql?("brands")
    params[:brand_id] = params[:place_id]
  end
  params[:top] = false
end

def process_description
  if params[:description]
    data = StringIO.new((params[:description]))
    params[:description] = data
  end
end

def browser_post
end


def like_post
  post = Post.where(globalID: params[:globalID]).first
  user_email = params[:user_email].presence
  user = User.find_by_email(user_email)
  if not user.posts.exists?(post)
    user.posts << post 
    AffinityScore.process_post_like(user, post)
  end
  render :json => post
  User.send_admin_users_notification(" user   : "+ user.email + " liked post with globalID : "+ params[:globalID],
    "http://shopick.co.in/admin/posts?utf8=%E2%9C%93&q%5BglobalID_contains%5D="+params[:globalID]+"&commit=Filter&order=id_desc")
end

def read_post
  post = Post.where(globalID: params[:globalID]).first
  user_email = params[:user_email].presence
  user = User.find_by_email(user_email)

  if not user.read_posts.exists?(post_id: post.id)
    user.read_posts.create(post_id: post.id)
  end
  render :json => post
end

def unlike_post
    posts = Post.where(globalID: params[:globalID]).first
    user_email = params[:user_email].presence
    user = User.find_by_email(user_email)
    if user.posts.exists?(posts)
      user.posts.delete(posts)
      AffinityScore.process_post_unlike(user, posts)
    end
    render :json => posts
      User.send_admin_users_notification(" user   : "+ user.email + " unliked post with globalID : "+ params[:globalID],
        "http://shopick.co.in/admin/posts?utf8=%E2%9C%93&q%5BglobalID_contains%5D="+params[:globalID]+"&commit=Filter&order=id_desc")

end


  def find_post
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
      post = Post.where(globalID: params[:globalID]).first
      AffinityScore.process_post_like(queryuser, post)
      FirstNotificationJob.perform_in(10.seconds, 20, queryuser.id)
    end
    User.send_admin_users_findthis_notification(@findthis)
    render :json => @findthis
  end

  def findthis_params
      params.permit(:globalID, :user_id, :post_id, :lat, :lon, :phoneno)
  end



end
