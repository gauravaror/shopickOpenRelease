require 'elasticsearch/model'

class Brand < ActiveRecord::Base
    has_many :products
    has_attached_file :brand_logo, :styles => { :large => "600x600>", :medium => "300x300>", :thumb => "100x100>", :small => "20x20>" }, :default_url => "/images/:style/missing.png"
    validates_attachment_content_type :brand_logo, :content_type => /\Aimage\/.*\Z/

    has_many :brand_categorizations, :dependent => :destroy
    has_many :categories, :through => :brand_categorizations
    accepts_nested_attributes_for :brand_categorizations, allow_destroy: true

    has_and_belongs_to_many :users,  :join_table => :brands_likes
    has_many :store_brands
    has_many :brand_updates
    has_many :affinity_scores
    has_many :stores, :through => :store_brands

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



	def as_json(options={})
  		super(:only => [:id, :name ],
  			:methods => [:logo_url, :tagline, :tags, :type, :order_in_category]
  		)
	end

	def logo_url
		brand_logo.url(:medium)
	end

	def tagline
		brand_line
	end

	def tags
		return_cat = []
		categories.map { |e| return_cat.push(e.id) }
		return_cat
	end

	def type
		1
	end

	def order_in_category
		1
	end


end
Brand.__elasticsearch__.create_index!
Brand.import
