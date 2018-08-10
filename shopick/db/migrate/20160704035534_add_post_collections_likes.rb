class AddPostCollectionsLikes < ActiveRecord::Migration
 def self.up
    create_table :post_collections_likes do |t|
      t.integer :post_collection_id
      t.integer :user_id
      t.timestamps null: false
    end
    add_index :post_collections_likes, [:post_collection_id, :user_id]

  end

  def self.down
    drop_table :post_collections_likes
  end
end
