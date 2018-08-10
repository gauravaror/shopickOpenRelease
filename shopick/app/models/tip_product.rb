class TipProduct < ActiveRecord::Base
  belongs_to :product
  belongs_to :tip
end
