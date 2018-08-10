class Tip < ActiveRecord::Base
  has_many :tip_products
  accepts_nested_attributes_for :tip_products, allow_destroy: true
  has_many :products, :through => :tip_products
  has_many :brand_updates
  belongs_to :brand
  belongs_to :category
  has_attached_file :tip_background, :styles => {  big: '1000x1000>', :large => "600x600>", :medium => "300x300>", :thumb => "100x100>", :small => "20x20>" }, :default_url => "/images/:style/missing.png"
  validates_attachment_content_type :tip_background, :content_type => /\Aimage\/.*\Z/

  has_many :tip_categorizations
  has_many :categories, :through => :tip_categorizations
  accepts_nested_attributes_for :tip_categorizations, allow_destroy: true
  has_and_belongs_to_many :users,  :join_table => :tips_likes


def as_json(options={})
  super(:only => [:id, :category_id, :globalID ],
      :methods => [:title, :description, :photoUrl, :image_url, :mainTag, :tags, :order_in_category, :type ]
  )
end


def title
	name
end

def description
	explaination
end


def photoUrl
	tip_background.url(:large)
end

def image_url
  tip_background.url(:large)
end

def mainTag
  if category
	 category.id
  end
end

def tags
	return_cat = []
	categories.map { |e| return_cat.push(e.id) }
	return_cat
end

def order_in_category
	1
end

def type
	1
end

end
