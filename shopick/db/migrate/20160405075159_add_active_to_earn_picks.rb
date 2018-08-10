class AddActiveToEarnPicks < ActiveRecord::Migration
  def change
    add_column :earn_picks, :active, :boolean
  end
end
