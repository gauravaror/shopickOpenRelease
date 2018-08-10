class FeatureImage < ActiveRecord::Base
    belongs_to :feature
    has_attached_file :feature_image, :styles => {  big: '1000x1000>', :large => "600x600>", :medium => "380x700>", :thumb => "100x100>", :small => "20x20" }, :default_url => "/images/:style/missing.png"
    validates_attachment_content_type :feature_image, :content_type => /\Aimage\/.*\Z/

end
