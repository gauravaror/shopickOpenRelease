class AddFeaturedToBrandUpdates < ActiveRecord::Migration
  def change
    add_column :brand_updates, :featured, :boolean
  end
end
