ActiveAdmin.register BrandUpdate do

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

permit_params :name, :explaination, :globalID, :show_to_gender,:update_background, :typeupdate, :featured, :product_id, :tip_id, :feature_id, :category_id, :brand_id, :info_tag,:brand_update_product_ids => [], :brand_update_products_attributes => [:id,:_destroy,:product_id], :category_ids => [],:categories_attributes => [:mapped_keyword,:id]


before_save do |updates|
    if updates.globalID?
    else
        updates.globalID  = SecureRandom.uuid;
    end
end


form do |f|
  f.inputs "Tip Details" do
  	f.input :name
    f.input :category, :as => :select, :member_label =>  'mapped_keyword'
    f.input :explaination
    f.input :update_background, :required => true, :as => :file,:hint => image_tag(object.update_background.url(:thumb))
    f.input :product
    f.input :tip
    f.input :featured
    f.input :brand
    f.input :show_to_gender, :as => :select
    f.input :typeupdate
    f.input :categories, :as => :check_boxes, :member_label =>  'mapped_keyword'
    f.has_many :brand_update_products do |attr|
        attr.input :product, :as => :select
        attr.input :_destroy, :as => :boolean, :required => false, :label => 'Remove'

    end

  end
  f.actions
 end



index do |ad|
    id_column
    column :category
    column :name
    column :explaination
    column :globalID
    column :show_to_gender
    column :typeupdate
    column do |event|
      image_tag(event.update_background.url(:small))
    end
    column :product
    column :featured
    column :brand
    column :tip
    column 'categories' do |story|
     story.categories.map { |survey|
     link_to survey.mapped_keyword, admin_category_path(survey)
    }.join("<br/>").html_safe
    end
    column "Products" do |m|
         m.products.map {  |feat| 
            link_to feat.title , admin_product_path(feat)
       }.join("<br/><br/>").html_safe
    end
  actions
end



show do |ad|
  attributes_table do
  	row :category
  	row :name
    row :explaination
    row :globalID
    row :typeupdate
    row :show_to_gender
    row :update_background do
      image_tag(ad.update_background.url(:thumb))
    end
    row :product
    row :featured
    row :tip
    row :brand
    row 'categories' do |story|
       story.categories.map { |survey|
           link_to survey.mapped_keyword, admin_category_path(survey)
       }.join("<br/>").html_safe
    end
    row "Products" do |m|
         m.products.map {  |feat| 
            link_to feat.title , admin_product_path(feat) +
            image_tag(feat.product_images.first.product_image.url(:thumb))
       }.join("<br/><br/>").html_safe
    end
    # Will display the image on show object page
  end


 end


end
