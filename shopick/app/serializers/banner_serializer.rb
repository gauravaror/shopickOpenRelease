class BannerSerializer < ActiveModel::Serializer
  attributes :id
  attributes :title
  attributes :description
  attributes :globalID
  attributes :intentUrl
  attributes :visible
  attributes :imageUrl

  def imageUrl
  	 object.banner.url(:big) if object.banner
  end
end
