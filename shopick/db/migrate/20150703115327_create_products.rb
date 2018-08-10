class CreateProducts < ActiveRecord::Migration
  def change
    create_table :products do |t|
	    t.belongs_to :brand, index: true
      t.string :sku
      t.string :title
      t.integer :mrp
      t.integer :price
      t.string :productUrl
      t.string :productDesc
      t.string :productBrand
      t.boolean :instock
      t.boolean :codAvailable
      t.integer :discount
      t.integer :size
      t.string :sizeUnit
      t.string :color
      t.string :styleCode
      t.integer :picks
      t.timestamps
    end
  end
end
