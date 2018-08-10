class CreateStoreBrands < ActiveRecord::Migration
  def change
    create_table :store_brands do |t|
      t.belongs_to :brand, index: true
      t.belongs_to :store, index: true
      t.timestamps 
    end
  end
end
