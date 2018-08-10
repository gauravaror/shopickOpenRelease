class AddCategoryToProduct < ActiveRecord::Migration
  def change
  	 add_reference :products, :category
  	 add_reference :post_collections, :category
  end
end
