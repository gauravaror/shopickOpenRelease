class AddBrandUpdatesLikes < ActiveRecord::Migration
 def self.up
    create_table :brand_updates_likes do |t|
      t.integer :brand_update_id
      t.integer :user_id
      t.timestamps null: false
    end
    add_index :brand_updates_likes, [:brand_update_id, :user_id]

  end

  def self.down
    drop_table :brand_updates_likes
  end
end
