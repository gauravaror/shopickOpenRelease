require 'elasticsearch/model'

class Product < ActiveRecord::Base
  belongs_to :brand
  belongs_to :category
  has_many :image_urls
  has_many :product_images
  has_many :brand_updates
  has_many :find_thi

  
  accepts_nested_attributes_for :product_images
    
  has_many :product_attribute_values
  accepts_nested_attributes_for :product_attribute_values, allow_destroy: true
  
  has_many :product_tags, :dependent => :destroy
  accepts_nested_attributes_for :product_tags, allow_destroy: true
  has_many :tags, :through => :product_tags

  has_many :product_categorizations, :dependent => :destroy
  accepts_nested_attributes_for :product_categorizations, allow_destroy: true
  has_many :categories, :through => :product_categorizations

  has_many :product_features, :dependent => :destroy
  accepts_nested_attributes_for :product_features, allow_destroy: true
  has_many :features, :through => :product_features

  has_many :product_stores, :dependent => :destroy
  accepts_nested_attributes_for :product_stores, allow_destroy: true
  has_many :stores, :through => :product_stores

  has_many :tip_products
  has_many :tips, :through => :tip_products

  has_many :brand_update_products
  accepts_nested_attributes_for :brand_update_products, allow_destroy: true
  has_many :brand_updates, :through => :brand_update_products

  has_and_belongs_to_many :users,  :join_table => :products_likes

  include ShowToGender

  def as_json(options={})
      super(:only => [:id, :title, :name, :description, :globalID ],
        :methods => [:image_url, :productImageUrl, :brand_name, :brand_id]

    )
  end

  def name
    title
  end

  def description
    desc
  end


  def brand_id
    if brand
      brand.id
    end
  end
  
  def brand_name
    if brand
      brand.name
    end
  end

 def productImageUrl
    if product_images && product_images.first
      product_images.first.product_image.url(:large)
    end
  end

  def image_url
    if product_images && product_images.first
      product_images.first.product_image.url(:large)
    end
  end


  include Elasticsearch::Model
  include Elasticsearch::Model::Callbacks
 settings  :analysis => {
              :filter => {
                :title_ngram  => {
                  "type"      => "edgeNGram",
                  "min_gram"  => 2,
                  "max_gram"  => 8,
                   }
              },
              :analyzer => {
                :default => {
                  "tokenizer"    => "standard",
                  "type"         => "custom",
                   "filter" => [ "lowercase", "asciifolding", "title_ngram" ]}
              }
            } do
    mapping  do
    end
  end



end
Product.__elasticsearch__.create_index!
Product.import