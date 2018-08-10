ActiveAdmin.register FindThi do

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



permit_params :product_id, :globalID, :phoneno, :post_id, :user_id

form do |f|
  f.semantic_errors *f.object.errors.keys
  f.inputs "Find This" do
    f.input :product
    f.input :globalID
    f.input :phoneno
    f.input :post
    f.input :user
    f.input :banner
    f.input :post_collection
  f.actions
 end
end



index do |ad|
    id_column
    column :product
    column :user
    column :post
    column :banner
    column :post_collection
    column :phoneno
    column :globalID
    column :lat
    column :lon
    column :created_at
    actions

end

show do |ad|
  attributes_table do
    row :product
    row :user
    row :post
    row :banner
    row :post_collection
    row :phoneno
    row :globalID
    row :lat
    row :lon
    row :created_at
  end
 end

end
