class CreatePostCollectionPosts < ActiveRecord::Migration
  def change
    create_table :post_collection_posts do |t|
      t.belongs_to :post, index: true
      t.belongs_to :post_collection, index: true
      t.timestamps null: false
    end
  end
end
