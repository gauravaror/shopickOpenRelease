class AddShowToGender < ActiveRecord::Migration
  def change
  	add_column :posts, :show_to_gender, :integer
  	add_column :post_collections, :show_to_gender, :integer
  	add_column :brand_updates, :show_to_gender, :integer
  	add_column :products, :show_to_gender, :integer
  end
end
