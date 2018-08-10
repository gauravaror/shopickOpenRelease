class AddSignupBoolToUser < ActiveRecord::Migration
  def change
  	add_column :users, :signup, :boolean, default: false
  end
end
