class RedeemPick < ActiveRecord::Base
	has_attached_file :image_url, :styles => {  big: '1000x1000>', :large => "600x600>", :medium => "380x700>", :thumb => "100x100>", :small => "20x20" }, :default_url => "/images/:style/missing.png"
    validates_attachment_content_type :image_url, :content_type => /\Aimage\/.*\Z/


    def as_json(options={})
  		super(:only => [:id, :title, :description, :instruction, :globalID, :requiredPicks ],
      	:methods => [ :imageUrl ]
  		)
	end

	def imageUrl
		image_url.url(:medium)
	end
end
