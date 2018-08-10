class CreateFeatureImages < ActiveRecord::Migration
  def change
    create_table :feature_images do |t|
      t.belongs_to :feature, index: true
      t.attachment :feature_image
      t.boolean :first
      t.timestamps null: false
    end
  end
end
