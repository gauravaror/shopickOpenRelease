class AddLatLonToFindThi < ActiveRecord::Migration
  def change
  	add_column :find_this, :lat, :decimal, :precision => 10, :scale => 6
  	add_column :find_this, :lon, :decimal, :precision => 10, :scale => 6

  end
end
