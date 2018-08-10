class CreateStores < ActiveRecord::Migration
  def change
    create_table :stores do |t|
      t.belongs_to :brand
      t.string :store_name
      t.string :address
      t.string :phone
      t.integer :walkin_picks
      t.integer :scan_picks
      t.integer :purchase_picks
      t.boolean :singlebrand
      t.boolean :companyowned
      t.decimal :lat, :precision => 10, :scale => 6
      t.decimal :lon, :precision => 10, :scale => 6
      t.timestamps
    end

    create_table :store_categorizations do |t|
      t.belongs_to :store, index: true
      t.belongs_to :category, index: true
    end
    
  end
end
