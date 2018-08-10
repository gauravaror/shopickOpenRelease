ActiveAdmin.register PostCollection do

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


permit_params :title, :description, :featured, :globalID, :category_id, :show_to_gender,:brand_id, :post_banner, :explaination,:post_collection_post_ids => [], :post_collection_posts_attributes => [:id,:_destroy,:post_id]


before_save do |post_collection|
    if post_collection.globalID?
    else
        post_collection.globalID  = SecureRandom.uuid;
    end
end


form do |f|
  f.inputs "Post Collection" do
  	f.input :title
    f.input :description
    f.input :explaination
    f.input :globalID
    f.input :featured
    f.input :category
    f.input :brand
    f.input :show_to_gender, :as => :select
    f.input :post_banner, :required => true, :as => :file,:hint => image_tag(object.post_banner.url(:thumb))
    f.has_many :post_collection_posts do |attr|
        attr.input :post, :as => :select, :member_label =>  'id'
        attr.input :_destroy, :as => :boolean, :required => false, :label => 'Remove'

    end

  end
  f.actions
 end



index do |ad|
    id_column
    column :title
    column :description
    column :explaination
    column :globalID
    column :featured
    column :category
    column :show_to_gender
    column :brand
    column do |event|
      image_tag(event.post_banner.url(:small))
    end
    column "Posts" do |m|
         m.posts.map {  |feat| 
            link_to feat.id , admin_post_path(feat)
       }.join("<br/><br/>").html_safe
    end
  actions
end



show do |ad|
  attributes_table do
  	row :title
  	row :description
    row :explaination
    row :globalID
    row :featured
    row :category
    row :show_to_gender
    row :brand
    row :post_banner do
      image_tag(ad.post_banner.url(:thumb))
    end
    # Will display the image on show object page
  end


 end


end
