ActiveAdmin.register ProductAttribute do

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

#       t.belongs_to :category
#       t.string :type
#       t.string :key
#       t.integer :rank
#       t.boolean :hidden
#end

permit_params :name, :rank, :hidden, :category_id, :feature_type, :feature_unit, :feature_description

form do |f|
  f.inputs "Product Attribute Details" do
  	f.input :feature_type
    f.input :name
    f.input :rank
    f.input :hidden
    f.input :feature_unit
    f.input :feature_description
    f.input :category,:as => :select, :member_label =>  'mapped_keyword'
  end
  f.actions
 end



index do |ad|
    id_column
    column :feature_type 
    column :name
    column :rank
    column :hidden
    column :category
    column :feature_unit
    column :feature_description
  actions
end



show do |ad|
  attributes_table do
  	row :feature_type
  	row :name
    row :rank
    row :hidden
    row :category
    row :feature_unit
    row :feature_description
  end


 end
end
