class Banner < ActiveRecord::Base
  has_attached_file :banner, :styles => {  big: '1000x1000>', :large => "600x600>", :medium => "300x300>", :thumb => "100x100>", :small => "20x20>" }, :default_url => "/images/:style/missing.png"
  validates_attachment_content_type :banner, :content_type => /\Aimage\/.*\Z/

  has_and_belongs_to_many :users,  :join_table => :banners_likes
  belongs_to :brand

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
	Banner.__elasticsearch__.create_index!
	Banner.import



end
