ActiveAdmin.register Product do


permit_params :sku,:title,:mrp, :price, :globalID, :show_to_gender,:productUrl,:desc, :codAvailable, :visible, :instock, :productDesc, :productBrand, :discount, :size, :sizeUnit, :color, :styleCode, :picks, :category_id, :brand_id, :product_image_ids => [], :product_images_attributes => [:_destroy, :id,:first,:product_image], :product_feature_ids => [], :product_features_attributes => [:_destroy, :id,:feature_id],:product_attribute_value_ids => [], :product_attribute_values_attributes => [:_destroy, :id,:product_attribute_id, :feature_value], :product_store_ids => [], :product_stores_attributes => [:id,:_destroy, :store_id], :product_categorization_ids => [], :product_categorizations_attributes => [:id,:_destroy,:category_id, :_destroy] , :category_ids => [],:categories_attributes => [:name,:_destroy,:id], :product_tag_ids => [], :product_tags_attributes => [:id,:_destroy,:tag_id] , :tag_ids => [],:tag_attributes => [:name,:_destroy,:id],:brand_update_product_ids => [], :brand_update_products_attributes => [:id,:_destroy,:brand_update_id]


before_save do |product|
    if product.globalID?
    else
        product.globalID  = SecureRandom.uuid;
    end
end


form do |f|
  f.semantic_errors *f.object.errors.keys
  f.inputs "Product Details" do
    f.input :title
    f.input :desc
    f.input :visible
    f.input :color
    f.input :mrp
    f.input :show_to_gender, :as => :select
    f.input :discount
    f.input :category
    f.input :brand, :as => :select
    f.has_many :product_categorizations do |cat|
     cat.input :category,:as => :select, :member_label =>  'mapped_keyword'
     cat.input :_destroy, :as => :boolean, :required => false, :label => 'Remove'
    end
    f.has_many :product_images do |photo|
    if photo.object.new_record?
      photo.input :product_image, :as => :file
      photo.input :first
      photo.input :_destroy, :as => :boolean, :required => false, :label => 'Remove'
    else
      photo.input :product_image, :as => :file,:hint => image_tag(photo.object.product_image.url(:thumb))
      photo.input :first
      photo.input :_destroy, :as => :boolean, :required => false, :label => 'Remove'
    end
  end
  f.has_many :brand_update_products do |attr|
        attr.input :brand_update, :as => :select
        attr.input :_destroy, :as => :boolean, :required => false, :label => 'Remove'

    end
  f.actions
 end
end

index do |ad|
    selectable_column
    id_column
    column :title
    column :visible
    column :desc
    column :color
    column :category
    column :mrp
    column :show_to_gender
    column :discount
    column :globalID
    column "Images" do |m|
         m.product_images.each do |img|
            span do
         image_tag(img.product_image.url(:thumb))
        end
     end
    end
    column "Categories" do |m|
         m.product_categorizations.map {  |cat|
            link_to cat.category.mapped_keyword, admin_category_path(cat)
       }.join("<br/>").html_safe
    end
    column "Brand Updates" do |m|
         m.brand_updates.map {  |feat| 
            link_to feat.title , admin_brand_update_path(feat)
       }.join("<br/><br/>").html_safe
    end
  actions
end

filter :categories, :as => :select, :member_label =>  'mapped_keyword', :collection => Category.all
filter :brand
filter :title
filter :desc
filter :globalID
filter :tips

show do |ad|
  attributes_table do
    row :title
    row :productUrl
    row :desc
    row :size
    row :sizeUnit
    row :color
    row :column
    row :globalID
    row :show_to_gender
    row :category
    row :mrp
    row :discount
    row :visible
    row('brand name') { |b| text_node ad.brand.name }
    ad.product_images.each do |photo|
          row :photo do 
            image_tag(photo.product_image.url(:thumb))
          end
    end

    row "ProductAttribute Value" do |m|
         m.product_attribute_values.map {  |feat|
            link_to feat.product_attribute.name + "   :   " +feat.feature_value, admin_product_attribute_path(feat.product_attribute_id)
       }.join("<br/><br/>").html_safe
    end

    row :feature do |m|
      m.product_features.map {  |feat|
            link_to feat.feature.name, admin_feature_path(feat)
       }.join("<br/>").html_safe
    end
    row :store do |m|
       m.product_stores.map { |store|
          link_to store.store.name, admin_store_path(store)
        }.join("<br/>").html_safe
    end
        row "Categories" do |m|
         m.product_categorizations.map {  |cat|
            link_to cat.category.mapped_keyword, admin_category_path(cat)
       }.join("<br/>").html_safe
    end

    end
 end

end
