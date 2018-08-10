class BrandUpdateCategorization < ActiveRecord::Base
	belongs_to :brand_update
    belongs_to :category
end
