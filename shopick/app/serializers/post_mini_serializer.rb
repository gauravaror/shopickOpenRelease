class PostMiniSerializer < ActiveModel::Serializer


  attributes :id
  attributes :title
  attributes :globalID
  attributes :image_url


  def image_url
      object.image.url(:big) if object.image
  end


end
