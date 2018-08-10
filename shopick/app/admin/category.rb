ActiveAdmin.register Category do

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

permit_params :name, :pc_parent_id, :brand_line, :mapped_keyword, :cat_desc,:id_level,:category_logo

form do |f|
  f.inputs "Category Details" do
  	f.input :id_level
    f.input :pc_parent_id, :as => :select, :collection => Category.all.map{|u| ["#{u.mapped_keyword}", u.id]}
    f.input :name
    f.input :mapped_keyword
    f.input :cat_desc
    f.input :category_logo, :required => true, :as => :file
  end
  f.actions
 end



index do |ad|
    id_column
    column :name
    column :id_level
    column :pc_parent_id
    column :mapped_keyword
    column do |event|
      image_tag(event.category_logo.url(:small))
    end
  actions
end



show do |ad|
  attributes_table do
  	row :id_level
  	row :pc_parent_id
    row :name
    row :mapped_keyword
    row :cat_desc
    row :category_logo do
      image_tag(ad.category_logo.url(:thumb))
    end
    # Will display the image on show object page
  end


 end
end
