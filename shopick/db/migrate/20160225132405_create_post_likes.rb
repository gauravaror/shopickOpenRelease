class CreatePostLikes < ActiveRecord::Migration
  def self.up
    create_table :posts_likes do |t|
      t.integer :post_id
      t.integer :user_id
    end

    add_index :posts_likes, [:post_id, :user_id]

  end

  def self.down
    drop_table :posts_likes
  end

end
