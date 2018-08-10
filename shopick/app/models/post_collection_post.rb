class PostCollectionPost < ActiveRecord::Base
	belongs_to :post
	belongs_to :post_collection
end
