class BrandController < ApplicationController
	def index
		@brand =  Brand.all
		render :json =>  { "brands" => @brand.as_json(:root => false)}.to_json
	end

	def brands_all
		##getting current user for likes
	    c_user_email = params[:user_email].presence
    	c_user = User.find_by_email(c_user_email)

		@brand =  Brand.where(visibleAsLikable: true )
		render :json =>  @brand, each_serializer: BrandSerializer, root: false, c_user: c_user
	end

	def updates
		if params[:filter]
			@brand =  Brand.joins(:brand_categorizations).where('brand_categorizations.category_id' => params[:filter]).joins(:brand_updates => :brand_update_categorizations).where('brand_update_categorizations.category_id' => params[:filter]).distinct
		elsif params[:brand_id]
			@category_id = Brand.find(params[:brand_id]).categories.first.id
			@brand =  Brand.joins(:brand_categorizations).where('brand_categorizations.category_id' => @category_id).joins(:brand_updates => :brand_update_categorizations).where('brand_update_categorizations.category_id' => @category_id).distinct.order("brands.id="+params[:brand_id]+" desc")
		else
			@brand =  Brand.joins(:brand_updates).distinct
		end
		render :json => @brand, root: false
	end


	def like_brand
	  brand = Brand.where(id: params[:id]).first
	  user_email = params[:user_email].presence
	  user = User.find_by_email(user_email)
	  if not user.brands.exists?(brand)
	  	user.brands << brand 
	    AffinityScore.process_brand_like(user, brand)
	  end
	  render :json => brand
	end

	def unlike_brand
	    brands = Brand.where(id: params[:id]).first
	    user_email = params[:user_email].presence
	    user = User.find_by_email(user_email)
	    if user.brands.exists?(brands)
	      user.brands.delete(brands)
	      AffinityScore.process_brand_unlike(user, brands)
	    end
	    render :json => brands
	end

end
