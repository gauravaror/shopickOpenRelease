class AddLikeToProducts < ActiveRecord::Migration
  def self.up
    create_table :products_likes do |t|
      t.integer :product_id
      t.integer :user_id
    end

    add_index :products_likes, [:product_id, :user_id]

  end

  def self.down
    drop_table :products_likes
  end
end
