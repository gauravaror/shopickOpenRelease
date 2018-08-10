class CreateProductAttributes < ActiveRecord::Migration
  def change
    create_table :product_attributes do |t|
      t.belongs_to :category
      t.string :feature_type
      t.string :feature_unit
      t.string :feature_description
      t.string :key
      t.integer :rank
      t.boolean :hidden
      t.timestamps null: false
    end
  end
end
