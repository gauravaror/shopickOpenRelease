class CreateNotificationUsers < ActiveRecord::Migration
  def change
    create_table :notification_users do |t|
      t.belongs_to :notification
      t.belongs_to :user
      t.timestamps null: false
    end
  end
end
