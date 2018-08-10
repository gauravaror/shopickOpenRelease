class CreateReferredUsers < ActiveRecord::Migration
  def change
    create_table :referred_users do |t|
      t.belongs_to :user, index:true
      t.integer :referred_user_id
      t.string  :referred_usercode, index:true
      t.string  :referrer_usercode, index:true
      t.timestamps null: false
    end
  end
end
