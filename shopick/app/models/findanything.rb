class Findanything < ActiveRecord::Base
  validates_presence_of :description
  has_attached_file :findanything_image, :styles => {  big: '1000x1000>', :large => "600x600>", :medium => "300x300>", :thumb => "100x100>", :small => "20x20>" }, :default_url => "/images/:style/missing.png"
  validates_attachment_content_type :findanything_image, :content_type => /\Aimage\/.*\Z/


end

