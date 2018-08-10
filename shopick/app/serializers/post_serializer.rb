class PostSerializer < ActiveModel::Serializer


  attributes :id
  attributes :title
  attributes :description
  attributes :price_range_discount_title
  attributes :post_type
  attributes :globalID
  attributes :top
  attributes :name
  attributes :image_url
  attributes :username
  attributes :user_id
  attributes :storename
  attributes :store_id
  attributes :category_id
  attributes :categoryname
  attributes :brand_id
  attributes :brandname
  attributes :brand_logo
  attributes :user_image
  attributes :liked
  attributes :similar_posts
  attributes :stores

def name
  object.description
end

def description
 object.price_range_discount_title
end

def image_url
    object.image.url(:big) if object.image
end

def username
  object.user.name if object.user
   
end

def user_image
  object.user.profileImage if object.user
end


def user_id
    object.user.id if object.user
end

def storename
    object.store.name if object.store
end

def store_id
   object.store.id if object.store
end

def categoryname
    object.category.name if object.category
end

def category_id
    object.category.id if object.category
end

def brandname
    object.brand.name if object.brand
end

def brand_id
    object.brand.id if object.brand
end

def brand_logo
    object.brand.brand_logo.url(:thumb) if object.brand
end


  def similar_posts
    collection = PostCollectionPost.where(:post_id => object.id).select(:post_collection_id)
    posts = Post.joins(:post_collection_posts).where('post_collection_posts.post_collection_id' => collection).where.not(:id => object.id)
    if posts.size == 0 
      posts = Post.get_posts(-1, -1, -1, object.brand_id).where.not(:id => object.id).order("top DESC, created_at desc").limit(8)
    end
    posts
  end

  def stores
    if object.brand
      if instance_options[:last_known_lat].blank? || instance_options[:last_known_lon].blank?
         Store.joins(:store_brands).where('store_brands.brand_id' => object.brand.id).last(3)
       else
        Store.find_by_sql("SELECT s.*, ( 3959 * acos( cos( radians("+instance_options[:last_known_lat]+") ) * cos( radians( lat ) ) * cos( radians( lon ) - radians("+instance_options[:last_known_lon]+") ) + sin( radians("+instance_options[:last_known_lat]+") ) * sin( radians( lat ) ) ) ) AS distance FROM stores as s left join store_brands as sb on s.id = sb.store_id where sb.brand_id = " + object.brand.id.to_s+ " HAVING distance < 50 ORDER BY distance LIMIT 3 ;");
       end
     else
        a = []
        a
     end
  end



def liked
  puts instance_options[:c_user]
  object.users.include?(instance_options[:c_user])
end


end
