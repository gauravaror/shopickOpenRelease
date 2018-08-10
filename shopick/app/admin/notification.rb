ActiveAdmin.register Notification do

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
permit_params :custom_title, :image_url, :intentUrl, :custom_message, :post_id, :brand_update_id, :notification_user_ids => [],:notification_users_attributes => [:name,:user_id, :id]

form do |f|
  f.inputs "Tip Details" do
  	f.input :custom_title
    f.input :custom_message
    f.input :intentUrl
    f.input :image_url, :required => true, :as => :file
    f.input :post, :as => :select
    f.input :brand_update, :as => :select
    f.has_many :notification_users do |attr|
        attr.input :user, :as => :select, :member_label =>  'email'
        attr.input :_destroy, :as => :boolean, :required => false, :label => 'Remove'

  	end
  end
  f.actions
 end



index do |ad|
    id_column
    column :custom_title
    column :custom_message
    column :post
    column :intentUrl
    column :brand_update
    column "Users" do |m|
         m.users.map {  |feat| 
            link_to feat.email , admin_user_path(feat)
       }.join("<br/><br/>").html_safe
    end
    actions defaults: true do |post|
      link_to 'Notify', notify_path(post)
    end

    column do |event|
      image_tag(event.image_url.url(:medium))
    end

end

action_item :view, only: :show do 
  link_to 'Notify ALL', notify_all_path(notification)
end
action_item :view, only: :show do 
  link_to 'Notify MALE', notify_male_path(notification)
end
action_item :view, only: :show do 
  link_to 'Notify FEMALE', notify_female_path(notification)
end


show do |ad|
  attributes_table do
  	row :custom_message
  	row :custom_title
    row :post
    row :intentUrl
    row :brand_update
    row :image_url do |ad|
      image_tag(ad.image_url.url(:thumb))
    end
    row "Users" do |m|
         m.users.map {  |feat| 
            link_to feat.email , admin_user_path(feat)
       }.join("<br/><br/>").html_safe
    end

    # Will display the image on show object page
  end


 end
end
