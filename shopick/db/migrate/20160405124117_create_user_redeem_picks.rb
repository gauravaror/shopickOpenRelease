class CreateUserRedeemPicks < ActiveRecord::Migration
  def change
    create_table :user_redeem_picks do |t|
		t.belongs_to :user
	    t.belongs_to :redeem_pick
	    t.string :globalID
	    t.string :phoneno
	    t.timestamps null: false
    end
  end
end
