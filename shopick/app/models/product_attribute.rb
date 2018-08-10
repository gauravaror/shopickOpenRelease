class ProductAttribute < ActiveRecord::Base
  belongs_to :category
  has_many :product_attribute_values
end
