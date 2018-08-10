class AddTokenToUser < ActiveRecord::Migration
  def change
  	add_column :users, :gcm_token, :text
  end
end
