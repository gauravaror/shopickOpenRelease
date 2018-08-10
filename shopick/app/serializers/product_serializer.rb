class ProductSerializer < ActiveModel::Serializer
  attributes :id
  attributes :desc
  attributes :description
  attributes :name
  attributes :discount
  attributes :mrp
  attributes :title
  attributes :brand_id
  attributes :globalID
  attributes :brand_name
  attributes :productImageUrl
  attributes :image_url
  attributes :order_in_category
  attributes :tags
  attributes :brand_id
  attributes :similar_products
  attributes :stores
  attributes :liked
  #attributes :category
  #attributes :category_id
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

  def brand_id
  	object.brand.id
  end

  def brand
    object.brand.name
  end

#  def category_id
#    object.categories.id
#  end
  
#  def category
#    object.categories.name
#  end
 
  
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

  def similar_products
  	updates = BrandUpdateProduct.where(:product_id => object.id).select(:brand_update_id)
  	Product.joins(:brand_update_products).where('brand_update_products.brand_update_id' => updates).where.not(:id => object.id)
  end

  def stores
    if instance_options[:last_known_lat].blank? || instance_options[:last_known_lon].blank?
  	   Store.joins(:store_brands).where('store_brands.brand_id' => object.brand.id).last(3)
     else
      Store.find_by_sql("SELECT s.*, ( 3959 * acos( cos( radians("+instance_options[:last_known_lat]+") ) * cos( radians( lat ) ) * cos( radians( lon ) - radians("+instance_options[:last_known_lon]+") ) + sin( radians("+instance_options[:last_known_lat]+") ) * sin( radians( lat ) ) ) ) AS distance FROM stores as s left join store_brands as sb on s.id = sb.store_id where sb.brand_id = " + object.brand.id.to_s+ " HAVING distance < 50 ORDER BY distance LIMIT 3 ;");
     end
  end

def liked
  #puts serialization_options[:c_user]
  object.users.include?(instance_options[:c_user])
end

end
