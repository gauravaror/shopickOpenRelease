class AddFieldsToPickHistories < ActiveRecord::Migration
  def change
  	add_column :picks_histories, :notificationMessage, :string
  	add_column :picks_histories, :pickType, :integer
  	add_column :picks_histories, :processed, :boolean
	add_column :picks_histories, :globalID, :string
	add_column :picks_histories, :rejectionTitle, :string
  end
end
