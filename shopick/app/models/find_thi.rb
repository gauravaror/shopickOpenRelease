class FindThi < ActiveRecord::Base
	belongs_to :user
    belongs_to :product
    belongs_to :post
    belongs_to :banner
    belongs_to :post_collection
end
