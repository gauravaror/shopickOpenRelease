require 'active_support/concern'

module ShowToGender extend ActiveSupport::Concern
	included do
		enum show_to_gender: {:male => 0, :female => 1, :both => 2}
		before_save :execute_before_save_update_show_gender
	    def execute_before_save_update_show_gender
	        if self.category_id? && Category.where(id: self.category_id).size > 0
	          if self.category_id == Category.find_by(name: 'Men').id || Category.find(self.category_id).pc_parent_id == Category.find_by(name: 'Men').id
	            self.show_to_gender = 0
	          end
	          if self.category_id == Category.find_by(name: 'Women').id || Category.find(self.category_id).pc_parent_id == Category.find_by(name: 'Women').id
	            self.show_to_gender = 1
	          end
	        end
	        if self.show_to_gender == nil
	        	self.show_to_gender = 2
	        end
	    end
	end

	class_methods do
	    def negate_gender(gend)
	    	if (gend == "-1") 
	    		return -1
	    	end
	    	if (gend == "0")
	    		return 1
	    	end
	    	if (gend == "1")
	    		return 0
	    	end
	    	return -1
	    end

	    def process_show_for_gender(items, gender)
	    	return items.where.not(show_to_gender: negate_gender(gender))
	    end
	end
end