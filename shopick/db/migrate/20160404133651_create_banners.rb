class CreateBanners < ActiveRecord::Migration
  def change
    create_table :banners do |t|
      t.string :title
      t.string :description
      t.text :explaination
      t.string :globalID
      t.string :intentUrl
      t.boolean :visible
      t.attachment :banner
      t.timestamps null: false
    end
  end
end