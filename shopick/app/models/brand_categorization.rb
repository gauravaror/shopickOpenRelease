class BrandCategorization < ActiveRecord::Base
    belongs_to :brand
    belongs_to :category
end
