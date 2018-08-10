class CreateEarnPicks < ActiveRecord::Migration
  def change
    create_table :earn_picks do |t|
      t.string :title
      t.string :description
      t.text :instruction
      t.integer :givenPicks
      t.timestamps null: false
    end
  end
end