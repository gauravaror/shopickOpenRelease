class AddImageToNotification < ActiveRecord::Migration
  def up
    add_attachment :notifications, :image_url
  end

  def down
    remove_attachment :notifications, :image_url
  end
  
end
