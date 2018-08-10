class AddAndroidUniqIdToUser < ActiveRecord::Migration
  def change
  	add_column :users, :uniq_device_id, :string
  	add_index :users, :uniq_device_id
  	add_index :users, :instanceID, :length => 100
  end


end
