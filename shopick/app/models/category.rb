require 'elasticsearch/model'

class Category < ActiveRecord::Base
  has_many :product_categorizations
  has_many :products, :through => :product_categorizations

  has_many :tip_categorizations
  has_many :tips, :through => :tip_categorizations

  has_many :brand_categorizations
  has_many :brands, :through => :brand_categorizations

  has_many :brand_update_categorizations
  has_many :brand_updates, :through => :brand_update_categorizations

  has_many :store_categorizations
  has_many :stores, :through => :store_categorizations

  has_many :post_categorizations
  has_many :posts, :through => :post_categorizations
  
  has_many :product_attributes
  has_many :tips
  has_many :posts
  has_many :products
  has_many :post_collections
  has_many :brand_updates
  has_many :affinity_scores

   has_attached_file :category_logo, :styles => {   :large => "600x600>", :medium => "300x300>", :thumb => "100x100>", :small => "20x20>" }, :default_url => "/images/:style/missing.png"
   validates_attachment_content_type :category_logo, :content_type => /\Aimage\/.*\Z/

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
    mappings  do
      indexes :name
    end
  end


def get_top_category
  Category.find(id_level: 10)
end


def as_json(options={})
  super(:only => [:id, :name ],
      :methods => [:color, :abstract, :tag, :image_url, :order_in_category, :category ]

  )
end

def image_url
  if category_logo
    category_logo.url(:medium)
  end
end


def order_in_category
    1
end

def tag
  mapped_keyword
end

def abstract
  ""
end

def color
  "#2a56c6"
end

def category
  if pc_parent_id
    cat = Category.find(pc_parent_id)
  if cat
    cat.mapped_keyword
  end
else 
  mapped_keyword
end 
end
end
Category.__elasticsearch__.create_index!
Category.import

