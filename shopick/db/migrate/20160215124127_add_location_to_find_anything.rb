class AddLocationToFindAnything < ActiveRecord::Migration
  def change
  	add_column :findanythings, :lat, :decimal, :precision => 10, :scale => 6
  	add_column :findanythings, :lon, :decimal, :precision => 10, :scale => 6
  	add_column :findanythings, :locality, :string

  end
end
