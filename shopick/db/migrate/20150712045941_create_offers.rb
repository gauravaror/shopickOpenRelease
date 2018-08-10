class CreateOffers < ActiveRecord::Migration
  def change
    create_table :offers do |t|
      t.string :title
      t.string :desc

      t.timestamps
    end
  end
end
