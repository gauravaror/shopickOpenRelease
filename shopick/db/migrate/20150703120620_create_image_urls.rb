class CreateImageUrls < ActiveRecord::Migration
  def change
    create_table :image_urls do |t|
      t.string :dimension
      t.string :imageUrl
      t.belongs_to :product, index: true
      t.timestamps
    end
  end
end
