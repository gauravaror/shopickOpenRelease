class CreateBrandUpdateProducts < ActiveRecord::Migration
  def change
    create_table :brand_update_products do |t|
      t.belongs_to :product, index: true
      t.belongs_to :brand_update, index: true
      t.timestamps null: false
    end
  end
end
