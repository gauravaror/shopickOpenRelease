class BrandUpdateProduct < ActiveRecord::Base
	belongs_to :product
	belongs_to :brand_update
end
