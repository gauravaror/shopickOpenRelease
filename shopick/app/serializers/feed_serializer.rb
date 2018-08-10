class FeedSerializer < ActiveModel::Serializer


  attributes :id
  attributes :title
  attributes :description
  attributes :post_type
  attributes :globalID

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
  attributes :featured_in_globalID
  attributes :featured_in_title


def image_url
    object.image.url(:medium) if object.image
end

def description
 object.price_range_discount_title
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



def liked
  puts instance_options[:c_user]
  object.users.include?(instance_options[:c_user])
end


def featured_in_globalID
  if object.post_collection.where(featured: true).first && instance_options[:show]
    object.post_collection.where(featured: true).first.globalID
  end
end

def featured_in_title
  if object.post_collection.where(featured: true).first && instance_options[:show]
    object.post_collection.where(featured: true).first.title
  end
end

end
