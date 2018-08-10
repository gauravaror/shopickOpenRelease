class CreateTipProducts < ActiveRecord::Migration
  def change
    create_table :tip_products do |t|
      t.belongs_to :product, index: true
      t.belongs_to :tip, index: true
      t.timestamps null: false
    end
  end
end
