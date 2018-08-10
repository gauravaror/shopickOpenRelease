class TipCategorization < ActiveRecord::Base
	belongs_to :tip
    belongs_to :category

end
