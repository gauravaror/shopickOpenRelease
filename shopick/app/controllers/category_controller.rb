class CategoryController < ApplicationController
	def index 
		@category = Category.all
		render :json =>  { "tags" => @category.as_json(:root => false)}.to_json

	end

	def top_category
		@category =  Category.where(id_level: 10)
		render :json =>  { "top_tags" => @category.as_json(:root => false)}.to_json

	end

	def tag_category
		@category =  Category.where.not(pc_parent_id: nil)
		render :json =>  { "tags" => @category.as_json(:root => false)}.to_json
	end

	def category
		@category =  Category.where.not(pc_parent_id: nil)
		render :json =>   @category, root: false
	end


end
