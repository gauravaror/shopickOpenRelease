class AddHiddenFieldToPosts < ActiveRecord::Migration
  def change
  	add_column :posts, :hiddenfiledsearch, :string
  end
end
