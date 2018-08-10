class AddPostCollectionToFindThis < ActiveRecord::Migration
  def change
  	add_reference :find_this, :post_collection, index: true
  end
end
