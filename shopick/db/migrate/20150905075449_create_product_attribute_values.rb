class CreateProductAttributeValues < ActiveRecord::Migration
  def change
    create_table :product_attribute_values do |t|
      t.belongs_to :product_attribute
      t.belongs_to :product
      t.string :feature_value
      t.timestamps null: false
    end
  end
end
