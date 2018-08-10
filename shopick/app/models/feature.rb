class Feature < ActiveRecord::Base
    has_many :feature_images
    accepts_nested_attributes_for :feature_images
    has_many :product_features
    has_many :brand_updates
    has_many :products, :through => :product_features
    has_attached_file :feature_logo, :styles => {   :large => "600x600>", :medium => "300x300>", :thumb => "100x100>", :small => "20x20>" }, :default_url => "/images/:style/missing.png"
    validates_attachment_content_type :feature_logo, :content_type => /\Aimage\/.*\Z/

end
