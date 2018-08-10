class CreateProductImages < ActiveRecord::Migration
  def change
    create_table :product_images do |t|
      t.belongs_to :product, index: true
      t.attachment :product_image
      t.boolean :first
      t.timestamps null: false
    end
  end
end
