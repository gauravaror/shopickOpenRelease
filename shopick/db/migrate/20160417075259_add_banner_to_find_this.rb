class AddBannerToFindThis < ActiveRecord::Migration
  def change
  	add_reference :find_this, :banner, index: true
  end
end
