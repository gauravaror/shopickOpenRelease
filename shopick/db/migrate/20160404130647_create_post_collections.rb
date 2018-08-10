class CreatePostCollections < ActiveRecord::Migration
  def change
    create_table :post_collections do |t|
      t.string :title
      t.string :description
      t.text :explaination
      t.string :globalID
      t.attachment :post_banner
      t.timestamps null: false
    end
  end
end
