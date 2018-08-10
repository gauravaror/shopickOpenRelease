class AddGlobalIdtoProductUpdatesTips < ActiveRecord::Migration
  def change
  	 	add_column :tips, :globalID, :text
  	 	add_column :brand_updates, :globalID, :text
  	 	add_column :products, :globalID, :text
  	 	add_index :posts, :globalID, :length => 20
  end
end
