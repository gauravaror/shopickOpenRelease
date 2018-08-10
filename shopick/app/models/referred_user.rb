class ReferredUser < ActiveRecord::Base
	has_many :user
end
