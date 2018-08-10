class BrandSerializer < ActiveModel::Serializer


  attributes :id
  attributes :name
  attributes :logo_url
  attributes :liked


  def liked
    puts instance_options[:c_user]
    object.users.include?(instance_options[:c_user])
  end


end
