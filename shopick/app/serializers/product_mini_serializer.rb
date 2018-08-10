class ProductMiniSerializer < ActiveModel::Serializer

  attributes :id
  attributes :name
  attributes :desc
  attributes :globalID
  attributes :discount
  attributes :mrp
  
  attributes :brand_name
  attributes :brand_id
  attributes :productImageUrl
  attributes :order_in_category
  attributes :tags
  attributes :liked
  attributes :description
  attributes :title
  attributes :image_url
  attributes :brand

  def name
    object.title
  end

  def title
    object.title
  end

  def description
    object.desc
  end

  def brand
    object.brand.name
  end

  def brand_id
    object.brand.id
  end
  
  def brand_name
    object.brand.name
  end

 def productImageUrl
    object.product_images.first.product_image.url(:large)
  end

  def image_url
    object.product_images.first.product_image.url(:large)
  end

  def order_in_category
    1
  end

  def tags
    return_cat = []
    object.categories.map { |e| return_cat.push(e.id) }
    return_cat
  end

  def liked
  #puts serialization_options[:c_user]
  object.users.include?(instance_options[:c_user])
end


end
