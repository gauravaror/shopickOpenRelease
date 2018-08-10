class CreateUpdateCategorizations < ActiveRecord::Migration
  def change
    create_table :brand_update_categorizations do |t|
      t.belongs_to :category, index: true
      t.belongs_to :brand_update, index: true
      t.timestamps null: false
    end
  end
end
