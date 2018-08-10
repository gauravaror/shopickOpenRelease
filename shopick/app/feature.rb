ActiveAdmin.register Feature do

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

permit_params :name,:feature_logo,:short_description,:feature_description,:feature_content,:feature_type, :feature_image_ids => [], :feature_images_attributes => [:id,:feature_image]
form do |f|
  f.semantic_errors *f.object.errors.keys
  f.inputs "Feature Details" do
    f.input :name
    f.input  :feature_logo, :as => :file
    f.input :short_description
    f.input :feature_description
    f.input :feature_content
    f.has_many :feature_images do |photo|
    if photo.object.new_record?
      photo.input :feature_image, :as => :file
    else
      photo.input :feature_image, :as => :file,:hint => image_tag(photo.object.feature_image.url(:thumb))
    end
  end
    f.input :feature_type
    f.actions
 end
end

index do |ad|
    id_column
    column :name
    column "Feature Logo" do |m|
       image_tag(m.feature_logo.url(:thumb))
    end
    column :short_description
    column :feature_description
    column :feature_content
    column "Images" do |m|
         m.feature_images.each do |img|
        span do
         image_tag(img.feature_image.url(:thumb))
        end
     end
    end
    column :feature_type
    actions
end

show do |ad|
  attributes_table do
    row :name
    row :logo do 
      image_tag(ad.feature_logo.url(:thumb))
    end
    row :short_description
    row :feature_description
    row :feature_content
    ad.feature_images.each do |photo|
          row :photo do 
            image_tag(photo.feature_image.url(:thumb))
          end
    end
    row :feature_type
  end
 end

end
