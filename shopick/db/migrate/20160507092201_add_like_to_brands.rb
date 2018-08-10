class AddLikeToBrands < ActiveRecord::Migration
  def self.up
    create_table :brands_likes do |t|
      t.integer :brand_id
      t.integer :user_id
      t.timestamps null: false
    end
    add_index :brands_likes, [:brand_id, :user_id]

  end

  def self.down
    drop_table :brands_likes
  end
end
