ActiveAdmin.register Post do

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

permit_params :title, :image, :description, :hiddenfiledsearch, :show_to_gender,:price_range_discount_title,:likes, :top, :post_type, :order_in_category, :user_id,:brand_id, :store_id, :globalID, :category_id, :category_ids => [],:categories_attributes => [:name,:id],:post_collection_post_ids => [], :post_collection_posts_attributes => [:id,:_destroy,:post_collection_id]

before_save do |post|
    if post.globalID?
    else
        post.globalID  = SecureRandom.uuid;
    end
end

after_save do |post|
    if post.brand_id? 
    else
        if post.store
            post.update(brand_id: post.store.brand_id);
        end
    end
end

form do |f|
  f.semantic_errors *f.object.errors.keys
  f.inputs "Post" do
    f.input :price_range_discount_title
    f.input :image, :required => true, :as => :file
    f.input :hiddenfiledsearch
    f.input :likes
    f.input :post_type
    f.input :globalID
    f.input :brand
    f.input :show_to_gender, :as => :select
    f.input :user, :as => :select, :collection => User.all.map{|u| [u.name, u.id]}
    f.input :store, :as => :select, :collection => Store.all.map{|u| [u.name, u.id]}
    f.input :category, :as => :select, :member_label =>  'mapped_keyword'
    f.input :categories, :as => :check_boxes, :member_label =>  'mapped_keyword'
    f.input :top
    f.has_many :post_collection_posts do |attr|
        attr.input :post_collection, :as => :select, :member_label =>  'id'
        attr.input :_destroy, :as => :boolean, :required => false, :label => 'Remove'
    end

    # Will preview the image when the object is edited
     #f.has_many :categories do |s|
      #s.input :category, :label => "tag"
 	   #end
  f.actions
 end
end





index do |ad|
    id_column
    column :price_range_discount_title
    column :hiddenfiledsearch
    column :likes
    column :post_type
    column :show_to_gender
    column :globalID
    column :brand
    column :order_in_category
    column :store
    column :category
    column :test_user_id
    column :user
    column :top
    column :created_at
    column do |event|
      image_tag(event.image.url(:medium))
    end
    column "post_collection" do |m|
         m.post_collection.map {  |feat| 
            link_to feat.id , admin_post_collection_path(feat)
       }.join("<br/><br/>").html_safe
    end

    actions defaults: true do |post|
      link_to 'Notify', admin_post_path(post)
    end

end


csv do |json|
	id_column
    column :hiddenfiledsearch
    column :likes
    column :post_type
    column :globalID
    column :brand
    column :order_in_category
    column :store
    column :category
    column :user
    column :top
end



show do |ad|
  attributes_table do
    row :price_range_discount_title
    row :hiddenfiledsearch
    row :top
    row :likes
    row :post_type
    row :order_in_category
    row :store
    row :show_to_gender
    row :category
    row :brand
    row :user
    row :test_user_id
    row :globalID
    row :created_at
    row :image do
      image_tag(ad.image.url(:medium))
    end
    # Will display the image on show object page
  end
 end

end
