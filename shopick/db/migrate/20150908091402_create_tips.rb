class CreateTips < ActiveRecord::Migration
  def change
    create_table :tips do |t|
      t.string :name
      t.string :desc
      t.text :explaination
      t.belongs_to :category
      t.attachment :tip_background
      t.string :info_tag
      t.timestamps null: false
    end
  end
end
