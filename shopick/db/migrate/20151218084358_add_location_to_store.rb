class AddLocationToStore < ActiveRecord::Migration
  def change
  	add_reference :stores, :location, index: true
  end
end
