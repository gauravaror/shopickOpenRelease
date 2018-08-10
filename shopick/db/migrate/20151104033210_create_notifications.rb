class CreateNotifications < ActiveRecord::Migration
  def change
    create_table :notifications do |t|
      t.belongs_to :post
      t.belongs_to :brand_update
      t.string :custom_title
      t.string :custom_message
      t.string :message_id
      t.timestamps null: false
    end
  end
end
