ActiveAdmin.register RedeemPick do

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


permit_params :title, :description, :instruction, :globalID, :requiredPicks, :image_url, :active


before_save do |redeem_pick|
    if redeem_pick.globalID?
    else
        redeem_pick.globalID  = SecureRandom.uuid;
    end
end

form do |f|
  f.semantic_errors *f.object.errors.keys
  f.inputs "Find Anything" do
    f.input :title
    f.input :image_url, :required => true, :as => :file
    f.input :description
    f.input :instruction
    f.input :globalID
    f.input :requiredPicks
    f.input :active

  f.actions
 end
end



index do |ad|
    id_column
    column :id
    column :title
    column :description
    column :instruction
    column :requiredPicks
    column :globalID
    column :active
    column :created_at
    column do |event|
      image_tag(event.image_url.url(:medium))
    end
    actions

end

show do |ad|
  attributes_table do
    row :id
    row :title
    row :description
    row :instruction
    row :requiredPicks
    row :globalID
    row :active
    row :created_at
    row :image_url do |event|
      image_tag(event.image_url.url(:medium))
    end
  end
 end


end
