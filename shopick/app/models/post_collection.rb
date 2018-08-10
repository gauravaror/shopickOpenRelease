class PostCollection < ActiveRecord::Base  

  has_attached_file :post_banner, :styles => {  big: '1000x1000>', :large => "600x600>", :medium => "300x300>", :thumb => "100x100>", :small => "20x20>" }, :default_url => "/images/:style/missing.png"
  validates_attachment_content_type :post_banner, :content_type => /\Aimage\/.*\Z/

  has_many :post_collection_posts
  accepts_nested_attributes_for :post_collection_posts, allow_destroy: true
  has_many :posts, :through => :post_collection_posts
  belongs_to :brand
  belongs_to :category
  include ShowToGender

  has_and_belongs_to_many :users,  :join_table => :post_collections_likes

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
	PostCollection.__elasticsearch__.create_index!
	PostCollection.import

def self.get_post_collections(user_id)    
    #@post_collection = PostCollection.find_by_sql("select  p.*, IFNULL(a.affinity_scores,1) as score from post_collections as p left join affinity_scores as a on (a.user_id = "+ user_id + " and a.brand_id = p.brand_id)  where p.featured = true order by  score desc, p.created_at desc");
    @post_collection = PostCollection.joins("left join affinity_scores as a on (a.user_id = 105 and  a.brand_id = post_collections.brand_id) ").where(featured: true).select("post_collections.*,IFNULL(a.affinity_scores,1) as score" ).order("score desc, post_collections.created_at desc")
    return @post_collection
end

def self.get_post_collections_brand(brand_id)    
    @post_collection = PostCollection.where(brand_id: brand_id).where(featured: true).order("created_at desc");
    return @post_collection
end

end
