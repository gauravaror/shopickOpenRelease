class Notification < ActiveRecord::Base
	belongs_to :brand_update
	belongs_to :post

	has_attached_file :image_url, :styles => { :main => "1200x628>", :large => "600x600>", :medium => "300x300>", :thumb => "100x100>", :small => "20x20>" }, :default_url => ""
    validates_attachment_content_type :image_url, :content_type => /\Aimage\/.*\Z/


	has_many :notification_users
  	has_many :users, :through => :notification_users
  	accepts_nested_attributes_for :notification_users, allow_destroy: true

end
