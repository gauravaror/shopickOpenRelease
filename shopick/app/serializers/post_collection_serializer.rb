class PostCollectionSerializer < ActiveModel::Serializer
  attributes :id
  attributes :title
  attributes :description
  attributes :globalID
  attributes :featured
  attributes :brand_name
  attributes :brand_logo
  attributes :brand_id
  attributes :post_banner
  attributes :posts
  attributes :liked

  def post_banner
  	 object.post_banner.url(:big) if object.post_banner
  end

  def brand_name
     object.brand.name if object.brand
  end


  def brand_logo
     object.brand.brand_logo.url(:thumb) if object.brand
  end

  def brand_id
     object.brand.id if object.brand
  end

  def posts
    object.posts.order("created_at desc").limit(4)
  end

  def liked
    puts instance_options[:c_user]
    object.users.include?(instance_options[:c_user])
  end


end
