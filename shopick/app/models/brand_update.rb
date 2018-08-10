class BrandUpdate < ActiveRecord::Base

  belongs_to :category
  has_attached_file :update_background, :styles => {  big: '1000x1000>', :large => "600x600>", :medium => "300x300>", :thumb => "100x100>", :small => "20x20>" }, :default_url => "/images/:style/missing.png"
  validates_attachment_content_type :update_background, :content_type => /\Aimage\/.*\Z/

  has_many :brand_update_categorizations
  has_many :categories, :through => :brand_update_categorizations
  accepts_nested_attributes_for :brand_update_categorizations, allow_destroy: true

  has_many :brand_update_products
  accepts_nested_attributes_for :brand_update_products, allow_destroy: true
  has_many :products, :through => :brand_update_products
  has_and_belongs_to_many :users,  :join_table => :brand_updates_likes
  include ShowToGender


  has_many :notifications
  
  belongs_to :product
  belongs_to :tip
  belongs_to :feature
  belongs_to :brand

  scope :category_id, -> (category_id) { where "brand_update_categorizations.category_id" => category_id }
  scope :brand_id, -> (brand_id) { where "brand_updates.brand_id" => brand_id }


	def as_json(options={})
  		super(:only => [:id, :product_id, :feature_id, :tip_id, :brand_id, :globalID ],
      	:methods => [:title, :description, :photoUrl, :image_url, :category_id, :mainTag, :brand_name, :tags, :order_in_category, :type ]
  		)
	end

	def title
		name
	end

	def description
		explaination
	end

	def photoUrl
		update_background.url(:big)
	end

	def image_url
		update_background.url(:big)
	end

	def category_id
		if category
			category.id
		end
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

	def brand_name
		if brand
			brand.name
		end
	end

	def type
		typeupdate
	end


def self.get_brand_updates(category_id, brand_id)
	@brand_updates =  BrandUpdate.where(nil) if category_id.to_i == -1
	@brand_updates =  BrandUpdate.joins(:brand_update_categorizations).where(nil) 
	@brand_updates = @brand_updates.category_id(category_id) if category_id.to_i !=  -1
	@brand_updates = @brand_updates.brand_id(brand_id) if brand_id.to_i != -1
 	return @brand_updates

end


end
