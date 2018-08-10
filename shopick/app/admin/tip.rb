ActiveAdmin.register Tip do

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
permit_params :name, :desc,:brand_id, :explaination, :globalID, :tip_background, :category_id, :info_tag,:tip_product_ids => [], :tip_products_attributes => [:id,:_destroy,:product_id], :category_ids => [],:categories_attributes => [:mapped_keyword,:id]


before_save do |tip|
    if tip.globalID?
    else
        tip.globalID  = SecureRandom.uuid;
    end
end


form do |f|
  f.inputs "Tip Details" do
  	f.input :name
    f.input :category, :as => :select, :member_label =>  'mapped_keyword'
    f.input :desc
    f.input :explaination
    f.input :info_tag
    f.input :brand
    f.input :tip_background, :required => true, :as => :file,:hint => image_tag(object.tip_background.url(:thumb))
    f.has_many :tip_products do |attr|
        attr.input :product, :as => :select
        attr.input :_destroy, :as => :boolean, :required => false, :label => 'Remove'

  	end
    f.input :categories, :as => :check_boxes, :member_label =>  'mapped_keyword'

  end
  f.actions
 end



index do |ad|
    id_column
    column :category
    column :name
    column :desc
    column :explaination
    column :globalID
    column :brand
    column :info_tag
    column do |event|
      image_tag(event.tip_background.url(:small))
    end
    column "Products" do |m|
         m.products.map {  |feat| 
            link_to feat.title , admin_product_path(feat)
       }.join("<br/><br/>").html_safe
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
  	row :category
  	row :name
    row :desc
    row :explaination
    row :globalID
    row :brand
    row :info_tag
    row :tip_background do
      image_tag(ad.tip_background.url(:thumb))
    end
    row "Products" do |m|
         m.products.map {  |feat| 
            link_to feat.title , admin_product_path(feat) +
            image_tag(feat.product_images.first.product_image.url(:thumb))
       }.join("<br/><br/>").html_safe
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
