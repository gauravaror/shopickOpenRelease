class LeaderboardSerializer < ActiveModel::Serializer

  attributes :email
  attributes :name
  attributes :profileImage
  attributes :picks
  attributes :monthlyPicks
  attributes :usercode
  attributes :rank
end
