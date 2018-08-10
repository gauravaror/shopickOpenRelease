class CreateTipCategorizations < ActiveRecord::Migration
  def change
    create_table :tip_categorizations do |t|
      t.belongs_to :category, index: true
      t.belongs_to :tip, index: true
      t.timestamps null: false
    end
  end
end
