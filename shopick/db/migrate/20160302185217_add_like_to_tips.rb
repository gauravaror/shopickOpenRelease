class AddLikeToTips < ActiveRecord::Migration
  def self.up
    create_table :tips_likes do |t|
      t.integer :tip_id
      t.integer :user_id
    end

    add_index :tips_likes, [:tip_id, :user_id]

  end

  def self.down
    drop_table :tips_likes
  end
end
