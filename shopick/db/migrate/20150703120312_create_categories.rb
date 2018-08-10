class CreateCategories < ActiveRecord::Migration
  def change
    create_table :categories do |t|
      t.integer :id_level
      t.integer :pc_parent_id
      t.string :keyword
      t.string :mapped_keyword
      t.string :cat_desc
      t.attachment :category_logo

      t.timestamps
    end
    create_table :product_categorizations do |t|
      t.belongs_to :product, index: true
      t.belongs_to :category, index: true
    end

    

  end
end
