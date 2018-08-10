class AddPicksToUser < ActiveRecord::Migration
  def change
  	add_column :users, :picks, :decimal
  	add_column :users, :monthlyPicks, :decimal
  end
end
