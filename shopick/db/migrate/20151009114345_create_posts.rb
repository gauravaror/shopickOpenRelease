class CreatePosts < ActiveRecord::Migration
  def change
    create_table :posts do |t|
    	t.belongs_to :user
    	t.belongs_to :store
    	t.belongs_to :category
      t.attachment :image
      t.string :title
      t.string :description
      t.integer :likes
      t.integer :typepost
      t.integer :order_in_category 
      t.timestamps null: false
    end

    create_table :post_categorizations do |t|
      t.belongs_to :post, index: true
      t.belongs_to :category, index: true
    end
  end
end
