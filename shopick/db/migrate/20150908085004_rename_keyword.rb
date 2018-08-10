class RenameKeyword < ActiveRecord::Migration
  def change
  	    rename_column :tags, :keyword, :name
  	    rename_column :categories, :keyword, :name
  	    rename_column :brands, :brand_name, :name
  	    rename_column :stores, :store_name, :name
  	    rename_column :features, :feature_name, :name
  	    rename_column :product_attributes,:key,:name

  end
end
