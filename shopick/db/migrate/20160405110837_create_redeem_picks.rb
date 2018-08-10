class CreateRedeemPicks < ActiveRecord::Migration
  def change
    create_table :redeem_picks do |t|
      t.string :title
      t.string :description
      t.text :instruction
      t.string :globalID
      t.integer :requiredPicks
      t.attachment :image_url
      t.boolean :active
      t.timestamps null: false
    end
  end
end
