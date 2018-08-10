class TipSerializer < ActiveModel::Serializer


  attributes :id
  attributes :title
  attributes :description
  attributes :globalID
  attributes :image_url
  attributes :photoUrl
  attributes :liked
  attributes :name
  attributes :desc
  attributes :explaination
  attributes :category_id
  attributes :type
  attributes :order_in_category


def type
    1
end

def order_in_category
    1
end


def image_url
    object.tip_background.url(:medium) if object.tip_background
end

def photoUrl
    object.tip_background.url(:medium) if object.tip_background
end

def title
  object.name
   
end

def name
  object.name
   
end

def description
  object.explaination
end

def desc
  object.desc
end


def categoryname
    object.category.name if object.category
end

def category_id
    object.category.id if object.category
end

def liked
  puts instance_options[:c_user]
  object.users.include?(instance_options[:c_user])
end

end
