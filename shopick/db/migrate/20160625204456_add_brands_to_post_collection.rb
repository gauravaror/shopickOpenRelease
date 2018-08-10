class AddBrandsToPostCollection < ActiveRecord::Migration
  def change
  	add_column :post_collections, :brand_id, :integer
    add_index :post_collections, :brand_id
  end
end
