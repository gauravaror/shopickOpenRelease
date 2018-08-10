class AddTestUserColumn < ActiveRecord::Migration
  def change
  	add_column :posts, :test_user_id, :int
  end
end