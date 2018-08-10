class AddPriceRangeDiscountTitleToPosts < ActiveRecord::Migration
  def change
    add_column :posts, :price_range_discount_title, :string
  end
end
