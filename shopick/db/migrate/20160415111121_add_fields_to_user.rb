class AddFieldsToUser < ActiveRecord::Migration
  def change
    add_column :users, :service_id, :string
    add_column :users, :loginType, :string
    add_column :users, :service_token, :text
  end
end
