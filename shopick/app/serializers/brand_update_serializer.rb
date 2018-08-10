class BrandUpdateSerializer < ActiveModel::Serializer
  attributes :id
  attributes :product_id
  attributes :feature_id
  attributes :tip_id
  attributes :brand_id
  attributes :globalID

  attributes :title
  attributes :description
  attributes :photoUrl
  attributes :image_url
  attributes :category_id
  attributes :mainTag
  attributes :brand_name
  attributes :tags
  attributes :order_in_category
  attributes :type
  attributes :liked



  def title
    object.name
  end

  def description
    object.explaination
  end

  def photoUrl
    object.update_background.url(:big)
  end

  def image_url
    object.update_background.url(:big)
  end

  def category_id
    if object.category
      object.category.id
    end
  end

  def mainTag
    if object.category
      object.category.id
    end
  end

  def tags
    return_cat = []
    object.categories.map { |e| return_cat.push(e.id) }
    return_cat
  end

  def order_in_category
    1
  end

  def brand_name
    if object.brand
      object.brand.name
    end
  end

  def type
    object.typeupdate
  end

  def liked
    puts instance_options[:c_user]
    object.users.include?(instance_options[:c_user])
  end


end
