ActiveAdmin.register Store do

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

permit_params :name,:address,:phone,:lat,:lon,:brand_id, :location_id, :store_categorization_ids => [], :store_categorizations_attributes => [:id,:_destroy,:category_id], :store_brand_ids => [], :store_brands_attributes => [:id, :_destroy,:brand_id] 

form do |f|
  f.semantic_errors *f.object.errors.keys
  f.inputs "Product Details" do
    f.input :name
    f.input :brand
    f.input :address
    f.input :phone
    f.input :lat
    f.input :lon
    f.input :location
    f.has_many :store_categorizations do |stor_cat|
      stor_cat.input :category, :as => :select, :member_label =>  'mapped_keyword'
      stor_cat.input :_destroy, :as => :boolean, :required => false, :label => 'Remove'

    end
    f.has_many :store_brands do |brand|
      brand.input :brand, :as => :select
      brand.input :_destroy, :as => :boolean, :required => false, :label => 'Remove'

    end
    f.actions
 end
end

index do |ad|
    id_column
    column :name
    column :address
    column :phone
    column :brand_id
    column :lat
    column :lon
    column :location
    column 'brand name' do |br| 
      br.brands.map { |brand_var|
        link_to brand_var.name, admin_brand_path(brand_var)
      }.join("<br/>").html_safe
    end
    column 'categories' do |story|
      story.categories.map { |survey|
        link_to survey.mapped_keyword, admin_category_path(survey)
      }.join("<br/>").html_safe
    end
  actions
end

show do |ad|
  attributes_table do
    row :name
    row :address
    row :phone
    row :brand_id
    row :lat
    row :lon
    row :location
    row 'categories' do |story|
       story.categories.map { |survey|
           link_to survey.mapped_keyword, admin_category_path(survey)
       }.join("<br/>").html_safe
    end
    row 'brand name' do |br| 
      br.brands.map { |brand_var|
        link_to brand_var.name, admin_brand_path(brand_var)
      }.join("<br/>").html_safe
    end  end
 end


end
