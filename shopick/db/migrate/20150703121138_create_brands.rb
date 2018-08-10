class CreateBrands < ActiveRecord::Migration
  def change
    create_table :brands do |t|
      t.string :brand_name
      t.attachment :brand_logo
      t.string :brand_name_mapped
      t.string :brand_line
      t.integer :walkin_picks
      t.integer :scan_picks
      t.integer :purchase_picks
      t.timestamps
    end

    create_table :brand_categorizations do |t|
      t.belongs_to :brand, index: true
      t.belongs_to :category, index: true
    end
  end
end
