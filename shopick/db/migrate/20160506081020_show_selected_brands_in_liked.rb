class ShowSelectedBrandsInLiked < ActiveRecord::Migration
  def change
  	add_column :brands, :visibleAsLikable, :boolean
  end
end
