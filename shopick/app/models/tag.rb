class Tag < ActiveRecord::Base
  has_many :product_tags
  has_many :products, :through => :product_tags
end
