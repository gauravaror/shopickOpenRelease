class AddUserCodeToUsers < ActiveRecord::Migration
  def change
  	add_column :users, :usercode, :string
  	add_column :users, :referred, :boolean
  end
end
