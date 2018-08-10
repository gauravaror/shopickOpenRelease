ActiveAdmin.register UserRedeemPick do

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
permit_params :redeem_pick_id, :user_id, :phoneno, :globalID

index do |ad|
    id_column
    column :id
    column :redeem_pick
    column :user
    column :phoneno
    column :globalID
    column :created_at
    actions
end


end
