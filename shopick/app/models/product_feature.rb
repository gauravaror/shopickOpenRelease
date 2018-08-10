class ProductFeature < ActiveRecord::Base
    belongs_to :product
    belongs_to :feature
end
