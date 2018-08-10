class CreateFeatures < ActiveRecord::Migration
  def change
    create_table :features do |t|
      t.string :feature_name
      t.attachment :feature_logo
      t.string :short_description
      t.string :feature_description
      t.text :feature_content
      t.integer :feature_type
      t.boolean :keyfeature
      t.timestamps 
    end

    create_table :product_features do |t|
      t.belongs_to :product, index: true
      t.belongs_to :feature, index: true
    end

  end
end