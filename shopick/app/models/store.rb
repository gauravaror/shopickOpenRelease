class Store < ActiveRecord::Base
    belongs_to :brand
    has_many :posts
    belongs_to :location

    has_many :store_categorizations, :dependent => :destroy
    has_many :categories, :through => :store_categorizations
    accepts_nested_attributes_for :store_categorizations, allow_destroy: true

    has_many :store_brands, :dependent => :destroy
    has_many :brands, :through => :store_brands
    accepts_nested_attributes_for :store_brands, allow_destroy: true

    has_many :product_stores
    has_many :products, :through => :product_stores
    accepts_nested_attributes_for :product_stores, allow_destroy: true

def as_json(options={})
  super(:only => [:id, :name, :address, :phone, :order_in_category, :lat, :lon, :distance ], :methods => [:brand_id, :brand_logo]
  )
end

def brand_id
    if brands.first
        brands.first.id
    end
end

def brand_logo
  if brands.first
    brands.first.brand_logo.url(:thumb)
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
    mapping do
    end
  end
Store.__elasticsearch__.create_index!
Store.import
end
