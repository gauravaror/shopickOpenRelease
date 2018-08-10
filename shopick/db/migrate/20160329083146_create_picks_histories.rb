class CreatePicksHistories < ActiveRecord::Migration
  def change
    create_table :picks_histories do |t|
 	  t.belongs_to :user, index:true
      t.integer :pickTransaction
      t.string  :transactionReason
      t.boolean  :approved, index:true
      t.timestamps null: false
    end
  end
end
