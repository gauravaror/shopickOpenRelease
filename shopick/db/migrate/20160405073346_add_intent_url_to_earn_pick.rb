class AddIntentUrlToEarnPick < ActiveRecord::Migration
  def change
    add_column :earn_picks, :intentUrl, :string
  end
end
