class AddIsFeaturedFlagToPostCollection < ActiveRecord::Migration
  def change
    add_column :post_collections, :featured, :boolean
  end
end
