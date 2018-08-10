class AddImageToUsers < ActiveRecord::Migration
  def change
  	add_column :users, :profileImage, :string
	add_column :users, :coverImage, :string
	add_column :users, :gender, :int
	add_column :users, :age_max, :int
	add_column :users, :age_min, :int
	add_column :users, :shopick_token, :string
  end
end
