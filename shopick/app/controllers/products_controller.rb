class ProductsController < ApplicationController
	before_filter :authenticate_user_from_token!, :except => [:updates_products, :product_global]
	before_filter :authenticate_user!, :except => [:updates_products, :product_global]


	def updates_products
		products = Product.joins(:brand_update_products).where('brand_update_products.brand_update_id' => params[:update_id])
		##getting current user for likes
		c_user_email = params[:user_email].presence
		c_user = User.find_by_email(c_user_email)
		render :json => products, each_serializer: ProductMiniSerializer, root: false, c_user: c_user
	end

	def presentation_products
		products = Product.joins(:tip_products).where('tip_products.tip_id' => params[:presentation_id])
		##getting current user for likes
		c_user_email = params[:user_email].presence
		c_user = User.find_by_email(c_user_email)
		render :json => products, each_serializer: ProductMiniSerializer, root: false, c_user: c_user
	end

	def updates_products_global
		@update = BrandUpdate.find_by(:globalID => params[:update_id]);
		##getting current user for likes
		c_user_email = params[:user_email].presence
		c_user = User.find_by_email(c_user_email)
		products = Product.joins(:brand_update_products).where('brand_update_products.brand_update_id' => @update.id)
		render :json => products, each_serializer: ProductMiniSerializer, root: false, c_user: c_user
	end

	def presentation_products_global
		@presentation =  Tip.find_by(:globalID => params[:presentation_id])
		##getting current user for likes
		c_user_email = params[:user_email].presence
		c_user = User.find_by_email(c_user_email)

		products = Product.joins(:tip_products).where('tip_products.tip_id' => @presentation.id)
		render :json => products, each_serializer: ProductMiniSerializer, root: false, c_user: c_user
	end



	def product
		##getting current user for likes
		c_user_email = params[:user_email].presence
		c_user = User.find_by_email(c_user_email)
		product = Product.find(params[:product_id])
		render :json => product, each_serializer: ProductSerializer, root: false, c_user: c_user, last_known_lat: params[:last_known_lat], last_known_lon: params[:last_known_lon]
	end


	def product_global
		##getting current user for likes
		c_user_email = params[:user_email].presence

		c_user = User.find_by_email(c_user_email)
		product = Product.where(globalID: params[:product_id]).first
		render :json => product, each_serializer: ProductSerializer, root: false, c_user: c_user, last_known_lat: params[:last_known_lat], last_known_lon: params[:last_known_lon]
	end


	def like_product
	  product = Product.where(globalID: params[:globalID]).first
	  user_email = params[:user_email].presence
	  user = User.find_by_email(user_email)
	  if not user.products.exists?(product)
	  	user.products << product 
	  	AffinityScore.process_product_like(user, product)
	  end 
	  render :json => product
      User.send_admin_users_notification(" user   : "+ user.email + " liked product with globalID : "+ params[:globalID],
        "http://shopick.co.in/admin/products?utf8=%E2%9C%93&q%5BglobalID_contains%5D="+params[:globalID]+"&commit=Filter&order=id_desc")
	end

	def unlike_product
	    product = Product.where(globalID: params[:globalID]).first
	    user_email = params[:user_email].presence
	    user = User.find_by_email(user_email)
	    if user.products.exists?(product)
	      user.products.delete(product)
	      AffinityScore.process_product_unlike(user, product)
	    end
	    render :json => product
      	User.send_admin_users_notification(" user   : "+ user.email + " unliked product with globalID : "+ params[:globalID],
        "http://shopick.co.in/admin/products?utf8=%E2%9C%93&q%5BglobalID_contains%5D="+params[:globalID]+"&commit=Filter&order=id_desc")
	end


	def find_product
		@findthis =  FindThi.new(findthis_params)
		@findthis.save
		   value  = params[:user_id]
		    if value.eql?  "-1"
		      value = params[:temp_user_id]
		    end
		    
		    if value.eql? "-1"
		    else
		      queryuser = User.find(value)
		      queryuser.update(:phoneno => params[:phoneno])
			  product = Product.where(globalID: params[:globalID]).first
		      AffinityScore.process_product_like(queryuser, product)
		      FirstNotificationJob.perform_in(10.seconds, 20, queryuser.id)
		    end
		User.send_admin_users_findthis_notification(@findthis)
		render :json => @findthis
	end

	def findthis_params
	    params.permit(:globalID, :user_id, :product_id, :lat, :lon, :phoneno)
	end
end
