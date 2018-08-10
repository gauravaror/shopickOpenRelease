ActiveAdmin.register User do
  permit_params :email, :password, :usercode, :referred,:picks,:monthlyPicks, :password_confirmation, :name, :gender, :age_max, :age_min, :profileImage, :coverImage, :test_user, :fake_user, :phoneno
 
action_item :view do
  #Resque.enqueue(NotificationJob,"http://partner.coupik.com/system/brand_updates/update_backgrounds/000/000/030/medium/2.jpg?1445590682","http://partner.coupik.com/system/brand_updates/update_backgrounds/000/000/054/thumb/1.jpg?1445926289","Wrangler Jeans Collection","Latest Rider collection for riders.",["ft0uRgZUmhg:APA91bHxl_nyITF_iiPJlp9WR4YfkQaOfxph2eBZvCB_5_3McrBO70rrIA0GcfwtL4I-a8DOyn1LwK5j1ammKSnq2-qBtIMlh6L9Y_gvRG2K86a2JowNboI7eSMNhDLR5t1s5xqnuANj"]);

end

  index do
    selectable_column
    id_column
    column :name
    column :email
    column :profileImage
    column :coverImage
    column :gender
    column :age_max
    column :age_min
    column :current_sign_in_at
    column :sign_in_count
    column :created_at
    column :instanceID
    column :test_user
    column :fake_user
    column :phoneno
    column :usercode
    column :referred
    column :monthlyPicks
    column :picks
    column :signup
    column :service_id
    column :loginType
    column :uniq_device_id
    actions
  end

  filter :name
  filter :email
  filter :gender
  filter :age_min
  filter :age_max
  filter :current_sign_in_at
  filter :sign_in_count
  filter :created_at
  filter :loginType
  filter :service_id
  filter :instanceID
  filter :uniq_device_id

  form do |f|
    f.inputs "Admin Details" do
      f.input :name
      f.input :email
      f.input :gender
      f.input :age_max
      f.input :age_min
      f.input :profileImage
      f.input :coverImage
      f.input :password
      f.input :usercode
      f.input :referred
      f.input :password_confirmation
      f.input :test_user
      f.input :fake_user
      f.input :phoneno
      f.input :uniq_device_id
      f.input :signup
      f.input :instanceID
      f.input :monthlyPicks
      f.input :picks

    end
    f.actions
  end

end
