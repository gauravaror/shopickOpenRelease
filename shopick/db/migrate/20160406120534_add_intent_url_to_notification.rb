class AddIntentUrlToNotification < ActiveRecord::Migration
  def change
    add_column :notifications, :intentUrl, :string
  end
end
