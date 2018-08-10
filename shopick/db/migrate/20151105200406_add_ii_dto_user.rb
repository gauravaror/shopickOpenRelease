class AddIiDtoUser < ActiveRecord::Migration
  def change
  	add_column :users, :instanceID, :text
  end
end
