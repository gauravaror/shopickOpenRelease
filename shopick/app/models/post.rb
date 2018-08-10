require 'elasticsearch/model'


class Post < ActiveRecord::Base
	has_many :post_categorizations, :dependent => :destroy
    has_many :categories, :through => :post_categorizations
    accepts_nested_attributes_for :post_categorizations, allow_destroy: true
    belongs_to :user
    belongs_to :store
    belongs_to :category
    belongs_to :brand
    has_many :notifications
    has_and_belongs_to_many :users,  :join_table => :posts_likes
    has_many :find_thi
    has_many :read_posts
    has_many :post_collection_posts
    accepts_nested_attributes_for :post_collection_posts, allow_destroy: true
    has_many :post_collection, :through => :post_collection_posts
    include ShowToGender

    scope :post_type, -> (post_type) { where.not post_type: 2 }
  	scope :category_id, -> (category_id) { where category_id: category_id }
  	scope :brand_id, -> (brand_id) { where "stores.brand_id" => brand_id }


    has_attached_file :image, :styles => { :medium => "300x300>", :thumb => "100x100>", :small => "20x20>", large: '600x600>', big: '1000x1000>' }, :default_url => ""
    validates_attachment_content_type :image, :content_type => /\Aimage\/.*\Z/

    include Elasticsearch::Model
  	include Elasticsearch::Model::Callbacks
   settings  :analysis => {
                :filter => {
                  :title_ngram  => {
                    "type"      => "edgeNGram",
                    "min_gram"  => 2,
                    "max_gram"  => 8,
                     }
                },
                :analyzer => {
                  :default => {
                    "tokenizer"    => "standard",
                    "type"         => "custom",
                     "filter" => [ "lowercase", "asciifolding", "title_ngram" ]}
                }
              } do
      mapping  do
      end
    end
def self.update_test_user(post)
	if post.user.test_user
    if post.category && post.category.pc_parent_id  == 12
		  post.user = User.where(fake_user: true, gender: 0).order("RAND()").first
    else
      post.user = User.where(fake_user: true, gender: 1).order("RAND()").first
    end
	end
  if post.store
    post.brand = post.store.brand
  end
end

def self.update_post_collection(post, post_collection_id)
  if  post_collection_id != -1 
    post_col  = PostCollection.find(post_collection_id)
    post_col.post_collection_posts.create(post_id: post.id)
  end
end


def self.update_picks_histories(user_id)
        post_usr = User.find(user_id)
        post_usr.picks_history.create(pickTransaction: 10,
        user_id: post_usr.id,
        pickType: 0,
        globalID:  SecureRandom.uuid,
        processed: false,
        notificationMessage: "Thanks for posting your recent purchase on shopick. We have credited your picks into your account.",
        transactionReason: "Add the picks for posting your recent purchase on shopick: " + post_usr.id.to_s)

      post_usr.picks_history.create(pickTransaction: 10,
        user_id: post_usr.id,
        pickType: 1,
        processed: false,
        globalID:  SecureRandom.uuid,
        notificationMessage: "Thanks for posting your recent purchase on shopick. We have credited your monthly campaign picks into your account.",
       transactionReason: "Add the monthly picks for posting your recent purchase on shopick : " + post_usr.id.to_s)
      post_usr.save
      User.send_admin_users_notification(" posting on shopick  added picks  : "+ post_usr.email+ " user id "+ post_usr.id.to_s,
        "http://shopick.co.in/admin/users?utf8=%E2%9C%93&q%5Bemail_contains%5D="+post_usr.email+"&commit=Filter&order=id_desc")
end

def self.get_posts(post_type, category_id, user_id, brand_id)
	@posts_current =  Post.where(nil) if brand_id.to_i == -1
	@posts_current =  Post.joins(:store).where(nil) if brand_id.to_i != -1
	@posts_current = @posts_current.post_type(post_type) 
	@posts_current = @posts_current.category_id(category_id) if category_id.to_i !=  -1
	@posts_current = @posts_current.brand_id(brand_id) if brand_id.to_i != -1
 	return @posts_current
end

def self.get_new_posts(user_id, post_t)    
      if post_t != -1
        @posts = Post.find_by_sql("select  p.*, (IFNULL(a.affinity_scores,1))/POW(DATEDIFF(now(),p.created_at)+1, 2) as score, IF(r.post_id, 0, 1) as read_ from posts as p left join affinity_scores as a on (a.user_id = "+ user_id + " and a.brand_id = p.brand_id and DATEDIFF(now(),p.created_at) < 20)  left join read_posts as r on (r.user_id =  "+ user_id + " and p.id = r.post_id)  where DATEDIFF(now(),p.created_at) < 20  and  p.post_type = "+ post_t  +" order by read_ desc, score desc, p.created_at desc");
      else 
        @posts = Post.find_by_sql("select  p.*, (IFNULL(a.affinity_scores,1))/POW(DATEDIFF(now(),p.created_at)+1, 2) as score, IF(r.post_id, 0, 1) as read_ from posts as p left join affinity_scores as a on (a.user_id = "+ user_id + " and a.brand_id = p.brand_id and DATEDIFF(now(),p.created_at) < 20)  left join read_posts as r on (r.user_id =  "+ user_id + " and p.id = r.post_id)  where DATEDIFF(now(),p.created_at) < 20 order by read_ desc, score desc, p.created_at desc limit 80");
      end
      return @posts
end

def self.get_collection_tab(user_id)    
 # @posts = Post.find_by_sql("select  p.*, (IFNULL(a.affinity_scores,1))/POW(DATEDIFF(now(),p.created_at)+1, 2) as score, IF(r.post_id, 0, 1) as read_ from posts as p left join affinity_scores as a on (a.user_id = "+ user_id + " and a.brand_id = p.brand_id and DATEDIFF(now(),p.created_at) < 20)  left join read_posts as r on (r.user_id =  "+ user_id + " and p.id = r.post_id)  where DATEDIFF(now(),p.created_at) < 20  and  p.post_type != 2 order by read_ desc, score desc, p.created_at desc");
  @posts = Post.joins("left join affinity_scores as a on (a.user_id = "+ user_id + "  and a.brand_id = posts.brand_id and DATEDIFF(now(),posts.created_at) < 20)").joins("left join read_posts as r on (r.user_id =  " + user_id +" and posts.id = r.post_id)").select("posts.*, (IFNULL(a.affinity_scores,1))/POW(DATEDIFF(now(),posts.created_at)+1, 2) as score, IF(r.post_id, 0, 1) as read_").where("DATEDIFF(now(), posts.created_at) < 20  and  posts.post_type != 2").order("read_ desc, score desc, posts.created_at desc")
  return @posts
end

def as_json(options={})
  super(:only => [:id, :title, :globalID, :post_type, :desc , :price_range_discount_title],
  		:methods => [:image_url, :username, :user_id, :brandname, :brand_logo, :brand_id, :username, :user_image ]

  )
end


def brandname
    brand.name if brand
end

def brand_id
    brand.id if brand
end

def brand_logo
    brand.brand_logo.url(:thumb) if brand
end


def image_url
    image.url(:big)
end

def username
   if user 
	user.name
   end
end

def user_image
   if user 
	user.profileImage
   end
end

def tags
	return_cat = []
	categories.map { |e| return_cat.push(e.id) }
	return_cat
end

def user_id
	if user 
		user.id
	end
end

def storename
    if store 
		store.name
    end
end

def store_id
	if store
		store.id
	end
end

def categoryname
	if category
		category.name
	end
end

def category_id
	if category
		category.id
	end
end


end
Post.__elasticsearch__.create_index!
Post.import