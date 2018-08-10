ActiveAdmin.register PicksHistory do

# See permitted parameters documentation:
# https://github.com/activeadmin/activeadmin/blob/master/docs/2-resource-customization.md#setting-up-strong-parameters
#
# permit_params :list, :of, :attributes, :on, :model
#
# or
#
# permit_params do
#   permitted = [:permitted, :attributes]
#   permitted << :other if resource.something?
#   permitted
# end
permit_params :pickTransaction, :user_id, :globalID, :pickType, :notificationMessage, :transactionReason, :approved, :processed


before_save do |pick_history|
    if pick_history.globalID?
    else
        pick_history.globalID  = SecureRandom.uuid;
    end
end


form do |f|
  f.inputs "Post Collection" do
  	f.input :pickTransaction
    f.input :user_id
    f.input :pickType
    f.input :globalID
    f.input :notificationMessage
    f.input :transactionReason
    f.input :approved
    f.input :processed

  end
  f.actions
 end





index do |ad|
    id_column
    column :pickTransaction
    column :user_id
    column :pickType
    column :globalID
    column :notificationMessage
    column :approved
    column :transactionReason
    column :processed
    column :created_at
  actions
end





action_item :view, only: :show do 
  link_to 'Reject', reject_picks_history_path(picks_history)
end


action_item :view, only: :show do 
  link_to 'Approve', approve_picks_history_path(picks_history)
end

show do |ad|
  attributes_table do
  	row :pickTransaction
  	row :user_id
    row :pickType
    row :globalID
    row :notificationMessage
    row :approved
    row :transactionReason
    row :processed
    row :created_at
    # Will display the image on show object page
  end


 end


end
