ActiveAdmin.register Banner do

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

permit_params :title, :description,:brand_id, :globalID, :banner,:intentUrl, :visible, :visibleAsOffer, :top

before_save do |updates|
    if updates.top?
    else
        updates.top  = false;
    end
end


before_save do |banner|
    if banner.globalID?
    else
        banner.globalID  = SecureRandom.uuid;
    end
end


form do |f|
  f.inputs "Post Collection" do
  	f.input :title
    f.input :description
    f.input :explaination
    f.input :globalID
    f.input :intentUrl
    f.input :brand
    f.input :visible
    f.input :visibleAsOffer
    f.input :top
    f.input :banner, :required => true, :as => :file,:hint => image_tag(object.banner.url(:thumb))

  end
  f.actions
 end



index do |ad|
    id_column
    column :title
    column :description
    column :explaination
    column :globalID
    column :intentUrl
    column :brand
    column :visible
    column :visibleAsOffer
    column :top
    column do |event|
      image_tag(event.banner.url(:small))
    end
  actions
end



show do |ad|
  attributes_table do
  	row :title
  	row :description
    row :explaination
    row :globalID
    row :brand
    row :intentUrl
    row :visible
    row :visibleAsOffer
    row :top
    row :banner do
      image_tag(ad.banner.url(:thumb))
    end
    # Will display the image on show object page
  end


 end



end
