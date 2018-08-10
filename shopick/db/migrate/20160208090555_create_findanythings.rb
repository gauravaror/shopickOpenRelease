class CreateFindanythings < ActiveRecord::Migration
  def change
    create_table :findanythings do |t|
      t.string :email, null: false, default: ""
      t.string :name, null: false, default: ""
      t.string :phoneno, null: false, default: ""
      t.string :globalID, null: false
      t.text :description
      t.string  :status
      t.attachment :findanything_image
      t.timestamps null: false
    end
  end
end
