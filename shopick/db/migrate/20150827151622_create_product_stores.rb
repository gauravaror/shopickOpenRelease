class CreateProductStores < ActiveRecord::Migration
  def change
    create_table :product_stores do |t|
      t.belongs_to :product, index: true
      t.belongs_to :store, index: true
      t.timestamps null: false
    end
  end
end
