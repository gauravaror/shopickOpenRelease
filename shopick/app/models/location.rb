class Location < ActiveRecord::Base
	has_many :stores

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
	Location.__elasticsearch__.create_index!
	Location.import


end
