ActiveAdmin.register Brand do

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


permit_params :name, :brand_logo, :brand_line, :visibleAsLikable ,:brand_name_mapped, :walkin_picks, :scan_picks, :purchase_picks, :category_ids => [],:categories_attributes => [:mapped_keyword,:id]

form do |f|
  f.semantic_errors *f.object.errors.keys
  f.inputs "Brand Details" do
    f.input :name
    f.input :brand_logo, :required => true, :as => :file
    f.input :brand_line
    f.input :brand_name_mapped
    f.input :walkin_picks
    f.input :scan_picks
    f.input :purchase_picks
    f.input :categories, :as => :check_boxes, :member_label =>  'mapped_keyword'
    f.input :visibleAsLikable
    # Will preview the image when the object is edited
     #f.has_many :categories do |s|
      #s.input :category, :label => "tag"
 	   #end
  f.actions
 end
end



index do |ad|
    id_column
    column :name
    column :brand_line
    column :brand_name_mapped
    column :walkin_picks
    column :scan_picks
    column :purchase_picks
    column :visibleAsLikable
    column 'categories' do |story|
     story.categories.map { |survey|
     link_to survey.mapped_keyword, admin_category_path(survey)
    }.join("<br/>").html_safe
end
    column do |event|
      image_tag(event.brand_logo.url(:small))
    end
  actions
end




show do |ad|
  attributes_table do
    row :name
    row :brand_line
    row :brand_name_mapped
    row :walkin_picks
    row :scan_picks
    row :purchase_picks
    row :visibleAsLikable
    row :brand_logo do |ad|
      image_tag(ad.brand_logo.url(:thumb))
    end
    row 'categories' do |story|
       story.categories.map { |survey|
           link_to survey.mapped_keyword, admin_category_path(survey)
       }.join("<br/>").html_safe
    end
    # Will display the image on show object page
  end
 end

end
