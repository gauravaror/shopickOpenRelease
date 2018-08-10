class AddVisibleAsofferToBanner < ActiveRecord::Migration
  def change
    add_column :banners, :visibleAsOffer, :boolean
  end
end
