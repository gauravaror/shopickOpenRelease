class AddGlobalIdtoPost < ActiveRecord::Migration
  def change
  	add_column :posts, :globalID, :text
  end
end
