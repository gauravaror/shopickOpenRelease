class AddTopToPost < ActiveRecord::Migration
  def change
  	add_column :posts, :top, :boolean, index: true
  end
end
