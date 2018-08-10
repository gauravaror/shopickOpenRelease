class AddTopToBanner < ActiveRecord::Migration
  def change
    add_column :banners, :top, :boolean
  end
end
