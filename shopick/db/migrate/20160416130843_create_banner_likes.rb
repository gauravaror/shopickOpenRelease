class CreateBannerLikes < ActiveRecord::Migration
  def self.up
    create_table :banners_likes do |t|
      t.integer :banner_id
      t.integer :user_id
    end
    add_index :banners_likes, [:banner_id, :user_id]

  end

  def self.down
    drop_table :banners_likes
  end
end