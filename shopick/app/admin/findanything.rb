ActiveAdmin.register Findanything do

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


permit_params :name, :email, :phoneno, :description, :findanything_image, :globalID, :locality

before_save do |findanything|
    if findanything.globalID?
    else
        findanything.globalID  = SecureRandom.uuid;
    end
end

form do |f|
  f.semantic_errors *f.object.errors.keys
  f.inputs "Find Anything" do
    f.input :name
    f.input :findanything_image, :required => true, :as => :file
    f.input :description
    f.input :email
    f.input :phoneno
    f.input :locality
    f.input :globalID
  f.actions
 end
end



index do |ad|
    id_column
    column :name
    column :description
    column :email
    column :phoneno
    column :globalID
    column :locality
    column :created_at
    column do |event|
      image_tag(event.findanything_image.url(:medium))
    end
    actions

end

show do |ad|
  attributes_table do
    row :name
    row :description
    row :email
    row :phoneno
    row :globalID
    row :locality
    row :findanything_image do |event|
      image_tag(event.findanything_image.url(:medium))
    end
    row :created_at
  end
 end


end
