class UserRedeemPick < ActiveRecord::Base
	belongs_to :user
	belongs_to :redeem_pick
end
