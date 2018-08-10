class CreateUpdates < ActiveRecord::Migration
  def change
    create_table :brand_updates do |t|
      t.belongs_to :product
      t.belongs_to :feature
      t.belongs_to :tip
      t.belongs_to :brand
      t.string :name
      t.text :explaination
      t.belongs_to :category
      t.attachment :update_background
      t.integer :typeupdate
      t.timestamps null: false
    end
  end
end
