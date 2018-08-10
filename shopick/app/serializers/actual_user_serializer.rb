class ActualUserSerializer < ActiveModel::Serializer
  attributes :id
  attributes :email
  attributes :name
  attributes :profileImage
  attributes :coverImage
  attributes :gender
  attributes :coverImage
  attributes :instanceID
  attributes :usercode
  attributes :referred
  attributes :picks
  attributes :monthlyPicks
end
