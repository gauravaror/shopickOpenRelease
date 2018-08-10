class AddBrandIdToBanner < ActiveRecord::Migration
  def change
  	add_column :banners, :brand_id, :integer
    add_index :banners, :brand_id
  	add_column :posts, :brand_id, :integer
    add_index :posts, :brand_id
    add_column :tips, :brand_id, :integer
    add_index :tips, :brand_id
  end
end
