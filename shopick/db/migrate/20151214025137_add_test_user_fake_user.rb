class AddTestUserFakeUser < ActiveRecord::Migration
  def change
  	  	add_column :users, :test_user, :boolean
  	  	add_column :users, :fake_user, :boolean
  end
end
