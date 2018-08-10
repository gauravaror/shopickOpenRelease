class CreateFindThis < ActiveRecord::Migration
  def change
    create_table :find_this do |t|
      t.belongs_to :user
      t.belongs_to :post
      t.belongs_to :product
      t.string :globalID
      t.string :phoneno
      t.timestamps null: false
    end
  end
end
